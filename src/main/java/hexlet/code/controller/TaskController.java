package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskCreationDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String BY = "/by";
    private static final String ID = "/{id}";
    private static final String ONLY_TASK_OWNER_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    private TaskService taskService;
    private TaskRepository taskRepository;

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been created"),
            @ApiResponse(responseCode = "422", description = "Arguments not valid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createNewTask(@Valid @RequestBody TaskCreationDto taskCreationDto) {
        return taskService.createNewTask(taskCreationDto);
    }

    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(ID)
    public Task getTaskById(@PathVariable(name = "id") Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with such ID not found."));
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Get all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    @Operation(summary = "Change task information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was changed"),
            @ApiResponse(responseCode = "422", description = "Arguments not valid")
    })
    @PutMapping(ID)
    public Task updateTask(@PathVariable(name = "id") Long id, @Valid @RequestBody TaskCreationDto taskCreationDto) {
        return taskService.updateTask(id, taskCreationDto);
    }

    @Operation(summary = "Delete task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task has been deleted"),
            @ApiResponse(responseCode = "404", description = "Task with such ID not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_TASK_OWNER_BY_ID)
    public String deleteTask(@PathVariable(name = "id") Long id) {
        taskRepository.deleteById(id);
        return "OK";
    }

    @Operation(summary = "Filter tasks")
    @ApiResponse(responseCode = "200", description = "Filration result")
    @GetMapping(BY)
    public Iterable<Task> getTasksBy(@QuerydslPredicate Predicate predicate) {
        return taskRepository.findAll(predicate);
    }
}
