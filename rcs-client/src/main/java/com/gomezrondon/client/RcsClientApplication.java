package com.gomezrondon.client;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;

@SpringBootApplication
public class RcsClientApplication {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(RcsClientApplication.class, args);
		System.in.read();
	}


	@Bean
	ApplicationListener<ApplicationReadyEvent> rsocketRequestListerner(RSocketRequester requester) {
		return event -> {
			requester
					.route("test-time") // returns a stream
					//.route("solo-texto") // returns 1 value
					.data("Test")
					.retrieveFlux(String.class)
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
