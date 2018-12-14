package com.polus.fibicomp;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
/* @ComponentScan("com.polus.fibicomp.*") */
public class FibicompApplication {

	@PostConstruct
	void started() {
		System.out.println("timezone : " + TimeZone.getTimeZone("TimeZone"));
		TimeZone.setDefault(TimeZone.getTimeZone("TimeZone"));
	}

	public static void main(String[] args) {
		SpringApplication.run(FibicompApplication.class, args);
	}
}
