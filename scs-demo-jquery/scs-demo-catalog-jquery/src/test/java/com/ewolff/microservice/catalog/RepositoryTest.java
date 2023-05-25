package com.ewolff.microservice.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = CatalogApp.class)
@ActiveProfiles("test")
public class RepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void AreAllIPodReturned() {

		assertEquals(3, itemRepository.findByNameContaining("iPod").size());
		assertTrue(itemRepository.findByNameContaining("iPod").stream()
				.anyMatch(s -> s.getName().equals("iPod touch")));

	}
}
