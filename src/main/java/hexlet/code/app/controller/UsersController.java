package hexlet.code.app.controller;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.exceptions.UserNotFoundException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {
    private static final String ID = "/{id}";


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

    @PatchMapping(ID)
    public User updateUser(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UserDto userDto
    ) {
        return userService.updateUserData(id, userDto);
    }

    @DeleteMapping(ID)
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return "OK";
    }
}
