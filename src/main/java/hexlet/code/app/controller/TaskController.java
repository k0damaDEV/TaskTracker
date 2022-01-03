package hexlet.code.app.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.dto.TaskCreationDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.app.controller.UsersController.ID;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.controller.UsersController.ONLY_OWNER_BY_ID;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String BY = "/by";

    private TaskService taskService;
    private TaskRepository taskRepository;

    @PostMapping
    public Task createNewTask(@Valid @RequestBody TaskCreationDto taskCreationDto) {
        return taskService.createNewTask(taskCreationDto);
    }

    @GetMapping(ID)
    public Task getTaskById(@PathVariable(name = "id") Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with such ID not found."));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PutMapping(ID)
    public Task updateTask(@PathVariable(name = "id") Long id, @Valid @RequestBody TaskCreationDto taskCreationDto) {
        return taskService.updateTask(id, taskCreationDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public String deleteTask(@PathVariable(name = "id") Long id) {
        taskRepository.deleteById(id);
        return "OK";
    }

    @GetMapping("/by")
    public Iterable<Task> getTasksBy(@QuerydslPredicate Predicate predicate) {
        return taskRepository.findAll(predicate);
    }
}
