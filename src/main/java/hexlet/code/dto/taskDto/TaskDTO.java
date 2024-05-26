package hexlet.code.dto.taskDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {

    private Long id;

    private Long index;

    @JsonFormat(pattern = "dd.MM.yyyy, HH:mm")
    private LocalDateTime createdAt;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title;

    private String content;

    private String status;
}
