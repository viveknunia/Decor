package com.ecomm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EcommApplication {

	public static void main(String[] args) {
		log.info("Starting Ecomm Application");
		SpringApplication.run(EcommApplication.class, args);
		log.info("Ecomm Application started successfully");
	}
}
