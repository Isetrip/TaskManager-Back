package com.isetrip.taskmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void testGetAllTasks() throws Exception {
        MvcResult result = mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
    }

    @Test
    void testDeleteAllTasks() throws Exception {
        MvcResult result = mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
        mockMvc.perform(delete("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 0;
        });
    }

    @Test
    void testGetTasksByStatus() throws Exception {
        mockMvc.perform(delete("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.IN_PROGRESS, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task/status/" + TestStatus.IN_PROGRESS)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 1;
        });
    }

    @Test
    void testGetTasksByPriority() throws Exception {
        mockMvc.perform(delete("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.HIGH, status().isCreated());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.IN_PROGRESS, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task/priority/" + TestPriority.HIGH)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 1;
        });
    }

    @Test
    void testGetTasksByDate() throws Exception {
        mockMvc.perform(delete("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.HIGH, status().isCreated());
        TestTaskResponse task = createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.IN_PROGRESS, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task/date/03.06.2023")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2;
        });
        mockMvc.perform(get("/task/date/" + task.getDate())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 0;
        });
    }

    @Test
    void testGetTaskById() throws Exception {
        TestTaskResponse task = createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(get("/task/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestTaskResponse taskToControl = stringToObject(mvcResult, TestTaskResponse.class);
                    assert Objects.equals(taskToControl.getId(), task.getId());
                });
    }

    @Test
    void testUpdateTaskById() throws Exception {
        TestTaskResponse task = createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        TestTaskRequest update = new TestTaskRequest();
        update.setName("qqewq");
        update.setStatus(TestStatus.COMPLETED);
        mockMvc.perform(put("/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestTaskResponse response = stringToObject(mvcResult, TestTaskResponse.class);
                    assert Objects.equals(response.getName(), update.getName());
                    assert Objects.equals(response.getDescription(), task.getDescription());
                    assert Objects.equals(response.getStatus(), update.getStatus());
                    assert Objects.equals(response.getPriority(), task.getPriority());
                });
    }

    @Test
    void testDeleteTaskById() throws Exception {
        MvcResult result = mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestTaskResponse task = createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        createNewTask("Make Units", "Make Unit tests for TaskManager", TestStatus.NEW, TestPriority.MEDIUM, status().isCreated());
        mockMvc.perform(delete("/task/" + task.getId())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(delete("/task/" + Long.MAX_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
        mockMvc.perform(get("/task")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 1 + stringToObject(result, ArrayList.class).size();
        });
    }

    TestTaskResponse createNewTask(String name, String description, TestStatus status, TestPriority priority, ResultMatcher statusMatcher) throws Exception {
        TestTaskRequest task = new TestTaskRequest();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        MvcResult mvcResult = mockMvc.perform(post("/task")
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

    @Data
    static class TestTaskRequest {
        private String name;
        private String description;
        private TestStatus status;
        private TestPriority priority;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    static class TestTaskResponse extends TestTaskRequest {
        private long id;
        private String date;
    }

    static enum TestStatus {
        NEW, IN_PROGRESS, COMPLETED;
    }

    static enum TestPriority {
        LOW, MEDIUM, HIGH;
    }

}
