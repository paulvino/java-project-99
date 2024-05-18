package hexlet.code.util;

import hexlet.code.model.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TaskStatusUtils {

    @Bean
    public List<TaskStatus> getDefaultTaskStatuses() {

        var creationStatus = createTaskStatus("Creation", "creation");
        var newStatus = createTaskStatus("New", "new");
        var inProgressStatus = createTaskStatus("In progress", "in_progress");
        var inTestingStatus = createTaskStatus("In testing", "in_testing");
        var doneStatus = createTaskStatus("Done", "done");

        return List.of(
                creationStatus,
                newStatus,
                inProgressStatus,
                inTestingStatus,
                doneStatus
        );
    }

    private TaskStatus createTaskStatus(String name, String slug) {
        var taskStatus = new TaskStatus();

        taskStatus.setName(name);
        taskStatus.setSlug(slug);

        return taskStatus;
    }
}
