package com.polus.fibicomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
/* @ComponentScan("com.polus.fibicomp.*") */
public class FibicompApplication {

	public static void main(String[] args) {
		SpringApplication.run(FibicompApplication.class, args);
	}
}
