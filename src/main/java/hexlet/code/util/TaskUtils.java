package hexlet.code.util;

import hexlet.code.dto.taskDto.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskUtils {

    public Specification<Task> build(TaskParamsDTO params) {
        return withName(params.getName())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withTaskStatus(params.getTaskStatus()));
    }

    private Specification<Task> withName(String data) {
        return (root, query, criteriaBuilder) -> data == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + data + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, criteriaBuilder) -> assigneeId == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withTaskStatus(String slug) {
        return (root, query, criteriaBuilder) -> slug == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("taskStatus").get("slug"), slug);
    }
}
