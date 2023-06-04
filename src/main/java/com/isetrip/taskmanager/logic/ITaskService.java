package com.isetrip.taskmanager.logic;

import com.isetrip.taskmanager.data.Task;
import com.isetrip.taskmanager.exception.NotFoundException;

import java.util.List;

public interface ITaskService {

    public List<Task> getAll();

    public Task get(Long id) throws NotFoundException;

}
