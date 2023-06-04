package com.isetrip.taskmanager.web;

import com.isetrip.taskmanager.logic.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class TaskController {

    @Autowired
    private ITaskService service;

}
