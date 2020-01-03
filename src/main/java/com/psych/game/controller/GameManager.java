package com.psych.game.controller;

import com.psych.game.Utils;
import com.psych.game.model.Game;
import com.psych.game.model.GameMode;
import com.psych.game.model.GameStatus;
import com.psych.game.model.Player;
import com.psych.game.repository.GameRepository;
import com.psych.game.repository.PlayerRepository;
import com.psych.game.repository.QuestionRepository;
import com.psych.game.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameManager {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RoundRepository roundRepository;

    @GetMapping("/create/{pid}/{gm}/{nr}//{num_ellens}")
    public String createGame(@PathVariable(value = "pid") Long playerId,
                             @PathVariable(value = "gm") int gameMode,
                             @PathVariable(value = "nr") int numRounds) {
        //@PathVariable(value = "num_ellens") int numEllens) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        GameMode mode = null;
        switch (gameMode) {
            case 1:
                mode = GameMode.IS_THIS_A_FACT;
                break;
            case 2:
                mode = GameMode.UNSCRAMBLE;
                break;
            case 3:
                mode = GameMode.WORD_UP;
                break;
        }

        Game game = new Game();
        game.setNumRounds(numRounds);
        game.setLeader(player);
        game.setGameMode(mode);
//        game.setNumEllens(numEllens);

        // TODO: ADD player, getting nullException
        // game.getPlayers().add(player);
        // or else when the player click start game at that time call join api for leader side

        gameRepository.save(game);
        return "" + game.getId() + "-" + Utils.getSecretCodeFromID(game.getId());
    }

    @GetMapping("/join/{pid}/{gc}")
    public String joinGame(@PathVariable(value = "pid") Long playerId,
                           @PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        if (!game.getGameStatus().equals(GameStatus.JOINING))
            return "Game status is not joining";

        Player player = playerRepository.findById(playerId).orElseThrow();
        game.getPlayers().add(player);

        // TODO : Once game ends it should delete that from games_players tabel
        gameRepository.save(game);
        return "successfully joined";
    }

    @GetMapping("/start/{pid}/{gc}")
    public String startGame(@PathVariable(value = "pid") Long playerId,
                            @PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();

        if (player != game.getLeader()) {
            return "Game leader should start the game";
        }

        if (game.getGameStatus() != GameStatus.JOINING) {
            return "Game already started ";
        }

        if (game.getPlayers().size() < 2) {
            return "Game should atleast contain 2 players ";
        }

        game.setGameStatus(GameStatus.IN_PROGRESS);
        game.setCurrentRound(1);

        gameRepository.save(game);
        return "Successfully started game";
    }

    @GetMapping("/end/{pid}/{gc}")
    public String endGame(@PathVariable(value = "pid") Long playerId,
                          @PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();

        if (player != game.getLeader()) {
            return "Game leader should end the game";
        }
        game.setGameStatus(GameStatus.OVER);
        gameRepository.save(game);
        return "Successfully ended game";
    }

    // getGameState - gid
    // JSON - current round - game stats of each player
    // - current round state - submitting-answer, selecting-answers-round-over
    @GetMapping("/gameState/{gc}")
    public String gameState(@PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        if (game.getNumRounds() == game.getCurrentRound()) {
            game.setGameStatus(GameStatus.OVER);
        }
        game.setCurrentRound(game.getCurrentRound() + 1);
        return "";
    }

    // submitAnswer - pid, gid, answer
    @GetMapping("/submitAnswerr/{pid}/{gc}/{answer}")
    public String submitAnswer(@PathVariable(value = "pid") Long playerId,
                               @PathVariable(value = "gc") String gameCode,
                               @PathVariable(value = "answer") String answer) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        return "";
    }


    @GetMapping("/leave/{pid}/{gc}")
    public String leaveGame(@PathVariable(value = "pid") Long playerId,
                            @PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        // update player's stats
        return player.getName() + " Left the game";
    }

    // selectAnswer - pid, gid, answer-id
    // check if the answer is right or not,
    // update the and the game stats
    // to detect if the game has ended, and to end the game.
    // when the game ends, update every players stats
    @GetMapping("/selectAnswer/{pid}/{gc}/{ansid}")
    public String selectAnswer(@PathVariable(value = "pid") Long playerId,
                               @PathVariable(value = "gc") String gameCode,
                               @PathVariable(value = "ansid") int answerId) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        // update player's stats
        return "";
    }

    // getReady for next strp - pid, gid
    @GetMapping("/selectAnswer/{pid}/{gc}/{ansid}")
    public String getReady(@PathVariable(value = "pid") Long playerId,
                           @PathVariable(value = "gc") String gameCode) {
        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow();
        Player player = playerRepository.findById(playerId).orElseThrow();
        return "";
    }
}
