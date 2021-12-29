package hexlet.code.app;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static hexlet.code.app.controller.UsersController.USERS_CONTROLLER_PATH;
import static hexlet.code.app.controller.UsersController.ID;
import static hexlet.code.app.utils.TestUtils.BASE_API_URL;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@DBRider
@DataSet("test-data.yml")
@AutoConfigureMockMvc
class UsersControllerTest {
    private static final String FIXTURES_PATH = "src/test/resources/fixtures/";
    private static String userToCreateJson;
    private static String userToPatchJson;
    private static String userToCreateWithIncorrectCredentialsJson;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    void init() throws Exception {
        userToCreateJson = testUtils.readFileContent(FIXTURES_PATH + "userToCreate.json");
        userToPatchJson = testUtils.readFileContent(FIXTURES_PATH + "userToPatch.json");
        userToCreateWithIncorrectCredentialsJson = testUtils.readFileContent(FIXTURES_PATH
                + "userToCreateIncorrectCredentials.json");
    }

    @Test
    void testCreateUserWithCorrectCredentials() throws Exception {  // todo refactor
        MockHttpServletResponse resp = testUtils
                .perform(
                            post(BASE_API_URL + USERS_CONTROLLER_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToCreateJson)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("ivan@google.com");
        assertThat(response).contains("Ivan");
        assertThat(response).contains("Petrov");
        assertThat(userRepository.findUserByEmail("ivan@google.com")).isNotNull();
    }

    @Test
    void testCreateUserWithIncorrectCredentials() throws Exception {
        MockHttpServletResponse resp = testUtils
                .perform(
                                post(BASE_API_URL + USERS_CONTROLLER_PATH)
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
        assertThat(userRepository.findUserByEmail("ivam")).isEmpty();
    }

    @Test
    void testGetUserById() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                get(BASE_API_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId()),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(response).contains(expectedUser.getFirstName());
        assertThat(response).contains(expectedUser.getLastName());
        assertThat(response).doesNotContain("password");
    }

    @Test
    void testGetUserByIdWithoutAuthentication() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                get(BASE_API_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId())
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(401);
    }

    @Test
    void testGetUserByNonExistentId() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils
                .perform(
                        get(BASE_API_URL + USERS_CONTROLLER_PATH + ID, "568"),
                        expectedUser.getEmail()
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(404);
        assertThat(response).contains("User with such ID not found");
    }

    @Test
    void testGetAllUsers() throws Exception {
        MockHttpServletResponse resp = testUtils
                .perform(
                    get(BASE_API_URL + USERS_CONTROLLER_PATH)
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Alex");
    }

    @Test
    void testChangeUserData() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils
                .perform(
                        put(BASE_API_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToPatchJson),
                        expectedUser.getEmail()
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        User updatedUser = userRepository.getById(expectedUser.getId());

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
    void testChangeAnotherUserData() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final Long existentUserInDbId = 30L;

        MockHttpServletResponse resp = testUtils.perform(
                put(BASE_API_URL + USERS_CONTROLLER_PATH + ID, existentUserInDbId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToPatchJson),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        final User dbUser = userRepository.getById(existentUserInDbId);

        assertThat(resp.getStatus()).isEqualTo(401);
        assertThat(dbUser.getFirstName()).isEqualTo("fname_to_change");
        assertThat(dbUser.getLastName()).isEqualTo("lname_to_change");
        assertThat(dbUser.getEmail()).isEqualTo("change@mail.com");
    }

    @Test
    void testChangeUserDataWithInvalidCredentials() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils
                .perform(
                            put(BASE_API_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToCreateWithIncorrectCredentialsJson),
                        expectedUser.getEmail()
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(422);
        assertThat(resp.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response).contains("Email is not valid.");
        assertThat(response).contains("First name must contains at least one character");
        assertThat(response).contains("Last name must contains at least one character");
        assertThat(response).contains("Password must contains at least 3 characters.");
        assertThat(userRepository.findUserByEmail("ivam")).isEmpty();
    }

    @Test
    void testDeleteUser() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils
                .perform(
                        delete(BASE_API_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId()),
                        expectedUser.getEmail()
                ).andReturn().getResponse();

        String response = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(response).contains("OK");
        assertThat(userRepository.findById(expectedUser.getId())).isEmpty();
    }

    @Test
    void testDeleteAnotherUser() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final Long existentUserInDbId = 30L;

        MockHttpServletResponse resp = testUtils.perform(
                delete(BASE_API_URL + USERS_CONTROLLER_PATH + ID, existentUserInDbId),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(401);
        assertThat(userRepository.findById(existentUserInDbId)).isNotEmpty();
    }

}
