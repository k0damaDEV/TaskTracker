package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {
    @Size(min = 1, message = "Name must contains at least 1 character")
    private String name;
}
