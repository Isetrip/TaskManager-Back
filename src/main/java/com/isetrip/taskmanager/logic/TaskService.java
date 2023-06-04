package com.isetrip.taskmanager.logic;

import com.isetrip.taskmanager.data.ITaskRepository;
import com.isetrip.taskmanager.data.Task;
import com.isetrip.taskmanager.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Task task = this.repository.findTaskById(id);
        if (task == null)
            throw new NotFoundException();
        return null;
    }
}
