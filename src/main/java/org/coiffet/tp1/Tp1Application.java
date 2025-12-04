package org.coiffet.tp1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Tp1Application {

	public static void main(String[] args) {

		SpringApplication.run(Tp1Application.class, args);
	}


	@GetMapping("/bonjour")
	public String hello() {
		return "Hello world!";
	}


}