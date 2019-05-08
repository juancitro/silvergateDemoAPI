package com.extra;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.model.Item;
import com.repository.ItemRepository;

@Configuration
public class LoadDatabase {
	
	Logger log = Logger.getLogger("");

	@Bean
	CommandLineRunner initDatabase(ItemRepository repository) {
		return args -> {
			log.info("Preloading " + repository.save(new Item("Pala", 20)));
			log.info("Preloading " + repository.save(new Item("Martillo", 10)));
		};
	}
	
}
