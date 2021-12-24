package hexlet.code.app.controller;


import hexlet.code.app.dto.UserLoginDto;
import hexlet.code.app.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AuthController {

    private final UserAuthenticationService authenticationService;

    @PostMapping(path = "")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return authenticationService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
    }

}
