package com.isetrip.taskmanager.web.bodies.requests;

import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskRequest {

    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

}
