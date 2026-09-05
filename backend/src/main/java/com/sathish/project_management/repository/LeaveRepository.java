package com.sathish.project_management.repository;

import com.sathish.project_management.entity.Employee;
import com.sathish.project_management.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployee(Employee employee);
}