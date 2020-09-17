package com.gomezrondon.client;

import com.gomezrondon.client.service.TimeServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
//@ExtendWith(SpringExtension.class)//@RunWith() // deprecated
@DirtiesContext
@ActiveProfiles("test")
public class RSocketClientIntegrationTest {

    // @Autowired
    //  TimeServiceImp service;
    @Autowired
    RSocketRequester.Builder builder;

    // For every test the we need to re connect
    RSocketRequester getRequester(RSocketRequester.Builder builder) {
        return builder
                .connectTcp("localhost", 8888)
                .block();
    }

    @Test
    @DisplayName("testing solo-texto Rsocket endpoint")
    void test2() {

        TimeServiceImp service = new TimeServiceImp(getRequester(builder));

        //given
        Flux<String> stringFlux = service.getSoloTexto("Test-2");

        //then
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("Test-2")
                .verifyComplete();

    }

    @Test
    @DisplayName("testing test-time Rsocket endpoint")
    void test1() {

        TimeServiceImp service = new TimeServiceImp(getRequester(builder));
        //given
        Flux<String> streamTime = service.getStreamTime("Test-1");


        //when
         Assertions.assertNotNull(streamTime);
        Flux<String> takeElements = streamTime.take(5);

        //then
        StepVerifier.create(takeElements)
                 .expectSubscription()
                 .expectNextCount(5)
                .verifyComplete();

    }

}
