package hexlet.code.app;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
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
import java.io.IOException;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.controller.UsersController.ID;
import static hexlet.code.app.utils.TestUtils.BASE_API_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;
import static hexlet.code.app.utils.TestUtils.FIXTURES_PATH;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DBRider
@DataSet("test-data.yml")
@AutoConfigureMockMvc
public class LabelControllerTest {

    private static String labelToChangeJson;

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    @BeforeAll
    void init() throws IOException {
        labelToChangeJson = testUtils.readFileContent(FIXTURES_PATH + "labelToChange.json");
    }

    @Test
    void testGetAllLabels() throws Exception {
        User user = userRepository.findAll().get(0);
        MockHttpServletResponse resp = testUtils.perform(
                get(BASE_API_URL + LABEL_CONTROLLER_PATH),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(200);
    }

    @Test
    void testRegLabel() throws Exception {
        assertThat(labelRepository.findAll().size()).isEqualTo(3);
        testUtils.regDefaultLabel();
        assertThat(labelRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void testGetLabelById() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                get(BASE_API_URL + LABEL_CONTROLLER_PATH + ID, 1),
                user.getEmail()
        ).andReturn().getResponse();

        String body = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(body).contains("To Do");
    }

    @Test
    void testChangeLabel() throws Exception {
        User user = userRepository.findAll().get(0);

        MockHttpServletResponse resp = testUtils.perform(
                put(BASE_API_URL + LABEL_CONTROLLER_PATH + ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(labelToChangeJson),
                user.getEmail()
        ).andReturn().getResponse();

        String body = resp.getContentAsString();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(body).contains("Changed Name");
    }

    @Test
    void testDeleteLabel() throws Exception {
        User user = userRepository.findAll().get(0);
        assertThat(labelRepository.findAll().size()).isEqualTo(3);

        MockHttpServletResponse resp = testUtils.perform(
                delete(BASE_API_URL + LABEL_CONTROLLER_PATH + ID, 1),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findAll().size()).isEqualTo(2);
    }
}
