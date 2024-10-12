package com.todos.todos101.controllers;

import com.todos.todos101.dto.TaskDto;
import com.todos.todos101.models.Task;
import com.todos.todos101.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    @GetMapping("/")
    public ResponseEntity<?> getAllTask(Authentication authentication, @RequestParam(value = "name", defaultValue = "") String name) {
        String usernameOrEmail = authentication.getName();
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTask(usernameOrEmail));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.findTaskName(name, usernameOrEmail));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskDetail(@PathVariable(value = "id") Long id, Authentication authentication) {
        String usernameOrEmail = authentication.getName();
        Optional<Task> task = taskService.findTaskById(id, usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedTasks(Authentication authentication) {
        String usernameOrEmail = authentication.getName();
        List<Task> tasks = taskService.findAllCompletedTasks(usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/uncompleted")
    public ResponseEntity<?> getUncompletedTasks(Authentication authentication) {
        String usernameOrEmail = authentication.getName();
        List<Task> tasks = taskService.findAllUnCompletedTasks(usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(Authentication authentication ,@PathVariable Long id, @RequestBody Task task){
        String usernameOrEmail = authentication.getName();
        Optional<Task> taskUpdated = taskService.updateTask(id, task, usernameOrEmail);

        if (taskUpdated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }

    @PostMapping("/")
    public ResponseEntity<?> createTask(Authentication authentication, @Valid @RequestBody Task task) {
        TaskDto newTask = taskService.createTask(task, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication){
        String usernameOrEmail = authentication.getName();
        boolean isDelete = taskService.deleteTask(id, usernameOrEmail);

        if (isDelete) {
            return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }

}
