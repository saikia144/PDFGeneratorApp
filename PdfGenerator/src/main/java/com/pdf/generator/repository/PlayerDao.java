package com.pdf.generator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdf.generator.model.Player;

@Repository
public interface PlayerDao extends JpaRepository<Player, Integer>{
	
}
