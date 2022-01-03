package hexlet.code;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.controller.LabelController;
import hexlet.code.controller.UsersController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.assertj.core.api.Assertions.assertThat;

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
        labelToChangeJson = testUtils.readFileContent(TestUtils.FIXTURES_PATH + "labelToChange.json");
    }

    @Test
    void testGetAllLabels() throws Exception {
        User user = userRepository.findAll().get(0);
        MockHttpServletResponse resp = testUtils.perform(
                MockMvcRequestBuilders.get(TestUtils.BASE_API_URL + LabelController.LABEL_CONTROLLER_PATH),
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
                MockMvcRequestBuilders.get(TestUtils.BASE_API_URL + LabelController.LABEL_CONTROLLER_PATH + UsersController.ID, 1),
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
                MockMvcRequestBuilders.put(TestUtils.BASE_API_URL + LabelController.LABEL_CONTROLLER_PATH + UsersController.ID, 1)
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
                MockMvcRequestBuilders.delete(TestUtils.BASE_API_URL + LabelController.LABEL_CONTROLLER_PATH + UsersController.ID, 1),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(resp.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findAll().size()).isEqualTo(2);
    }
}
