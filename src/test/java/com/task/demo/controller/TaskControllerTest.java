package com.task.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.demo.entity.Task;
import com.task.demo.entity.User;
import com.task.demo.repository.TaskRepository;
import com.task.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(com.task.demo.config.TestSecurityConfig.class) // Import the test security configuration
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Clear the database
        taskRepository.deleteAll();
        userRepository.deleteAll();

        // Create two users
        User userone = new User();
        userone.setUsername("user1");
        userone.setPassword("password1");
        user1 = userRepository.save(userone);


        User userTwo = new User();
        userTwo.setUsername("user2");
        userTwo.setPassword("password1");

        user2 = userRepository.save(userTwo);

        // Create tasks for user1
        taskRepository.save(new Task(null, "Task 1", "Description 1", LocalDate.now().plusDays(5), false, user1));
        taskRepository.save(new Task(null, "Task 2", "Description 2", LocalDate.now().plusDays(5), false, user1));

        // Create tasks for user2
        taskRepository.save(new Task(null, "Task 3", "Description 3",  LocalDate.now().plusDays(5), false, user2));
    }

    @Test
    void shouldReturnTasksForAuthenticatedUser() throws Exception {
        // Simulate a request as user1
        mockMvc.perform(get("/api/tasks")
                        .requestAttr("user", user1) // Inject authenticated user
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Only 2 tasks for user1
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    void shouldNotReturnTasksForOtherUsers() throws Exception {
        // Simulate a request as user2
        mockMvc.perform(get("/api/tasks")
                        .requestAttr("user", user2) // Inject authenticated user
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Only 1 task for user2
                .andExpect(jsonPath("$[0].title", is("Task 3")));
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksForUser() throws Exception {
        // Create a new user with no tasks

        User user = new User();
        user.setUsername("user3");
        user.setPassword("password3");

        User userWithoutTasks = userRepository.save(user);

        mockMvc.perform(get("/api/tasks")
                        .requestAttr("user", userWithoutTasks) // Inject user with no tasks
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Expect an empty list
    }

    @Test
    @Disabled // TODO should return 403 forbidden for unauthenticated user
    void shouldReturnForbiddenForUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    @Test
    void shouldCreateTaskForAuthenticatedUser() throws Exception {
        String newTaskJson = objectMapper.writeValueAsString(
                new Task(null, "New Task", "New Description", LocalDate.now().plusDays(3), false, null)
        );

        mockMvc.perform(post("/api/tasks")
                        .requestAttr("user", user1) // Inject authenticated user
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.completed", is(false)));

        // Verify the task is saved for user1
        List<Task> tasks = taskRepository.findAllByUser(user1);
        assertThat(tasks, hasSize(3)); // User1 now has 3 tasks
    }

    @Test
    @Disabled
        //TODO need extra validation to make sure the context user is not different
    // forExample consider adding the user id at the end of /{userId}/tasks
    //then cross check the userId with the context user id
    void shouldDeleteTaskForAuthenticatedUser() throws Exception {
        // Delete the first task for user1
        Task taskToDelete = taskRepository.findAllByUser(user1).get(0);

        mockMvc.perform(delete("/api/tasks/" + taskToDelete.getId())
                        .requestAttr("user", user1) // Inject authenticated user
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify the task is deleted
        List<Task> tasks = taskRepository.findAllByUser(user1);
        assertThat(tasks, hasSize(1)); // User1 now has 1 task
    }

    @Test
    @Disabled //TODO failing intermittently - should return 403 forbidden when accessing other users' tasks
    void shouldNotAllowAccessToOtherUsersTasks() throws Exception {
        // Get the first task for user2
        Task taskForUser2 = taskRepository.findAllByUser(user2).get(0);

        mockMvc.perform(get("/api/tasks/" + taskForUser2.getId())
                        .requestAttr("user", user1) // Inject user1 (not the owner)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

}