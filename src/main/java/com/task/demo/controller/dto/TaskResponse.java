package com.task.demo.controller.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;

    public TaskResponse(Long id, String title, String description, boolean completed, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }
}
