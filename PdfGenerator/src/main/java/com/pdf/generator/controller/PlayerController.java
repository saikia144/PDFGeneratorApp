package com.pdf.generator.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf.generator.model.Player;
import com.pdf.generator.model.PlayerStatistics;

@RestController
@RequestMapping("/player")
public class PlayerController {
	@Value("${api.key}")
	private String apiKey;
	
	@GetMapping("/details")
	public void getPlayersDetails() throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		URIBuilder uriBuilder = new URIBuilder("https://v3.football.api-sports.io/players");
		uriBuilder.setParameter("league", "323");
		uriBuilder.setParameter("season", "2019");
		uriBuilder.setParameter("team", "3479");
		
		URI uri = uriBuilder.build();
		
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("x-rapidapi-key", apiKey);
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		
		try {
			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(responseBody);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody).get("response");
			
			playerInfo(rootNode);
			stats(rootNode);

		} finally {
			
		}
	}
	
	private void playerInfo(JsonNode node) {
		Player p = new Player();
		for (JsonNode playerNode : node) {
		    int playerId = playerNode.get("player").get("id").asInt();
		    String name  = playerNode.get("player").get("lastname").asText();
		    p.setId(playerId);p.setLastname(name);
		    System.out.println(p);
		}

	}
	
//	Just a demo in the demo branch
	private void stats(JsonNode node) {
	    ObjectMapper mapper = new ObjectMapper();
	    
	    //JsonNode responseArray = node.get("response");
	    for (JsonNode playerNode : node) {
	        JsonNode statisticsArray = playerNode.get("statistics");
	        for (JsonNode statisticsNode : statisticsArray) {
	            JsonNode cards = statisticsNode.get("cards");
	            Map<String, Integer> cardMap = mapper.convertValue(cards, new TypeReference<Map<String, Integer>>() {});
	            PlayerStatistics s = new PlayerStatistics();
	            s.setCards(cardMap);
	            System.out.println(s);
	        }
	    }
	}


}
