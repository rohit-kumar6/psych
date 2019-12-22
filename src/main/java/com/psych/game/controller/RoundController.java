package com.psych.game.controller;

import com.psych.game.model.Round;
import com.psych.game.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoundController {
    @Autowired
    private RoundRepository roundRepository;

    @GetMapping("/rounds")
    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }

    @PostMapping("/rounds")
    public Round createRound(@Valid @RequestBody Round round){
        return roundRepository.save(round);
    }

    @GetMapping("/rounds/{id}")
    public Round getRoundById(@PathVariable(value = "id") Long id) throws Exception {
        return roundRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/rounds/{id}")
    public Round updateRound(@PathVariable(value = "id") Long id, @Valid @RequestBody Round round) throws Exception {
        Round p = roundRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setRoundNumber(round.getRoundNumber());
        return roundRepository.save(p);
    }

    @DeleteMapping("/rounds/{id}")
    public ResponseEntity<?> deleteRound(@PathVariable(value = "id") Long id) throws Exception {
        Round p = roundRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        roundRepository.delete(p);
        return ResponseEntity.ok().build();
    }

}
