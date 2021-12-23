package hexlet.code.app;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@DBRider
@DataSet("users.yml")
@AutoConfigureMockMvc
class UsersControllerTest {
    private static final String BASE_API_URL = "/api";
    private static final String USERS = "/users";
    private static final String FIXTURES_PATH = "src/test/resources/fixtures/";
    private static String userToCreateJson;
    private static String userToPatchJson;
    private static String userToCreateWithIncorrectCredentialsJson;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init() throws IOException {
        userToCreateJson = readFileContent(FIXTURES_PATH + "userToCreate.json");
        userToPatchJson = readFileContent(FIXTURES_PATH + "userToPatch.json");
        userToCreateWithIncorrectCredentialsJson = readFileContent(FIXTURES_PATH
                + "userToCreateIncorrectCredentials.json");
    }

    @Test
    void testCreateUserWithCorrectCredentials() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                            post(BASE_API_URL + USERS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToCreateJson)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("ivan@google.com");
        assertThat(response).contains("Ivan");
        assertThat(response).contains("Petrov");
        assertThat(userRepository.findByEmail("ivan@google.com")).isNotNull();
    }

    @Test
    void testCreateUserWithIncorrectCredentials() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                                post(BASE_API_URL + USERS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userToCreateWithIncorrectCredentialsJson)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(422);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Email is not valid.");
        assertThat(response).contains("First name must contains at least one character");
        assertThat(response).contains("Last name must contains at least one character");
        assertThat(response).contains("Password must contains at least 3 characters.");
        assertThat(userRepository.findByEmail("ivam")).isEmpty();
    }

    @Test
    void testGetUserById() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                    get(BASE_API_URL + USERS + "/1")
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(response).contains("Alex");
        assertThat(response).contains("Testov");
        assertThat(response).doesNotContain("password");
    }

    @Test
    void testGetUserByNonExistentId() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                        get(BASE_API_URL + USERS + "/20")
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(404);
        assertThat(response).contains("User with such ID not found");
    }

    @Test
    void testGetAllUsers() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                    get(BASE_API_URL + USERS)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Alex");
    }

    @Test
    void testChangeUserData() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                        patch(BASE_API_URL + USERS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToPatchJson)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        User updatedUser = userRepository.getById(1L);

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Petr");
        assertThat(response).contains("Sidorov");
        assertThat(response).contains("petr@yahoo.com");
        assertThat(updatedUser.getFirstName()).isEqualTo("Petr");
        assertThat(updatedUser.getLastName()).isEqualTo("Sidorov");
        assertThat(updatedUser.getEmail()).isEqualTo("petr@yahoo.com");
    }

    @Test
    void testChangeUserDataWithInvalidCredentials() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                            patch(BASE_API_URL + USERS + "/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToCreateWithIncorrectCredentialsJson)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(422);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Email is not valid.");
        assertThat(response).contains("First name must contains at least one character");
        assertThat(response).contains("Last name must contains at least one character");
        assertThat(response).contains("Password must contains at least 3 characters.");
        assertThat(userRepository.findByEmail("ivam")).isEmpty();
    }

    @Test
    void testDeleteUser() throws Exception {
        MockHttpServletResponse resp = mockMvc
                .perform(
                        delete(BASE_API_URL + USERS + "/1")
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(response).contains("OK");
        assertThat(userRepository.findById(1L)).isEmpty();
    }


    private static String readFileContent(String path) throws IOException {
        Path resultPath = Paths.get(path).toAbsolutePath().normalize();
        return Files.readString(resultPath);
    }
}
