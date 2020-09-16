package com.gomezrondon.springrsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringRsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRsocketApplication.class, args);
	}

}

@RestController
class TimeController {

	private final TimeService timeService;

	TimeController(TimeService timeService) {
		this.timeService = timeService;
	}

	@MessageMapping("test-time")
	Flux<String> getTime(String someValue) {
		return this.timeService.time(someValue);
	}

	@MessageMapping("solo-texto")
	Flux<String> soloTexto(String someValue) {
		return Flux.just(someValue);
	}

/*	@GetMapping(value = "/times/{somevalue}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	Flux<String> getTime2(  @PathVariable String somevalue) {
		return this.timeService.time(somevalue);
	}*/
}


@Service
class TimeService {
	Flux<String> time(String someValue) {
		return Flux.fromStream(Stream.generate(()-> someValue +" "+ LocalDateTime.now().toString()+"\n"))
				.delayElements(Duration.ofSeconds(1));
	}
}