package com.pdf.generator.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf.generator.enums.GoalType;
import com.pdf.generator.model.Player;
import com.pdf.generator.model.PlayerStatistics;
import com.pdf.generator.repository.PlayerDao;

//http://localhost:8081/player/details?league=323&season=2019&team=3479
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService{
	@Autowired
	PlayerDao pdao; 

	@Value("${api.key}")
	private String apiKey;
	
	@Override
	public void getPlayerById(String team, String season, String league) {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		URIBuilder uriBuilder;
		try {
			uriBuilder = new URIBuilder("https://v3.football.api-sports.io/players");
			uriBuilder.setParameter("league", league);
			uriBuilder.setParameter("season", season);
			uriBuilder.setParameter("team", team);

			URI uri = uriBuilder.build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("x-rapidapi-key", apiKey);

			CloseableHttpResponse response = httpClient.execute(httpGet);

			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(responseBody);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody).get("response");

			playerInfo(rootNode);

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	private void playerInfo(JsonNode node) {
		for (JsonNode playerNode : node) {
			int playerId = playerNode.get("player").get("id").asInt();
			String name = playerNode.get("player").get("lastname").asText();
			PlayerStatistics ps = stats(playerNode.get("statistics")); // Pass only the playerNode's statistics

			Player p = new Player();
			p.setPid(playerId);
			p.setLastname(name);
			p.setStatistics(ps);
			pdao.save(p); // Save the entity here
		}
	}

	private PlayerStatistics stats(JsonNode playerStats) {
	    ObjectMapper mapper = new ObjectMapper();
	    PlayerStatistics ps = new PlayerStatistics();
	    
	    for (JsonNode stats : playerStats) {
	        JsonNode cards = stats.get("cards");
	        JsonNode goals = stats.get("goals");
	        Map<String, Integer> cardMap = mapper.convertValue(cards, new TypeReference<Map<String, Integer>>() {});
	        Map<GoalType, Integer> goalMap = convertGoalMap(goals, mapper);

	        ps.setCards(cardMap);
	        ps.setGoals(goalMap);
	    }

	    System.out.println(ps.getCards());
	    return ps;
	}

	private Map<GoalType, Integer> convertGoalMap(JsonNode goals, ObjectMapper mapper) {
	    Map<GoalType, Integer> goalMap = new HashMap<>();

	    Iterator<Map.Entry<String, JsonNode>> iterator = goals.fields();
	    while (iterator.hasNext()) {
	        Map.Entry<String, JsonNode> entry = iterator.next();
	        String goalTypeString = entry.getKey();
	        int goalValue = entry.getValue().asInt();

	        GoalType goalType = GoalType.fromString(goalTypeString);
	        if (goalType != null) {
	            goalMap.put(goalType, goalValue);
	        }
	    }

	    return goalMap;
	}

}
