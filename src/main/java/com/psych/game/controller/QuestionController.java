package com.psych.game.controller;

import com.psych.game.model.Question;
import com.psych.game.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question){
        return questionRepository.save(question);
    }

    @GetMapping("/questions/{id}")
    public Question getQuestionById(@PathVariable(value = "id") Long id) throws Exception {
        return questionRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/questions/{id}")
    public Question updateQuestion(@PathVariable(value = "id") Long id, @Valid @RequestBody Question question) throws Exception {
        Question p = questionRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setQuestionText(question.getQuestionText());
        p.setCorrectAnswer(question.getCorrectAnswer());
        return questionRepository.save(p);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(value = "id") Long id) throws Exception {
        Question p = questionRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        questionRepository.delete(p);
        return ResponseEntity.ok().build();
    }
}
