package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.controller.UsersController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DBRider
@DataSet("test-data.yml")
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    private static String taskStatusToChangeJson;
    private static String taskStatusToCreateInvalidJson;

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeAll
    void init() throws IOException {
        taskStatusToChangeJson = testUtils
                .readFileContent(TestUtils.FIXTURES_PATH + "taskStatusToChange.json");
        taskStatusToCreateInvalidJson = testUtils
                .readFileContent(TestUtils.FIXTURES_PATH + "taskStatusInvalidCreate.json");
    }

    @Test
    void testCreateTaskStatus() throws Exception {
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(3);
        testUtils.regDefaultTaskStatus();
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void testGetTaskStatusById() throws Exception {
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.get(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + UsersController.ID, 1)
        ).andReturn().getResponse();

        String body = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(body).contains("name");
        assertThat(body).contains("id");
        assertThat(body).contains("createdAt");
        assertThat(body).contains("New");
    }

    @Test
    void testGetAllTasks() throws Exception {
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.get(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(200);
    }

    @Test
    void testChangeTaskStatus() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.put(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + UsersController.ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskStatusToChangeJson),
                user.getEmail()
        ).andReturn().getResponse();

        String body = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(body).contains("Changed");
    }

    @Test
    void testDeleteTaskStatus() throws Exception {
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(3);

        User user = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.delete(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + UsersController.ID, 3),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void testCreateTaskWithInvalidName() throws Exception {
        User user = userRepository.findAll().get(0);
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.post(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskStatusToCreateInvalidJson),
                user.getEmail()
        ).andReturn().getResponse();

        String body = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(422);
        assertThat(body).contains("Name must contains at least 1 character");
    }

    @Test
    void testCreateTaskStatusWithoutAuthorization() throws Exception {
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.post(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskStatusToChangeJson)
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(401);
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void testChangeTaskStatusWithoutAuthorization() throws Exception {
        testUtils.regDefaultTaskStatus();

        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.put(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + UsersController.ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskStatusToChangeJson)
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(401);
        Assertions.assertThat(taskStatusRepository.findAll().get(0).getName()).isEqualTo("New");
    }

    @Test
    void testDeleteTaskStatusWithoutAuthorization() throws Exception {
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.delete(TestUtils.BASE_API_URL + TaskStatusController.TASK_STATUS_CONTROLLER_PATH + UsersController.ID, 1)
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(401);
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(3);
    }
}
