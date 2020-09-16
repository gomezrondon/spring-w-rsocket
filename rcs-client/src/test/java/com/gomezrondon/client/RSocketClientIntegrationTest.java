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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
//@ExtendWith(SpringExtension.class)//@RunWith() // deprecated
@DirtiesContext
public class RSocketClientIntegrationTest {

    @Autowired
    TimeServiceImp service;

    @Test
    @DisplayName("testing test-time Rsocket endpoint")
    void test1() {
        //given
       // TimeServiceImp timeServiceImp = new TimeServiceImp(requester);

        //when
        Flux<String> streamTime = service.getStreamTime("Test-1");


        //then
         Assertions.assertNotNull(streamTime);
        Flux<String> takeElements = streamTime.take(5);
        Assertions.assertEquals(5, takeElements.count().block());
/*
        StepVerifier.create(streamTime)
                //.expectSubscription()
              //  .expectNextCount(5)
                .verifyComplete();
*/


    }

}
