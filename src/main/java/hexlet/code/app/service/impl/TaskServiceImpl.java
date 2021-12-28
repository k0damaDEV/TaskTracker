package hexlet.code.app.service.impl;

import hexlet.code.app.dto.TaskCreationDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private UserRepository userRepository;
    private TaskStatusRepository taskStatusRepository;
    private TaskRepository taskRepository;

    @Override
    public Task createNewTask(TaskCreationDto taskCreationDto) {
        return taskRepository.save(new Task(
                taskCreationDto.getName(),
                taskCreationDto.getDescription(),
                taskStatusRepository.findById(taskCreationDto.getTaskStatusId()).orElse(null),
                getCurrentUser(),
                userRepository.findById(taskCreationDto.getExecutorId()).orElse(null)
        ));
    }

    @Override
    public Task updateTask(Long id, TaskCreationDto taskCreationDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with such ID not found."));

        task.setName(taskCreationDto.getName());
        task.setDescription(taskCreationDto.getDescription());
        task.setExecutor(userRepository.findById(taskCreationDto.getExecutorId())
                .orElseThrow(() -> new NotFoundException("Task with such ID not found.")));
        task.setTaskStatus(taskStatusRepository.findById(taskCreationDto.getTaskStatusId())
                .orElseThrow(() -> new NotFoundException("Task status with such ID not found.")));

        return taskRepository.save(task);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser() {
        return userRepository.getByEmail(getCurrentUsername());
    }
}
