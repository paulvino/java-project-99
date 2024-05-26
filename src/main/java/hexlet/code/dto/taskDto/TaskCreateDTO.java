package hexlet.code.dto.taskDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    @NotBlank
    private String title;

    @Column(unique = true)
    private Long index;

    private String content;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @NotNull
    private String status;
}