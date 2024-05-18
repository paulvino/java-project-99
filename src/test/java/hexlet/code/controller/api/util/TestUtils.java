package hexlet.code.controller.api.util;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;

import java.time.format.DateTimeFormatter;

@Getter
@Component
public class TestUtils {

    private Model<User> userModel;

    private Model<TaskStatus> taskStatusModel;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .supply(Select.field(TaskStatus::getName), () -> faker.lorem().word())
                .toModel();
    }

    public void clean() {
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }
}
