package com.canseverayberk.tickler.integration;

import com.canseverayberk.tickler.AbstractIT;
import com.canseverayberk.tickler.configuration.KafkaEventStreams;
import com.canseverayberk.tickler.model.CallbackPayload;
import com.canseverayberk.tickler.model.Tickle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestCallbackIT extends AbstractIT {

    @Autowired
    private KafkaEventStreams kafkaEventStreams;

    private ClientAndServer mockServer;

    @BeforeAll
    public void startMockServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterAll
    public void stopMockServer() {
        mockServer.stop(true);
    }

    @BeforeEach
    public void setup() {
        mockServer.reset();
    }

    @Test
    void should_call_callback_url_when_tickle_expires_after_rest_request() throws InterruptedException, JsonProcessingException {
        // given
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String payload = new ObjectMapper().writeValueAsString(CallbackPayload.builder().id(1L).name("name").build());

        MockRestCallback mockRestCallback = new MockRestCallback(payload, countDownLatch);
        mockServer
                .when(request().withPath("/callback"))
                .callback(mockRestCallback);

        Tickle tickle = Tickle.builder()
                .payload(payload)
                .restCallbackUrl("http://localhost:1080/callback")
                .ttl(3L)
                .build();

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.exchange("http://localhost:9090/api/v1/tickle",
                HttpMethod.POST,
                new HttpEntity<>(tickle, headers),
                Void.class);

        // then
        assertThat(countDownLatch.await(30, TimeUnit.SECONDS)).isTrue();
        assertThat(mockRestCallback.isRequestPayloadExpected()).isTrue();
    }

    @Test
    void should_call_callback_url_when_tickle_expires_after_consuming_from_kafka() throws InterruptedException, JsonProcessingException {
        // given
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String payload = new ObjectMapper().writeValueAsString(CallbackPayload.builder().id(1L).name("name").build());

        MockRestCallback mockRestCallback = new MockRestCallback(payload, countDownLatch);
        mockServer
                .when(request().withPath("/callback"))
                .callback(mockRestCallback);

        Tickle tickle = Tickle.builder()
                .payload(payload)
                .restCallbackUrl("http://localhost:1080/callback")
                .ttl(3L)
                .build();

        // when
        kafkaEventStreams.tickleRequestInput().send(MessageBuilder.withPayload(tickle).build());

        // then
        assertThat(countDownLatch.await(30, TimeUnit.SECONDS)).isTrue();
        assertThat(mockRestCallback.isRequestPayloadExpected()).isTrue();
    }

    class MockRestCallback implements ExpectationCallback {

        private String expectedRequestPayload;
        private String actualRequestPayload;
        private CountDownLatch countDownLatch;

        public MockRestCallback() {
        }

        public MockRestCallback(String expectedRequestPayload, CountDownLatch countDownLatch) {
            this.expectedRequestPayload = expectedRequestPayload;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            actualRequestPayload = httpRequest.getBodyAsString();
            countDownLatch.countDown();
            return response().withStatusCode(200);
        }

        public boolean isRequestPayloadExpected() {
            return expectedRequestPayload.equals(actualRequestPayload);
        }
    }

}
