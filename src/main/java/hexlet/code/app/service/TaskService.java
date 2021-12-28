package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreationDto;
import hexlet.code.app.model.Task;

public interface TaskService {
    Task createNewTask(TaskCreationDto taskCreationDto);
    Task updateTask(Long id, TaskCreationDto taskCreationDto);
}
