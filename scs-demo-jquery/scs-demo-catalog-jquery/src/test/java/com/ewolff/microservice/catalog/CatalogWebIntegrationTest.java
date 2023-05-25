package com.ewolff.microservice.catalog;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CatalogApp.class)
@ActiveProfiles("test")
public class CatalogWebIntegrationTest {

	@Autowired
	private ItemRepository itemRepository;

	@LocalServerPort
	private int serverPort;

	private Item iPodNano;

	private RestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		if (iPodNano == null) {
			iPodNano = itemRepository.findByName("iPod nano").get(0);
		}
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
	}

	@Test
	public void IsItemReturned() {
		String url = catalogURL() + "/" + iPodNano.getId() + ".html";
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, url);

		assertTrue(body.contains("iPod nano"));
		assertTrue(body.contains("<div"));
		assertTrue(body.contains("<!DOCTYPE"));
	}

	@Test
	public void IsItemSnippetReturned() {
		String url = catalogURL() + "/" + iPodNano.getId() + ".snippet";
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, url);

		assertTrue(body.contains("iPod nano"));
		assertTrue(body.contains("<!DOCTYPE"));
	}

	private String catalogURL() {
		return "http://localhost:" + serverPort;
	}

	@Test
	public void FormReturned() {
		String url = catalogURL() + "/searchForm.html";
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, url);

		assertTrue(body.contains("<form"));
		assertTrue(body.contains("<div>"));
	}

	@Test
	public void SearchWorks() {
		String url = catalogURL() + "/searchByName.html?query=iPod";
		String body = restTemplate.getForObject(url, String.class);

		assertTrue(body.contains("iPod nano"));
		assertTrue(body.contains("<div"));
	}

	@Test
	public void IsChoiceReturnedAndSelectedWorks() {
		String url = catalogURL() + "/item-choice.snippet?selected=2&id=myId&name=myName";
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, url);

		assertTrue(body.contains("iPod nano"));
		assertTrue(body.contains("id=\"myId\""));
		assertTrue(body.contains("name=\"myName\""));
		assertTrue(body.contains("selected"));
		assertTrue(body.contains("<option"));
	}

	private <T> T getForMediaType(Class<T> value, MediaType mediaType, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(mediaType));

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<T> resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, value);

		return resultEntity.getBody();
	}

}
