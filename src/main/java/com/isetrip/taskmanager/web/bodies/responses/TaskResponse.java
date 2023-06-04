package com.isetrip.taskmanager.web.bodies.responses;

import com.isetrip.taskmanager.data.Task;
import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskResponse {

    private long id;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String date;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.date = task.getDate();
    }

}
