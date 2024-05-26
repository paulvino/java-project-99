package hexlet.code.dto.taskDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskParamsDTO {

    private String name;

    private Long assigneeId;

    private String taskStatus;
}
