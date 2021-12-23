package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;

public interface UserService {
    User createNewUser(UserDto userDto);
    User updateUserData(Long id, UserDto userDto);
}
