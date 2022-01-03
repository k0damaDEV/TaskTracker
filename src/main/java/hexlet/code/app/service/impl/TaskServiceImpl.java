package hexlet.code.app.service.impl;

import hexlet.code.app.dto.TaskCreationDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private UserRepository userRepository;
    private TaskStatusRepository taskStatusRepository;
    private TaskRepository taskRepository;
    private LabelRepository labelRepository;

    @Override
    public Task createNewTask(TaskCreationDto taskCreationDto) {
        Task task = new Task(
                taskCreationDto.getName(),
                taskCreationDto.getDescription(),
                taskStatusRepository.findById(taskCreationDto.getTaskStatusId())
                        .orElseThrow(() -> new NotFoundException("Task status with such ID not found.")),
                getCurrentUser(),
                userRepository.findById(taskCreationDto.getExecutorId())
                        .orElseThrow(() -> new NotFoundException("User with such ID not found."))
        );

        if (taskCreationDto.getLabelIds() != null) {
            task.setLabels(taskCreationDto.getLabelIds().stream().map(x -> labelRepository.findById(x)
                            .orElseThrow(() -> new NotFoundException("Label with such ID not found")))
                    .collect(Collectors.toList()));
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskCreationDto taskCreationDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with such ID not found."));

        task.setName(taskCreationDto.getName());
        task.setDescription(taskCreationDto.getDescription());
        task.setExecutor(userRepository.findById(taskCreationDto.getExecutorId())
                .orElseThrow(() -> new NotFoundException("User with such ID not found.")));
        task.setTaskStatus(taskStatusRepository.findById(taskCreationDto.getTaskStatusId())
                .orElseThrow(() -> new NotFoundException("Task status with such ID not found.")));

        if (taskCreationDto.getLabelIds() != null) {
            task.setLabels(taskCreationDto.getLabelIds().stream().map(x -> labelRepository.findById(x)
                            .orElseThrow(() -> new NotFoundException("Label with such ID not found.")))
                    .collect(Collectors.toList()));
        }

        return taskRepository.save(task);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser() {
        return userRepository.getByEmail(getCurrentUsername());
    }
}
