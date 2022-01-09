package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    private static final String ID = "/{id}";

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;

    @GetMapping(ID)
    public TaskStatus getStatusById(@PathVariable(name = "id") Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task status with such ID not found"));
    }

    @GetMapping
    public List<TaskStatus> getAllStatuses() {
        return taskStatusRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createTaskStatus(@Valid @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping(ID)
    public TaskStatus updateTaskStatus(@Valid @RequestBody TaskStatusDto taskStatusDto,
                                       @PathVariable(name = "id") Long id) {
        return taskStatusService.updateTaskStatus(taskStatusDto, id);
    }

    @DeleteMapping(ID)
    public String deleteTaskStatus(@PathVariable(name = "id") Long id) {
        taskStatusRepository.deleteById(id);
        return "OK";
    }
}
