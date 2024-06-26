package hexlet.code.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;

import hexlet.code.controller.api.util.TestUtils;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.UserUtils;


@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestUtils testUtils;

    private JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject(UserUtils.ADMIN_EMAIL));
        testLabel = Instancio.of(testUtils.getLabelModel())
                .create();
        labelRepository.save(testLabel);
    }

    @AfterEach
    public void clean() {
        testUtils.clean();
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/labels")
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        var labels = om.readValue(body, new TypeReference<List<Label>>() { });
        var expected = labelRepository.findAll();

        assertThat(labels.containsAll(expected));
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/labels/" + testLabel.getId())
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isNotNull().and(
                json -> json.node("id").isEqualTo(testLabel.getId()),
                json -> json.node("name").isEqualTo(testLabel.getName()),
                json -> json.node("createdAt").isEqualTo(testLabel.getCreatedAt().format(testUtils.FORMATTER))
        );

        var receivedLabel = om.readValue(body, Label.class);
        assertThat(receivedLabel).isEqualTo(testLabel);
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(testUtils.getLabelModel())
                .create();

        var labelsCount = labelRepository.count();

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        assertThat(labelRepository.count()).isEqualTo(labelsCount + 1);

        var addedLabel = labelRepository.findByName(data.getName()).get();

        assertNotNull(addedLabel);
        assertThat(labelRepository.findByName(testLabel.getName())).isPresent();

        assertThat(addedLabel.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var oldName = testLabel.getName();
        var newName = faker.lorem().word();

        var data = new HashMap<>();
        data.put("name", newName);

        var labelsCount = labelRepository.count();

        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(labelRepository.count()).isEqualTo(labelsCount);

        var label = labelRepository.findById(testLabel.getId()).get();

        assertThat(label.getName()).isEqualTo(newName);
        assertThat(labelRepository.findByName(oldName)).isEmpty();
    }

    @Test
    public void testDestroy() throws Exception {
        var labelsCount = labelRepository.count();

        token = jwt().jwt(builder -> builder.subject(testLabel.getName()));

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(labelRepository.count()).isEqualTo(labelsCount - 1);
        assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }

    @Test
    public void testDestroyLabelWhichIsUsing() throws Exception {
        var taskForTest = testUtils.getTestTask();
        taskRepository.save(taskForTest);

        var labelForTest = taskForTest.getLabels().iterator().next();

        mockMvc.perform(delete("/api/labels/" + labelForTest.getId()).with(token))
                .andExpect(status().isInternalServerError());

        assertThat(labelRepository.findById(labelForTest.getId())).isPresent();
    }
}
