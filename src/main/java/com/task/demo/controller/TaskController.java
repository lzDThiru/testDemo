package com.task.demo.controller;

import com.task.demo.controller.dto.TaskResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.task.demo.entity.Task;
import com.task.demo.entity.User;
import com.task.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO  add the userId in the path so we can make sure we are getting tasks for the right user by cross checking with the JWT token
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(taskService.getTasksForUser(user));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task,
                                           @RequestAttribute("user") User user) {
        task.setUser(user);
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task task,
                                           @RequestAttribute("user") User user) {
        task.setId(id);
        task.setUser(user);
        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
