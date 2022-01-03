package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UsersController.USERS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USERS_CONTROLLER_PATH)
public class UsersController {
    public static final String ID = "/{id}";
    public static final String USERS_CONTROLLER_PATH = "/users";
    public static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "All users")
    @GetMapping()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been created"),
            @ApiResponse(responseCode = "422", description = "Invalid arguments")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User createUser(@RequestBody @Valid final UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user"),
            @ApiResponse(responseCode = "404", description = "User with such ID not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(ID)
    public User getUserById(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with such ID not found"));
    }

    @Operation(summary = "Change user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been changed"),
            @ApiResponse(responseCode = "404", description = "User with such ID not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized/Can't update another user"),
            @ApiResponse(responseCode = "422", description = "User with such ID not found")
    })
    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UserDto userDto
    ) {
        return userService.updateUserData(id, userDto);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been deleted"),
            @ApiResponse(responseCode = "404", description = "User with such ID not found"),
            @ApiResponse(responseCode = "401", description = "Can't delete another user/Unauthorized")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "OK";
    }
}
