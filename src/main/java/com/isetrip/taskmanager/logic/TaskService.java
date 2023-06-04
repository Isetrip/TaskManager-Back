package com.isetrip.taskmanager.logic;

import com.isetrip.taskmanager.data.ITaskRepository;
import com.isetrip.taskmanager.data.Task;
import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import com.isetrip.taskmanager.exception.BadRequestException;
import com.isetrip.taskmanager.exception.NotFoundException;
import com.isetrip.taskmanager.web.bodies.requests.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private ITaskRepository repository;

    @Override
    public List<Task> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Task get(Long id) throws NotFoundException {
        Task task = this.repository.findAll().stream()
                .filter(task1 -> task1.getId() == id)
                .findFirst()
                .orElse(null);
        if (task == null)
            throw new NotFoundException();
        return task;
    }

    @Override
    public Task create(TaskRequest request) throws BadRequestException {
        if (Objects.isNull(request.getName()) ||
                        Objects.isNull(request.getDescription()) ||
                                Objects.isNull(request.getPriority()) ||
                                        Objects.isNull(request.getStatus()))
            throw new BadRequestException();
        return this.repository.save(new Task(request));
    }

    @Override
    public Task update(Long id, TaskRequest body) throws NotFoundException {
        Task task = get(id);
        if (body.getName() != null)
            task.setName(body.getName());
        if (body.getDescription() != null)
            task.setDescription(body.getDescription());
        if (body.getStatus() != null)
            task.setStatus(body.getStatus());
        if (body.getPriority() != null)
            task.setPriority(body.getPriority());
        return task;
    }

    @Override
    public Task delete(Long id) throws NotFoundException {
        Task task = get(id);
        this.repository.delete(task);
        return task;
    }

    @Override
    public List<Task> getByStatus(TaskStatus status) {
        return this.repository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getByPriority(TaskPriority priority) {
        return this.repository.findAll().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getByDate(String date) throws BadRequestException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date1 = dateFormat.parse(date);
            return this.repository.findAll().stream()
                    .filter(task -> {
                        try {
                            return dateFormat.parse(task.getDate()).after(date1);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (ParseException | RuntimeException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public HttpStatus deleteAll() {
        this.repository.deleteAll();
        return HttpStatus.OK;
    }
}
