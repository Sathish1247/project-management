package com.sathish.project_management.repository;

import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedEmployee(Employee employee);
}