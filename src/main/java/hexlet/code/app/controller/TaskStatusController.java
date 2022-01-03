package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.app.controller.UsersController.ID;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";

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
