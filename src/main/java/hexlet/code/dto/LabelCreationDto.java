package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelCreationDto {
    @Size(min = 1, message = "Name of label must contains at least 1 character.")
    private String name;
}
