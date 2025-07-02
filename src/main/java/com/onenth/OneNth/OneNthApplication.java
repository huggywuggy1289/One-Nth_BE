package com.onenth.OneNth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OneNthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneNthApplication.class, args);
	}

}
