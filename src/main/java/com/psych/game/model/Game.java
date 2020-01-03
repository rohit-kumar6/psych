package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "games")
public class Game extends Auditable{
    @Getter
    @Setter
    // Not null for integer not blank for string
    @NotNull
    private int numRounds;

    @Getter
    @Setter
    private int currentRound = 0;

    @ManyToMany
    @Getter
    @Setter
    private Map<Player,Stats> playerStats;
    // games_player_stats
    // game_id, player_id, stats_id

    @Getter
    @Setter
    @ManyToMany
    private List<Player> players;
    // games_players
    // game_id,player_id

    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.JOINING;

//    @Getter
//    @Setter
//    @NotNull
//    private int numEllens;

    @ManyToOne
    @NotNull
    @Getter
    @Setter
    private Player leader;

    @OneToMany(mappedBy = "game")
    @Getter
    @Setter
    private List<Round> rounds;
}