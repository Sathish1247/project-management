package com.sathish.project_management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

    private String title;

    private String description;

    private Long projectId;

    private Long employeeId;
}