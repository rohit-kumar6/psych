package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.Utils;
import com.psych.game.exceptions.InvalidActionForGameStateException;
import com.psych.game.exceptions.InvalidInputException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "games")
public class Game extends Auditable{
    @Getter
    @Setter
    @NotNull
    private int numRounds;

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Map<Player,Stats> playerStats = new HashMap<>();

    @Getter
    @Setter
    @ManyToMany
    @JsonIdentityReference
    private List<Player> players = new ArrayList<>();

    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.JOINING;

    @Getter
    @Setter
    private int hasEllen;

    @ManyToOne
    @NotNull
    @Getter
    @Setter
    @JsonIdentityReference
    private Player leader;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @Getter
    @Setter
    @JsonManagedReference
    private List<Round> rounds = new ArrayList<>();

    public Game(){}

    private Game(Builder builder) {
        this.numRounds = builder.numRounds;
        this.gameMode = builder.gameMode;
        this.hasEllen = builder.hasEllen;
        this.leader = builder.leader;
        try {
            addPlayer(leader);
        } catch (InvalidActionForGameStateException ignored) {}
    }

    public boolean hasPlayer(Player player){
        return playerStats.containsKey(player);
    }

    public void addPlayer(Player player) throws InvalidActionForGameStateException {
        if( ! gameStatus.equals(GameStatus.JOINING)){
            throw new InvalidActionForGameStateException("Cannot join game because it has already started.");
        }
        if(playerStats.containsKey(player)) return;
        players.add(player);
        playerStats.put(player, new Stats());
    }

    public static Builder newGame() {
        return new Builder();
    }

    public void startNewRound(){
        Round round = new Round(this, Utils.getRandomQuestion(),1);
        rounds.add(round);
        gameStatus = GameStatus.SUBMITTING_ANSWERS;
    }

    public void start() throws InvalidActionForGameStateException {
        if( ! gameStatus.equals(GameStatus.JOINING))
            throw new InvalidActionForGameStateException("Game had already started");
        startNewRound();
    }

    public Round currentRound(){
        return rounds.get(rounds.size()-1);
    }

    public void submitAnswer(Player player, String answer) throws InvalidActionForGameStateException {
        if(!gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidActionForGameStateException("Not accepting any answer at the moment");
        Round round = currentRound();
        round.submitAnswer(player,answer);
        if(round.getSubmittedAnswers().size() == players.size())
            gameStatus = GameStatus.SELECTING_ANSWERS;
    }

    public void selectAnswer(Player player, PlayerAnswer playerAnswer) throws InvalidActionForGameStateException, InvalidInputException {
        if(!gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidActionForGameStateException("Not selecting any answer at the moment");
        Round round = currentRound();
        round.selectAnswer(player,playerAnswer);
        if(round.getSelectedAnswers().size() == players.size())
        {
            if(rounds.size() < numRounds)
                gameStatus = GameStatus.GETTING_READY;
            else{
                gameStatus = GameStatus.OVER;
                // TODO : Update the stats of ellen
                // preodically update gamestats
            }
        }
    }

    public void getReady(Player player) {
        Round round = currentRound();
        round.getReady(player);
        if(round.getReadyPlayers().size() == players.size())
            // TODO : startNewRound should not always set round no 1
            startNewRound();
    }

    // ToDO
    public String getState() {
        return "";
        // TODO Allen answer should be added to answer when game in selecting state
    }

    public static final class Builder {
        private @NotNull int numRounds;
        private @NotNull GameMode gameMode;
        private int hasEllen;
        private @NotNull Player leader;

        public Builder() {
        }

        public Game build() {
            return new Game(this);
        }

        public Builder numRounds(int numRounds) {
            this.numRounds = numRounds;
            return this;
        }

        public Builder gameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder hasEllen(int hasEllen) {
            this.hasEllen = hasEllen;
            return this;
        }

        public Builder leader(Player leader) {
            this.leader = leader;
            return this;
        }
    }
}