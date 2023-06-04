package com.isetrip.taskmanager.logic;

import com.isetrip.taskmanager.data.Task;
import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import com.isetrip.taskmanager.exception.BadRequestException;
import com.isetrip.taskmanager.exception.NotFoundException;
import com.isetrip.taskmanager.web.bodies.requests.TaskRequest;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ITaskService {

    public List<Task> getAll();

    public Task get(Long id) throws NotFoundException;

    public Task create(TaskRequest request) throws BadRequestException;

    public Task update(Long id, TaskRequest body) throws NotFoundException;

    public Task delete(Long id) throws NotFoundException;

    public List<Task> getByStatus(TaskStatus status);

    public List<Task> getByPriority(TaskPriority priority);

    public List<Task> getByDate(String date) throws BadRequestException;

    public HttpStatus deleteAll();
}
