package com.pdf.generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdf.generator.service.PlayerService;

@RestController
@RequestMapping("/player")
public class PlayerController {
	
	@Autowired
	PlayerService ps;
	
	//3479
	@GetMapping("/details")
	public void fetchPlayerDetails(@RequestParam String team,@RequestParam String season, @RequestParam String league) {
		System.out.println("Your team id is: "+ team);
		ps.getPlayerById(team,season,league);
	}
}
