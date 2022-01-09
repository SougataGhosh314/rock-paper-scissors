package com.sougata.rockpaperscissors.repository;

import com.sougata.rockpaperscissors.model.GameStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStatRepository extends JpaRepository<GameStat, String> {
}
