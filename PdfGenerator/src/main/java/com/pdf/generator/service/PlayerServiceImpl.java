package com.pdf.generator.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
			int playerId = playerNode.get("player").get("id").asInt();
			String name = playerNode.get("player").get("lastname").asText();
			String photo = playerNode.get("player").get("photo").asText();
			PlayerStatistics ps = extractPlayerStats(playerNode.get("statistics"));

			// String s = ImageUtil.imageDownloader(photo, name);
			Player p = new Player();
			p.setPid(playerId);
			p.setLastname(name);
			p.setStatistics(ps);
			p.setPhoto(photo);

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

			Map<String, Integer> cardMap = mapper.convertValue(cards, new TypeReference<Map<String, Integer>>() {});
			Map<GoalType, Integer> goalMap = convertGoalMap(goals, mapper);

			ps.setCards(cardMap);
			ps.setGoals(goalMap);
		}
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

	//	public void generatePlayersReport(List<Player> players) {
	//	    try {
	//	       
	//	        InputStream mainReportTemplate = getClass().getResourceAsStream("/demo.jrxml");
	//
	//	        InputStream subreportTemplate = getClass().getResourceAsStream("/stats.jrxml");
	//
	//	       
	//	        JasperReport subreport = JasperCompileManager.compileReport(subreportTemplate);
	//	        
	//	        // Create a map of parameters to be passed to the report (if any)
	//	        Map<String, Object> parameters = new HashMap<>();
	//	        parameters.put("subreportTemplate", subreport);
	//	        parameters.put("SUBREPORT_DIR", "classpath:/");
	//	        // Create a JasperReport object from the main report template
	//	        JasperReport mainReport = JasperCompileManager.compileReport(mainReportTemplate);
	//
	//	       
	//	       
	//	        // Create a JRBeanCollectionDataSource using the players list
	//	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(players);
	//
	//	        // Generate the JasperPrint object by filling the report with data
	//	        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
	//
	//	        // Export the JasperPrint object to a byte array in PDF format
	//	        JasperExportManager.exportReportToPdfFile(jasperPrint,"player.pdf");
	//
	//	        // Save the PDF to a file or perform other actions as needed
	//	        // ...
	//
	//	    } catch (JRException e) {
	//	        // Handle exception accordingly
	//	        e.printStackTrace();
	//	    }
	//	}


	//	private void generatePlayersReport(List<Player> players) {
	//		try {
	//			// Load the JasperDesign from an XML file
	//			InputStream inputStream = getClass().getResourceAsStream("/pdf.jrxml");
	//			JasperDesign design = JRXmlLoader.load(inputStream);
	//			// Compile the JasperDesign into a JasperReport object
	//			JasperReport report = JasperCompileManager.compileReport(design);
	//
	//			// Set up parameters for the report, if needed
	//			Map<String, Object> parameters = new HashMap<>();
	//			//parameters.put("players", players);
	//			// Add any necessary parameters
	//
	//			// Create a data source with the players' information
	//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(players);
	//
	//			// Fill the report with data and parameters
	//			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
	//			
	//			// Export the report to PDF
	//			JasperExportManager.exportReportToPdfFile(print, "players_report.pdf");
	//
	//			System.out.println("PDF report generated successfully.");
	//		} catch (JRException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public void generatePlayersReport(List<Player> players) {
		System.out.println("Inside generate method");
		try {
			InputStream input = getClass().getResourceAsStream("/test.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport report = JasperCompileManager.compileReport(design);


			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(players);
			Map<String, Object> parameters = new HashMap<>();

			System.out.println(dataSource.getData());
			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
			JasperExportManager.exportReportToPdfFile(print,"stat.pdf");
			System.out.println("PDF generated");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
