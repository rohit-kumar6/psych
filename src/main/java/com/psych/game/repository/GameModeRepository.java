package com.psych.game.repository;

import com.psych.game.model.GameMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameModeRepository extends JpaRepository<GameMode,Long> {
}
