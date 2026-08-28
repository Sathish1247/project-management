package com.sathish.project_management.controller;

import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sathish.project_management.dto.RegisterResponse;
import com.sathish.project_management.dto.LoginRequest;
import com.sathish.project_management.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(EmployeeRepository employeeRepository,PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody Employee employee) {

        //System.out.println(employee.getPassword());
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Employee employee = employeeRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email, please sign up"));

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                employee.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(employee.getEmail());

        return ResponseEntity.ok(token);
    }
}