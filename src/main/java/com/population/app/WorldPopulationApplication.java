package com.population.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorldPopulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldPopulationApplication.class, args);
	}

}
