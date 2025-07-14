package com.assignment.assignment.Controller;

import com.assignment.assignment.Repository.TaskRepository;
import com.assignment.assignment.models.EStatus;
import com.assignment.assignment.models.Task;
import com.assignment.assignment.models.User;
import com.assignment.assignment.payload.Request.LoginRequest;
import com.assignment.assignment.payload.Request.TaskRequest;
import com.assignment.assignment.payload.Response.JwtResponse;
import com.assignment.assignment.payload.Response.MessageResponse;
import com.assignment.assignment.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;


    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.ok("Your ID: " + userId);
    }

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long userId = userDetails.getId();

        Task task = new Task(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getDueDate(), EStatus.OPEN, userId);

        taskRepository.save(task);

        return ResponseEntity.ok(new MessageResponse("Task created successfully!"));

    }

    @GetMapping("/task")
    public ResponseEntity<?> getTask() {

        Long userId = getUserId();

        List<Optional<Task>> tasks = taskRepository.findAllByUserId(userId);

        return ResponseEntity.ok(tasks);

    }

    @DeleteMapping("/task")
    public ResponseEntity<?> deleteTask( @RequestParam @NotNull Long taskId) {
        Long userId = getUserId();
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok("Task with ID " + taskId + " deleted.");
    }

    @PutMapping("/task")
    public  ResponseEntity<?> updateTask(@Valid @RequestBody TaskRequest taskRequest) {
        Long userId = getUserId();
        Long taskId = taskRequest.getId();
        Optional<Task> task = taskRepository.findById(taskId);

        if(task.isEmpty() || !Objects.equals(task.get().getUserId(), userId)) {
            return ResponseEntity.ok(new MessageResponse("Task doesn't exist!"));
        }

        if(taskRequest.getDescription() != null)  task.get().setDescription(taskRequest.getDescription());
        if(taskRequest.getTitle() != null) task.get().setTitle(taskRequest.getTitle());
        if(taskRequest.getDueDate() != null) task.get().setDueDate(taskRequest.getDueDate());

        taskRepository.flush();
        return ResponseEntity.ok(new MessageResponse("Task updated successfully!"));
    }

    private Long getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long userId = userDetails.getId();
        return userId;
    }


}
