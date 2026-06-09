package com.devsu.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAccountsApplication.class, args);
	}
}