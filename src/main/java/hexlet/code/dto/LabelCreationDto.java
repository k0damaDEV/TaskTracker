package hexlet.code.dto;

import javax.validation.constraints.Size;

public record LabelCreationDto(
        @Size(min = 1, message = "Name of label must contains at least 1 character.")
        String name
) { }
