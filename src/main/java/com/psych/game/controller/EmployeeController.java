package com.psych.game.controller;

import com.psych.game.model.Employee;
import com.psych.game.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable(value = "id") Long id) throws Exception {
        return employeeRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable(value = "id") Long id, @Valid @RequestBody Employee employee) throws Exception {
        Employee p = employeeRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        p.setName(employee.getName());
        p.setEmail(employee.getEmail());
        return employeeRepository.save(p);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long id) throws Exception {
        Employee p = employeeRepository.findById(id).orElseThrow(() -> new Exception("something went wrong"));
        employeeRepository.delete(p);
        return ResponseEntity.ok().build();
    }

}
