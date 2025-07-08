package com.onenth.OneNth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.PropertySource; // +
@SpringBootApplication
@EnableJpaAuditing
@PropertySource("classpath:env.properties") // +
public class OneNthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneNthApplication.class, args);
	}

}
