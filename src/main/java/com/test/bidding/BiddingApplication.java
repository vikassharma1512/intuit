package com.test.bidding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.test")
@Import(JdbcConfig.class)
public class BiddingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingApplication.class, args);
	}

}
