package hexlet.code.component;

import hexlet.code.util.TaskStatusUtils;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.util.UserUtils;
import hexlet.code.util.LabelUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final UserUtils userUtils;

    @Autowired
    private final TaskStatusUtils taskStatusUtils;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final LabelUtils labelUtils;

    @Autowired
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var admin = userUtils.getAdmin();
        userService.createUser(admin);

        var defaultTaskStatuses = taskStatusUtils.getDefaultTaskStatuses();

        for (var taskStatus : defaultTaskStatuses) {
            taskStatusRepository.save(taskStatus);
        }

        var defaultLabels = labelUtils.getDefaultLabels();

        for (var label : defaultLabels) {
            labelRepository.save(label);
        }
    }
}
