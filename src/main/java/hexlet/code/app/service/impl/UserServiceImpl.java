package hexlet.code.app.service.impl;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public User createNewUser(UserDto userDto) {
        return userRepository.save(new User(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                encoder.encode(userDto.getPassword())
        ));
    }

    @Override
    public User updateUserData(Long id, UserDto userDto) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with such ID not found."));

        dbUser.setEmail(userDto.getEmail());
        dbUser.setFirstName(userDto.getFirstName());
        dbUser.setLastName(userDto.getLastName());
        dbUser.setPassword(encoder.encode(userDto.getPassword()));

        return userRepository.save(dbUser);
    }
}
