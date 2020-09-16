package com.gomezrondon.client.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Service
@Slf4j
public class TimeServiceImp {

    private final RSocketRequester rSocketRequester;

    public TimeServiceImp(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }



    public Flux<String> getStreamTime(String someValue) {
        log.info("getStreamTime >> someValue: "+someValue);
        return rSocketRequester
                .route("test-time") // returns a stream
                .data(someValue)
                .retrieveFlux(String.class) // or retrieveMono
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()));

    }


    public Flux<String> getSoloTexto(String someValue) {
        return rSocketRequester
                .route("solo-texto") // returns 1 value
                .data(someValue)
                .retrieveFlux(String.class) // or retrieveMono
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()));

    }

}
