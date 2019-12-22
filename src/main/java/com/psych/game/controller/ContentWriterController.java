package com.psych.game.controller;

import com.psych.game.model.ContentWriter;
import com.psych.game.repository.ContentWriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContentWriterController {
    @Autowired
    private ContentWriterRepository contentWriterRepository;

    @GetMapping("/contentWriters")
    public List<ContentWriter> getAllcontentWriter() {
        return contentWriterRepository.findAll();
    }

    @PostMapping("/contentWriters")
    public ContentWriter createContentWriter(@Valid @RequestBody ContentWriter contentWriter){
        return contentWriterRepository.save(contentWriter);
    }

    @GetMapping("/contentWriters/{id}")
    public ContentWriter getContentWriterById(@PathVariable(value = "id") Long id) throws Exception {
        return contentWriterRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/contentWriters/{id}")
    public ContentWriter updateContentWriter(@PathVariable(value = "id") Long id, @Valid @RequestBody ContentWriter contentWriter) throws Exception {
        ContentWriter p = contentWriterRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setName(contentWriter.getName());
        p.setEmail(contentWriter.getEmail());
        return contentWriterRepository.save(p);
    }

    @DeleteMapping("/contentWriters/{id}")
    public ResponseEntity<?> deleteContentWriter(@PathVariable(value = "id") Long id) throws Exception {
        ContentWriter p = contentWriterRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        contentWriterRepository.delete(p);
        return ResponseEntity.ok().build();
    }
}
