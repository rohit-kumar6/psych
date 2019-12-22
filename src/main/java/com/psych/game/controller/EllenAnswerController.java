package com.psych.game.controller;

import com.psych.game.model.EllenAnswer;
import com.psych.game.repository.EllenAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EllenAnswerController {
    @Autowired
    private EllenAnswerRepository ellenAnswerRepository;

    @GetMapping("/ellenAnswers")
    public List<EllenAnswer> getAllellenAnswer() {
        return ellenAnswerRepository.findAll();
    }

    @PostMapping("/ellenAnswers")
    public EllenAnswer createEllenAnswer(@Valid @RequestBody EllenAnswer ellenAnswer){
        return ellenAnswerRepository.save(ellenAnswer);
    }

    @GetMapping("/ellenAnswers/{id}")
    public EllenAnswer getEllenAnswerById(@PathVariable(value = "id") Long id) throws Exception {
        return ellenAnswerRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/ellenAnswers/{id}")
    public EllenAnswer updateEllenAnswer(@PathVariable(value = "id") Long id, @Valid @RequestBody EllenAnswer ellenAnswer) throws Exception {
        EllenAnswer p = ellenAnswerRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setAnswer(ellenAnswer.getAnswer());
        p.setVoteCount(ellenAnswer.getVoteCount());
        return ellenAnswerRepository.save(p);
    }

    @DeleteMapping("/ellenAnswers/{id}")
    public ResponseEntity<?> deleteEllenAnswer(@PathVariable(value = "id") Long id) throws Exception {
        EllenAnswer p = ellenAnswerRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        ellenAnswerRepository.delete(p);
        return ResponseEntity.ok().build();
    }

}
