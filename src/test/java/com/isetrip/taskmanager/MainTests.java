package com.isetrip.taskmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MainTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateNewTask() throws Exception {
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
    }

    TestTaskResponse createNewTask(String name, String description, TestStatus status, TestPriority priority, ResultMatcher statusMatcher) throws Exception {
        TestTaskRequest task = TestTaskRequest.builder()
                .name(name)
                .description(description)
                .status(status)
                .priority(priority)
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(task))
                ).andExpect(statusMatcher)
                .andDo(mvcResult1 -> {
                    TestTaskResponse taskToControl = stringToObject(mvcResult1, TestTaskResponse.class);
                    assert Objects.equals(task.getName(), taskToControl.getName());
                    assert Objects.equals(task.getDescription(), taskToControl.getDescription());
                    assert Objects.equals(task.getStatus(), taskToControl.getStatus());
                    assert Objects.equals(task.getPriority(), taskToControl.getPriority());
                })
                .andReturn();
        return stringToObject(mvcResult, TestTaskResponse.class);
    }

    static String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <K> K stringToObject(MvcResult object, Class<K> objectClass) throws UnsupportedEncodingException, JsonProcessingException {
        return new ObjectMapper().readValue(object.getResponse().getContentAsString(), objectClass);
    }

    @Builder
    @Data
    static class TestTaskRequest {
        private String name;
        private String description;
        private TestStatus status;
        private TestPriority priority;
    }

    @Builder
    @Data
    static class TestTaskResponse {
        private long id;
        private String name;
        private String description;
        private TestStatus status;
        private TestPriority priority;
        private String date;
    }

    @Builder
    @Data
    static class TestTaskListResponse {
        private List<TestTaskResponse> taskList;
    }

    static enum TestStatus {
        NEW, IN_PROGRESS, COMPLETED;
    }

    static enum TestPriority {
        LOW, MEDIUM, HIGH;
    }

}
