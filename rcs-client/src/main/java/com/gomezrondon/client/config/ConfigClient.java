package com.gomezrondon.client.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Configuration
@Slf4j
public class ConfigClient {

    @Bean
    @Profile("dev")
    ApplicationListener<ApplicationReadyEvent> rsocketRequestListerner(RSocketRequester requester) {
        return event -> {
            requester
                    .route("test-time") // returns a stream
                    //.route("solo-texto") // returns 1 value
                    .data("Test")
                    .retrieveFlux(String.class) // or retrieveMono
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5) ))
                    .doOnError(IOException.class, e -> log.error(e.getMessage()))
                    .subscribe(System.out::println);
        };
    }

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .connectTcp("localhost", 8888)
                .block();
    }

}
