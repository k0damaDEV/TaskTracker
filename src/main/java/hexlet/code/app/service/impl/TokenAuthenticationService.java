package hexlet.code.app.service.impl;

import hexlet.code.app.exceptions.UserNotFoundException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.TokenService;
import hexlet.code.app.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenAuthenticationService implements UserAuthenticationService {

    private TokenService tokenService;

    private UserRepository userRepository;

    @Override
    public String login(String username, String password) {
        return userRepository.findUserByEmail(username)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> tokenService.expiring(Map.of("username", username)))
                .orElseThrow(() -> new UserNotFoundException("Invalid email and/or password"));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findUserByEmail(tokenService.verify(token).get("username").toString());
    }
}
