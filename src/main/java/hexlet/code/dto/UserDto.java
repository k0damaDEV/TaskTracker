package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    @Email(message = "Email is not valid.")
    @Column(unique = true)
    private String email;
    @NotNull
    @Size(min = 1, message = "First name must contains at least one character")
    private String firstName;
    @NotNull
    @Size(min = 1, message = "Last name must contains at least one character")
    private String lastName;
    @NotNull
    @Size(min = 3, message = "Password must contains at least 3 characters.")
    private String password;
}
