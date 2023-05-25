package com.ewolff.microservice.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@ComponentScan
@EnableAutoConfiguration
@Component
public class CatalogApp {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogApp(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	public void generateTestData() {
		itemRepository.save(new Item("iPod", "Just an MP3 player", 42.0));
		itemRepository.save(new Item("iPod touch", "A tiny MP3 player", 21.0));
		itemRepository.save(new Item("iPod nano", "A small MP3 player", 1.0));
		itemRepository.save(new Item("Apple TV", "A set top box for your TV", 100.0));
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogApp.class, args);
	}

}
