package com.pdf.generator.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/app")
@CrossOrigin(origins = "*")
public class PdfGenreatorController {
	@GetMapping("/players")
	public String showLeagues() throws URISyntaxException, IOException, InterruptedException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		URIBuilder uriBuilder = new URIBuilder("https://v3.football.api-sports.io/players");
		uriBuilder.setParameter("league", "323");
		uriBuilder.setParameter("season", "2019");
		uriBuilder.setParameter("team", "3479");

		URI uri = uriBuilder.build();

		HttpGet httpget = new HttpGet(uri);
		httpget.setHeader("x-rapidapi-key", "5d9298fee11597666a5aa6a5cca54cc9");
		httpget.setHeader("x-rapidapi-host", "v3.football.api-sports.io");

		CloseableHttpResponse response = httpClient.execute(httpget);

		try {
			// Read the response body as a string
			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(responseBody);
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(responseBody);

			int totalPages = rootNode.get("paging").get("total").asInt();
			int currentPage = 1;

			do {
				// Process the current page
				System.out.println("*****"+currentPage+"********");
				processPage(rootNode);

				// Check if there are more pages to fetch
				if (currentPage < 2) {
					// Delay before making the next request
					Thread.sleep(5000); // 5 seconds (adjust the delay as needed)

					// Increment the current page and update the URI
					currentPage++;
					uriBuilder.setParameter("page", String.valueOf(currentPage));
					uri = uriBuilder.build();

					// Make the next request
					httpget.setURI(uri);

					responseBody = EntityUtils.toString(response.getEntity());
					rootNode = objectMapper.readTree(responseBody);
				}
			} while (currentPage < 2);
		} finally {
			response.close();
			System.out.println("Done");
		}

		return null;
	}

	@GetMapping("/player")
	public void showPlayer() throws URISyntaxException, IOException, InterruptedException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		URIBuilder uriBuilder = new URIBuilder("https://v3.football.api-sports.io/players");
		uriBuilder.setParameter("league", "323");
		uriBuilder.setParameter("season", "2019");
		uriBuilder.setParameter("team", "3479");

		URI uri = uriBuilder.build();

		HttpGet httpget = new HttpGet(uri);
		httpget.setHeader("x-rapidapi-key", "5d9298fee11597666a5aa6a5cca54cc9");
		//httpget.setHeader("x-rapidapi-host", "v3.football.api-sports.io");

		CloseableHttpResponse response = httpClient.execute(httpget);

		try {
			// Read the response body as a string
			String responseBody = EntityUtils.toString(response.getEntity());
			
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(responseBody);

			int totalPages = rootNode.get("paging").get("total").asInt();

			// Process the first page
			System.out.println("***** 1 ********");
			processPage(rootNode);

		} finally {
			response.close();
			System.out.println("Done");
		}

	}


	private void processPage(JsonNode rootNode) {
		// Extract and process player information from the current page
		JsonNode playersNode = rootNode.get("response");
		int counter = 1;
		for (JsonNode playerNode : playersNode) {
			String name = playerNode.get("player").get("name").asText();
			String id = playerNode.get("player").get("id").asText();
			String country = playerNode.get("player").get("nationality").asText();

			// Do something with the extracted fields (e.g., print them)
			System.out.println(counter++ + " ID: " + id + " Name: " + name + " Country: " + country);
		}
	}

	
}
