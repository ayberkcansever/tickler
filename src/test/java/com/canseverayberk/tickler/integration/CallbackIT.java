package com.canseverayberk.tickler.integration;

import com.canseverayberk.tickler.AbstractIT;
import com.canseverayberk.tickler.model.CallbackPayload;
import com.canseverayberk.tickler.model.Tickle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class CallbackIT extends AbstractIT {

    @Test
    void should_call_callback_url() throws InterruptedException, JsonProcessingException {
        // given
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String payload = new ObjectMapper().writeValueAsString(CallbackPayload.builder().id(1L).name("name").build());

        ClientAndServer mockServer = startClientAndServer(1080);
        MockRestCallback mockRestCallback = new MockRestCallback(payload, countDownLatch);

        mockServer
                .when(request().withPath("/callback"))
                .callback(mockRestCallback);

        Tickle tickle = Tickle.builder()
                .payload(payload)
                .restCallbackUrl("http://localhost:1080/callback")
                .ttl(3)
                .build();

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.exchange("http://localhost:19090/api/v1/tickle",
                HttpMethod.POST,
                new HttpEntity<>(tickle, headers),
                Void.class);

        // then
        assertThat(countDownLatch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(mockRestCallback.isRequestPayloadExpected()).isTrue();
    }

    class MockRestCallback implements ExpectationCallback {

        private String expectedRequestPayload;
        private String actualRequestPayload;
        private CountDownLatch countDownLatch;

        public MockRestCallback(String expectedRequestPayload, CountDownLatch countDownLatch) {
            this.expectedRequestPayload = expectedRequestPayload;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            actualRequestPayload = httpRequest.getBodyAsString() + "12313";
            countDownLatch.countDown();
            return response().withStatusCode(200);
        }

        public boolean isRequestPayloadExpected() {
            return expectedRequestPayload.equals(actualRequestPayload);
        }
    }

}
