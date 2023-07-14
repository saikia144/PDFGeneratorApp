package com.pdf.generator.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf.generator.enums.GoalType;
import com.pdf.generator.model.Player;
import com.pdf.generator.model.PlayerStatistics;
import com.pdf.generator.repository.PlayerDao;
import com.pdf.generator.utils.URIBuilderUtil;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

//http://localhost:8081/player/details?league=323&season=2019&team=3479
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
	@Autowired
	PlayerDao pdao;

	@Value("${api.key}")
	private String apiKey;

	@Override
	public void getPlayerById(String team, String season, String league) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<Player> players = new ArrayList<>();
		try {
			URI uri = URIBuilderUtil.buildURI("https://v3.football.api-sports.io/players",team,season,league);
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("x-rapidapi-key", apiKey);

			CloseableHttpResponse response = httpClient.execute(httpGet);
			String responseBody = EntityUtils.toString(response.getEntity());

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody).get("response");

			players = processPlayerInfo(rootNode);

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		generatePlayersReport(players);
	}


	private List<Player> processPlayerInfo(JsonNode node) throws FileNotFoundException, IOException {
		List<Player> players = new ArrayList<>();
		for (JsonNode playerNode : node) {
			PlayerStatistics ps = extractPlayerStats(playerNode.get("statistics"));

			Player p = new Player();
			p.setName(playerNode.get("player").get("name").asText());
			p.setAge(playerNode.get("player").get("age").asInt());
			p.setBirthdate(playerNode.get("player").get("birth").get("date").asText());
			p.setCountry(playerNode.get("player").get("nationality").asText());
			p.setHeight(playerNode.get("player").get("height").asInt());
			p.setWeight(playerNode.get("player").get("weight").asInt());
			p.setPhoto(playerNode.get("player").get("photo").asText());
			p.setStatistics(ps);

			players.add(p);
			//pdao.save(p); // Save
		}
		return players;
	}

	private PlayerStatistics extractPlayerStats(JsonNode playerStats) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		PlayerStatistics ps = new PlayerStatistics();

		for (JsonNode stats : playerStats) {
			JsonNode cards = stats.get("cards");
			JsonNode goals = stats.get("goals");
			JsonNode shots = stats.get("shots");
			JsonNode passes = stats.get("passes"); //$F{statistics.cards}.get("red")
			JsonNode penalty = stats.get("penalty");

			Map<String, Integer> cardMap = mapper.convertValue(cards, new TypeReference<Map<String, Integer>>() {});
			Map<String, Integer> shotmap = mapper.convertValue(shots, new TypeReference<Map<String, Integer>>() {});
			Map<String, Integer> passMap = mapper.convertValue(passes, new TypeReference<Map<String, Integer>>() {});
			Map<String, Integer> penaltyMap = mapper.convertValue(penalty, new TypeReference<Map<String, Integer>>() {} );
			EnumMap<GoalType, Integer> egoalMap = mapper.convertValue(goals, new TypeReference<EnumMap<GoalType,Integer>>() {});
			//Map<String, Integer> game = mapper.convertValue(games, new TypeReference<Map<String, Integer>>() {});

			ps.setCards(cardMap);
			ps.setGoals(egoalMap);
			ps.setShots(shotmap);
			ps.setPasses(passMap);

		}
		return ps;
	}

	public void generatePlayersReport(List<Player> players) {
		//System.out.println("Inside generate method");
		try {
			InputStream input = getClass().getResourceAsStream("/test.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport report = JasperCompileManager.compileReport(design);


			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(players);
			Map<String, Object> parameters = new HashMap<>();
			//parameters.put("defaultValue", 0);

			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
			JasperExportManager.exportReportToPdfFile(print,"stats.pdf");

			System.out.println("PDF generated");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}










