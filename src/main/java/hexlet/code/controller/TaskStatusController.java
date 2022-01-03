package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.UsersController.ID;

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
