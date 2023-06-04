package com.isetrip.taskmanager.data;

import com.isetrip.taskmanager.data.enums.TaskPriority;
import com.isetrip.taskmanager.data.enums.TaskStatus;
import com.isetrip.taskmanager.web.bodies.requests.TaskRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String date;

    public Task(TaskRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.status = request.getStatus();
        this.priority = request.getPriority();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.date = dateFormat.format(new Date());
    }
}
