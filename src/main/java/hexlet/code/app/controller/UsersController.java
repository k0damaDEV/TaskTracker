package hexlet.code.app.controller;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.exceptions.UserNotFoundException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.UsersController.USERS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USERS_CONTROLLER_PATH)
public class UsersController {
    public static final String ID = "/{id}";
    public static final String USERS_CONTROLLER_PATH = "/users";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;


    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping()
    public User createUser(@RequestBody @Valid final UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @GetMapping(ID)
    public User getUserById(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with such ID not found"));
    }

    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UserDto userDto
    ) {
        return userService.updateUserData(id, userDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "OK";
    }
}
