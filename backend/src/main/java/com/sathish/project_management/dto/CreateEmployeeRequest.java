package com.sathish.project_management.dto;

import com.sathish.project_management.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequest {

    private String name;

    private String email;

    private String password;

    private Role role;
}