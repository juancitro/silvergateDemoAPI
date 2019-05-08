package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SilvergateDemoApiApplication {

	public static void main(String[] args) {
		/*
		 * Items pre-cargados en la BD:
		 * 		* id=1, name=Pala, price=20
		 * 		* id=2, name=Martillo, price=10 
		 */
		SpringApplication.run(SilvergateDemoApiApplication.class, args);
	}

}
