package com.sathish.project_management.controller;

import com.sathish.project_management.dto.RegisterResponse;
import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sathish.project_management.dto.CreateEmployeeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sathish.project_management.dto.UpdateEmployeeRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeRepository employeeRepository,PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<RegisterResponse>> getAllEmployees() {

        List<RegisterResponse> employees =
                employeeRepository.findAll()
                        .stream()
                        .map(employee -> new RegisterResponse(
                                employee.getId(),
                                employee.getName(),
                                employee.getEmail(),
                                employee.getRole()
                        ))
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<RegisterResponse> createEmployee(
            @RequestBody CreateEmployeeRequest request) {

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        employee.setRole(request.getRole());

        Employee savedEmployee = employeeRepository.save(employee);

        RegisterResponse response = new RegisterResponse(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getRole()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RegisterResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());

        Employee updatedEmployee = employeeRepository.save(employee);

        RegisterResponse response = new RegisterResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getRole()
        );

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employeeRepository.delete(employee);

        return ResponseEntity.ok("Employee deleted successfully");
    }
}