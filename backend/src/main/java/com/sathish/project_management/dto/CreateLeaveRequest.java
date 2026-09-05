package com.sathish.project_management.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLeaveRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;
}