package com.pdf.generator.model;

import java.util.Map;

public class PlayerStatistics {
	Map<String, Integer> substitutes;
	Map<String, Integer> goals; 
	Map<String, Integer> passes; 
	Map<String, Integer> shots; 
	Map<String, Integer> tackles; 
	Map<String, Integer> duels; 
	Map<String, Integer> dribbles; 
	Map<String, Integer> fouls; 
	Map<String, Integer> cards; 
	Map<String, Integer> penalty;
	
	public Map<String, Integer> getSubstitutes() {
		return substitutes;
	}
	public void setSubstitutes(Map<String, Integer> substitutes) {
		this.substitutes = substitutes;
	}
	public Map<String, Integer> getGoals() {
		return goals;
	}
	public void setGoals(Map<String, Integer> goals) {
		this.goals = goals;
	}
	public Map<String, Integer> getPasses() {
		return passes;
	}
	public void setPasses(Map<String, Integer> passes) {
		this.passes = passes;
	}
	public Map<String, Integer> getShots() {
		return shots;
	}
	public void setShots(Map<String, Integer> shots) {
		this.shots = shots;
	}
	public Map<String, Integer> getTackles() {
		return tackles;
	}
	public void setTackles(Map<String, Integer> tackles) {
		this.tackles = tackles;
	}
	public Map<String, Integer> getDuels() {
		return duels;
	}
	public void setDuels(Map<String, Integer> duels) {
		this.duels = duels;
	}
	public Map<String, Integer> getDribbles() {
		return dribbles;
	}
	public void setDribbles(Map<String, Integer> dribbles) {
		this.dribbles = dribbles;
	}
	public Map<String, Integer> getFouls() {
		return fouls;
	}
	public void setFouls(Map<String, Integer> fouls) {
		this.fouls = fouls;
	}
	public Map<String, Integer> getCards() {
		return cards;
	}
	public void setCards(Map<String, Integer> cards) {
		this.cards = cards;
	}
	public Map<String, Integer> getPenalty() {
		return penalty;
	}
	public void setPenalty(Map<String, Integer> penalty) {
		this.penalty = penalty;
	}
	
	@Override
	public String toString() {
		return "PlayerStatistics [substitutes=" + substitutes + ", goals=" + goals + ", passes=" + passes + ", shots="
				+ shots + ", tackles=" + tackles + ", duels=" + duels + ", dribbles=" + dribbles + ", fouls=" + fouls
				+ ", cards=" + cards + ", penalty=" + penalty + "]";
	}
	
	
	
}
