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

import hexlet.code.model.User;
import hexlet.code.util.UserUtils;
import hexlet.code.repository.UserRepository;
import hexlet.code.controller.api.util.ModelGenerator;
import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;

    private User testUser;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject(UserUtils.ADMIN_EMAIL));
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/users")
                .with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/users/" + testUser.getId())
                .with(token);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).get();

        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getUserModel())
                .create();

        var usersCount = userRepository.count();

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        assertThat(userRepository.count()).isEqualTo(usersCount + 1);

        var user = userRepository.findByEmail(data.getEmail()).get();

        assertNotNull(user);
        assertThat(userRepository.findByEmail(testUser.getEmail())).isPresent();

        assertThat(user.getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.getLastName()).isEqualTo(data.getLastName());
        assertThat(user.getEmail()).isEqualTo(data.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        var newMail = faker.internet().emailAddress();
        var data = new HashMap<>();
        data.put("firstName", "Hope");
        data.put("email", newMail);

        var usersCount = userRepository.count();

        var oldEmail = testUser.getEmail();

        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(userRepository.count()).isEqualTo(usersCount);

        var user = userRepository.findById(testUser.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(("Hope"));
        assertThat(user.getEmail()).isEqualTo(newMail);
        assertThat(userRepository.findByEmail(oldEmail)).isEmpty();
    }

    @Test
    public void testDestroy() throws Exception {
        var usersCount = userRepository.count();

        mockMvc.perform(delete("/api/users/" + testUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.count()).isEqualTo(usersCount - 1);
        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }
}
