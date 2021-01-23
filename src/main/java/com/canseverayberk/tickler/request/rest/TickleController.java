package com.canseverayberk.tickler.request.rest;

import com.canseverayberk.tickler.expiry.TickleSaver;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickle")
public class TickleController {

    private final TickleSaver tickleSaver;

    @PostMapping
    public void tickle(@Valid @RequestBody Tickle tickle) {
        log.info("Tickle requested: {}", tickle);
        tickleSaver.save(tickle);
    }

}
