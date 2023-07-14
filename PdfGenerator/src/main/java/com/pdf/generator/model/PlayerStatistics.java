package com.pdf.generator.model;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;

import com.pdf.generator.enums.GoalType;

@Embeddable
public class PlayerStatistics {

	@ElementCollection
	@CollectionTable(name = "player_substitutes", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "substitute_type")
	@Column(name = "count")
	private Map<String, Integer> substitutes;

	@ElementCollection
	@CollectionTable(name = "player_goals", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyEnumerated(EnumType.STRING) // for enum. not sure
	@MapKeyColumn(name = "goal_type")
	@Column(name = "count")
	//private Map<String, Integer> goals;
	private Map<GoalType, Integer> goals; // Use enum as the map key type
	
	@ElementCollection
	@CollectionTable(name = "player_passes", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "pass_type")
	@Column(name = "count")
	private Map<String, Integer> passes;

	@ElementCollection
	@CollectionTable(name = "player_shots", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "shot_type")
	@Column(name = "count")
	private Map<String, Integer> shots;

	@ElementCollection
	@CollectionTable(name = "player_tackles", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "tackle_type")
	@Column(name = "count")
	private Map<String, Integer> tackles;

	@ElementCollection
	@CollectionTable(name = "player_duels", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "duel_type")
	@Column(name = "count")
	private Map<String, Integer> duels;

	@ElementCollection
	@CollectionTable(name = "player_dribbles", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "dribble_type")
	@Column(name = "count")
	private Map<String, Integer> dribbles;

	@ElementCollection
	@CollectionTable(name = "player_fouls", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "foul_type")
	@Column(name = "count")
	private Map<String, Integer> fouls;

	@ElementCollection
	@CollectionTable(name = "player_cards", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "card_type")
	@Column(name = "count")
	private Map<String, Integer> cards;

	@ElementCollection
	@CollectionTable(name = "player_penalty", joinColumns = @JoinColumn(name = "player_id"))
	@MapKeyColumn(name = "penalty_type")
	@Column(name = "count")
	private Map<String, Integer> penalty;
	


	public Map<String, Integer> getSubstitutes() {
		return substitutes;
	}

	public void setSubstitutes(Map<String, Integer> substitutes) {
		this.substitutes = substitutes;
	}

	public Map<GoalType, Integer> getGoals() {
		return goals;
	}

	public void setGoals(Map<GoalType, Integer> goals) {
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

}

//@ElementCollection
//@CollectionTable(name="player_goals", joinColumns = @JoinColumn(name = "player_id"))
//@MapKeyColumn(name = "goal_type")
//@Column(name = "count")
//private Map<String, Integer> goals;
//
//public Map<String, Integer> getGoals() {
//	return goals;
//}
//
//public void setGoals(Map<String, Integer> goals) {
//	this.goals = goals;
//}


	//<field name="count" class="java.lang.Integer">
	//<property name="com.jaspersoft.studio.field.name" value="count"/>
	//<property name="com.jaspersoft.studio.field.label" value="count"/>
	//<property name="com.jaspersoft.studio.field.tree.path" value="player_goals"/>
	//</field>

//<textField textAdjust="StretchHeight" isBlankWhenNull="true">
//<reportElement style="Detail" positionType="Float" x="215" y="41" width="338" height="18" uuid="855c4cb9-f870-4244-b60c-6d3498217fb1"/>
//<textElement>
//	<font size="14" isBold="true"/>
//</textElement>
//<textFieldExpression><![CDATA[$F{count}]]></textFieldExpression>
//</textField>

