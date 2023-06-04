package com.isetrip.taskmanager.web;

import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import com.isetrip.taskmanager.exception.BadRequestException;
import com.isetrip.taskmanager.exception.NotFoundException;
import com.isetrip.taskmanager.logic.ITaskService;
import com.isetrip.taskmanager.web.bodies.requests.TaskRequest;
import com.isetrip.taskmanager.web.bodies.responses.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private ITaskService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskResponse> getAllTasks() {
        return this.service.getAll().stream().map(TaskResponse::new).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest body) throws BadRequestException {
        return new ResponseEntity<>(new TaskResponse(this.service.create(body)), HttpStatus.CREATED);
    }

    @DeleteMapping()
    public HttpStatus deleteAllTasks()  {
        return this.service.deleteAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskResponse getTask(@PathVariable("id") Long id) throws NotFoundException {
        return new TaskResponse(this.service.get(id));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TaskResponse updateTask(@PathVariable("id") Long id, @RequestBody TaskRequest body) throws NotFoundException {
        return new TaskResponse(this.service.update(id, body));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskResponse deleteTask(@PathVariable("id") Long id) throws NotFoundException {
        return new TaskResponse(this.service.delete(id));
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskResponse> getTasksByStatus(@PathVariable("status")  TaskStatus status) {
        return this.service.getByStatus(status).stream().map(TaskResponse::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/priority/{priority}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskResponse> getTasksByPriority(@PathVariable("priority")  TaskPriority priority) {
        return this.service.getByPriority(priority).stream().map(TaskResponse::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskResponse> getTasksByDate(@PathVariable("date")  String date) throws BadRequestException {
        return this.service.getByDate(date).stream().map(TaskResponse::new).collect(Collectors.toList());
    }


}
