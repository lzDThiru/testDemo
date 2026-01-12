package com.task.demo.controller.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private boolean completed;
}
