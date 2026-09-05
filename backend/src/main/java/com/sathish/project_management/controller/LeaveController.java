package com.sathish.project_management.controller;

import com.sathish.project_management.dto.CreateLeaveRequest;
import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.entity.Leave;
import com.sathish.project_management.entity.LeaveStatus;
import com.sathish.project_management.repository.EmployeeRepository;
import com.sathish.project_management.repository.LeaveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveController(
            LeaveRepository leaveRepository,
            EmployeeRepository employeeRepository) {

        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Leave> updateLeaveStatus(
            @PathVariable Long id,
            @RequestParam LeaveStatus status) {

        Leave leave = leaveRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Leave not found"));

        leave.setStatus(status);

        Leave updatedLeave = leaveRepository.save(leave);

        return ResponseEntity.ok(updatedLeave);
    }

    @GetMapping
    public ResponseEntity<java.util.List<Leave>> getAllLeaves() {

        return ResponseEntity.ok(leaveRepository.findAll());
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<java.util.List<Leave>> getMyLeaves(
            Authentication authentication) {

        String email = authentication.getName();

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        java.util.List<Leave> leaves =
                leaveRepository.findByEmployee(employee);

        return ResponseEntity.ok(leaves);
    }

    @PostMapping
    public ResponseEntity<Leave> createLeave(
            @RequestBody CreateLeaveRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Leave leave = new Leave();

        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus(LeaveStatus.PENDING);
        leave.setEmployee(employee);

        Leave savedLeave = leaveRepository.save(leave);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLeave);
    }
}