package com.canseverayberk.tickler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TicklerApplication {

	public static boolean I_AM_LEADER = false;

	public static void main(String[] args) {
		SpringApplication.run(TicklerApplication.class, args);
	}

}
