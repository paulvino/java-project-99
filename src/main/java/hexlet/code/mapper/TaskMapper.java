package hexlet.code.mapper;

import hexlet.code.dto.taskDto.TaskCreateDTO;
import hexlet.code.dto.taskDto.TaskDTO;
import hexlet.code.dto.taskDto.TaskUpdateDTO;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task destination);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
    }
}
