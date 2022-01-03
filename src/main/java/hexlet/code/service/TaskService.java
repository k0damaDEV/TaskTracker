package hexlet.code.service;

import hexlet.code.dto.TaskCreationDto;
import hexlet.code.model.Task;

public interface TaskService {
    Task createNewTask(TaskCreationDto taskCreationDto);
    Task updateTask(Long id, TaskCreationDto taskCreationDto);
}
