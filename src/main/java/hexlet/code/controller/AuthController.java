package hexlet.code.controller;


import hexlet.code.dto.UserLoginDto;
import hexlet.code.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.AuthController.LOGIN_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LOGIN_CONTROLLER_PATH)
public class AuthController {

    public static final String LOGIN_CONTROLLER_PATH = "/login";

    private final UserAuthenticationService authenticationService;

    @PostMapping(path = "")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return authenticationService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
    }

}
