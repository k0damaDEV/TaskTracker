package hexlet.code.app.service.impl;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        return taskStatusRepository.save(new TaskStatus(
                taskStatusDto.getName()
        ));
    }

    @Override
    public TaskStatus updateTaskStatus(TaskStatusDto taskStatusDto, Long id) {
        TaskStatus dbTask = taskStatusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task status with such ID not found"));

        dbTask.setName(taskStatusDto.getName());

        return taskStatusRepository.save(dbTask);
    }
}
