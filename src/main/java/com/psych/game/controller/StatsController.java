package com.psych.game.controller;

import com.psych.game.model.Stats;
import com.psych.game.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {

    @Autowired
    private StatsRepository statsRepository;

    @GetMapping("/stats")
    public List<Stats> getAllStats() {
        return statsRepository.findAll();
    }

    @PostMapping("/stats")
    public Stats createStats(@Valid @RequestBody Stats stats){
        return statsRepository.save(stats);
    }

    @GetMapping("/stats/{id}")
    public Stats getStatsById(@PathVariable(value = "id") Long id) throws Exception {
        return statsRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/stats/{id}")
    public Stats updateStats(@PathVariable(value = "id") Long id, @Valid @RequestBody Stats stats) throws Exception {
        Stats p = statsRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setGamePlayed(stats.getGamePlayed());
        p.setNumPsyched(stats.getNumPsyched());
        p.setNumPsychedBy(stats.getNumPsychedBy());
        return statsRepository.save(p);
    }

    @DeleteMapping("/stats/{id}")
    public ResponseEntity<?> deleteStats(@PathVariable(value = "id") Long id) throws Exception {
        Stats p = statsRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        statsRepository.delete(p);
        return ResponseEntity.ok().build();
    }
}
