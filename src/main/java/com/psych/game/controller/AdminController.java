package com.psych.game.controller;

import com.psych.game.model.Admin;
import com.psych.game.repository.adminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private adminRepository adminRepository;

    @GetMapping("/admins")
    public List<Admin> getAlladmins() {
        return adminRepository.findAll();
    }

    @PostMapping("/admins")
    public Admin createAdmin(@Valid @RequestBody Admin admin){
        return adminRepository.save(admin);
    }

    @GetMapping("/admins/{id}")
    public Admin getAdminById(@PathVariable(value = "id") Long id) throws Exception {
        return adminRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/admins/{id}")
    public Admin updateAdmin(@PathVariable(value = "id") Long id, @Valid @RequestBody Admin admin) throws Exception {
        Admin p = adminRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setName(admin.getName());
        p.setEmail(admin.getEmail());
        return adminRepository.save(p);
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable(value = "id") Long id) throws Exception {
        Admin p = adminRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        adminRepository.delete(p);
        return ResponseEntity.ok().build();
    }

}
