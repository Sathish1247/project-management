package com.sathish.project_management.dto;

import com.sathish.project_management.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequest {

    private TaskStatus status;
}