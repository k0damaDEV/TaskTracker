package hexlet.code.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserDto(
        @NotNull
        @Email(message = "Email is not valid.")
        String email,
        @NotNull
        @Size(min = 1, message = "First name must contains at least one character")
        String firstName,
        @NotNull
        @Size(min = 1, message = "Last name must contains at least one character")
        String lastName,
        @NotNull
        @Size(min = 3, message = "Password must contains at least 3 characters.")
        String password
) { }
