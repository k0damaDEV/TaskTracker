package hexlet.code.service.impl;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TokenService;
import hexlet.code.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenAuthenticationService implements UserAuthenticationService {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        return userRepository.findUserByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> tokenService.expiring(Map.of("username", username)))
                .orElseThrow(() -> new BadCredentialsException("Invalid email and/or password"));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findUserByEmail(tokenService.verify(token).get("username").toString());
    }
}
