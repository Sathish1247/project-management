package com.sathish.project_management.controller;

import com.sathish.project_management.dto.CreateTaskRequest;
import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.entity.Project;
import com.sathish.project_management.entity.Task;
import com.sathish.project_management.entity.TaskStatus;
import com.sathish.project_management.repository.EmployeeRepository;
import com.sathish.project_management.repository.ProjectRepository;
import com.sathish.project_management.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
import com.sathish.project_management.dto.TaskResponse;
import com.sathish.project_management.dto.UpdateTaskStatusRequest;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public TaskController(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody CreateTaskRequest request) {

        Project project = projectRepository
                .findById(request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignedEmployee(employee);

        Task savedTask = taskRepository.save(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTask);
    }
    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            Authentication authentication) {

        String email = authentication.getName();

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        List<TaskResponse> tasks = taskRepository
                .findByAssignedEmployee(employee)
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getProject().getId(),
                        task.getProject().getName(),
                        task.getAssignedEmployee().getId(),
                        task.getAssignedEmployee().getName()
                ))
                .toList();

        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        // Security check: employee can update only their own task
        if (!task.getAssignedEmployee().getId()
                .equals(employee.getId())) {

            return ResponseEntity.status(403).build();
        }

        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);

        TaskResponse response = new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getProject().getId(),
                updatedTask.getProject().getName(),
                updatedTask.getAssignedEmployee().getId(),
                updatedTask.getAssignedEmployee().getName()
        );

        return ResponseEntity.ok(response);
    }
}