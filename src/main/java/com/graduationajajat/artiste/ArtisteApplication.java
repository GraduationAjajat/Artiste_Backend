package com.graduationajajat.artiste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArtisteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtisteApplication.class, args);
	}

}
