package com.sathish.project_management.dto;

import com.sathish.project_management.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private Long projectId;

    private String projectName;

    private Long assignedEmployeeId;

    private String assignedEmployeeName;
}