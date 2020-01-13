package com.psych.game.controller;

import com.psych.game.Utils;
import com.psych.game.exceptions.IllegalGameException;
import com.psych.game.exceptions.InsufficientPlayersException;
import com.psych.game.exceptions.InvalidActionForGameStateException;
import com.psych.game.exceptions.InvalidInputException;
import com.psych.game.model.*;
import com.psych.game.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play")
public class PlayEndPoint {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RoundRepository roundRepository;
    @Autowired
    PlayerAnswerRepository playerAnswerRepository;

    @GetMapping("/create-game/{pid}/{gm}/{nr}/{ellen}")
    public String createGame(@PathVariable(value = "pid") Long playerId,
                             @PathVariable(value = "gm") int gameMode,
                             @PathVariable(value = "nr") int numRounds,
                             @PathVariable(value = "ellen") int hasEllen) {

        Player player = playerRepository.findById(playerId).orElseThrow();
        Game game = new Game.Builder()
                .hasEllen(hasEllen)
                .numRounds(numRounds)
                .gameMode(GameMode.fromValue(gameMode))
                .leader(player)
                .build();
        gameRepository.save(game);
        return "Created game: " + game.getId() + ". Code: " + Utils.getSecretCodeFromID(game.getId());
    }

    @GetMapping("/join-game/{pid}/{gc}")
    public String joinGame(@PathVariable(value = "pid") Long playerId,
                           @PathVariable(value = "gc") String gameCode) throws InvalidActionForGameStateException {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        game.addPlayer(player);
        gameRepository.save(game);
        return "successfully joined";
    }

    @GetMapping("/start-game/{pid}/{gid}")
    public String startGame(@PathVariable(value = "pid") Long playerId,
                            @PathVariable(value = "gid") Long gameId) throws IllegalGameException, InsufficientPlayersException, InvalidActionForGameStateException {
        Game game = gameRepository.findById(gameId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        if(!game.getLeader().equals(player)) {
            throw new IllegalGameException("Player hasn't joined any such game");
        }
        if(game.getPlayers().size() < 2) {
            throw new InsufficientPlayersException("Cannot start a game without any friends");
        }
        game.start();
        gameRepository.save(game);
        return "game started";
    }

    // submitAnswer - pid, gid, answer
    @GetMapping("/submit-answer/{pid}/{gid}/{answer}")
    public String submitAnswer(@PathVariable(value = "pid") Long playerId,
                               @PathVariable(value = "gid") Long gameId,
                               @PathVariable(value = "answer") String answer) throws IllegalGameException, InvalidActionForGameStateException {
        Game game = gameRepository.findById(gameId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();

        if(!game.hasPlayer(player)){
            throw new IllegalGameException("Player has not joined the game yet");
        }
        game.submitAnswer(player,answer);
        gameRepository.save(game);
        return "submitted answer";
    }

    @GetMapping("/select-answer/{pid}/{gid}/{pansid}")
    public String selectAnswer(@PathVariable(value = "pid") Long playerId,
                               @PathVariable(value = "gid") Long gameId,
                               @PathVariable(value = "pansid") Long playerAnswerId) throws IllegalGameException, InvalidActionForGameStateException, InvalidInputException {
        Game game = gameRepository.findById(gameId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        PlayerAnswer playerAnswer = playerAnswerRepository.findById(playerAnswerId).orElseThrow();

        if(!game.hasPlayer(player)){
            throw new IllegalGameException("Player has not joined the game yet");
        }
        game.selectAnswer(player,playerAnswer);
        gameRepository.save(game);
        return "selected answer";
    }

    @GetMapping("/get-ready/{pid}/{gid}")
    public String getReady(@PathVariable(value = "pid") Long playerId,
                               @PathVariable(value = "gid") Long gameId) throws IllegalGameException {
        Game game = gameRepository.findById(gameId).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        if(!game.hasPlayer(player)){
            throw new IllegalGameException("Player has not joined the game yet");
        }
        game.getReady(player);
        gameRepository.save(game);
        return "player ready";
    }

    // TODO : convert the response to apropriate type
    @GetMapping("/game-state/{gid}")
    public String gameState(@PathVariable(value = "gid") Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        return game.getState();
    }

    // leaveGame
    @GetMapping("/leave/{pid}/{gc}")
    public String leaveGame(@PathVariable(value = "pid") Long playerId,
                            @PathVariable(value = "gc") String gameCode) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        return player.getName() + " Left the game";
    }

    // endGame
    @GetMapping("/end/{pid}/{gc}")
    public String endGame(@PathVariable(value = "pid") Long playerId,
                          @PathVariable(value = "gc") String gameCode) {
        return "Successfully ended game";
    }
}
