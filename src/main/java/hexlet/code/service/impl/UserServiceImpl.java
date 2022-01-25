package hexlet.code.service.impl;

import hexlet.code.dto.UserDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public User createNewUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            throw new DuplicateKeyException("User with such email already exists");
        }

        return userRepository.save(new User(
                userDto.firstName(),
                userDto.lastName(),
                userDto.email(),
                encoder.encode(userDto.password())
        ));
    }

    @Override
    public User updateUserData(Long id, UserDto userDto) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with such ID not found."));

        dbUser.setEmail(userDto.email());
        dbUser.setFirstName(userDto.firstName());
        dbUser.setLastName(userDto.lastName());
        dbUser.setPassword(encoder.encode(userDto.password()));

        return userRepository.save(dbUser);
    }
}
