package hexlet.code.dto;

import javax.validation.constraints.Size;

public record TaskStatusDto(
        @Size(min = 1, message = "Name must contains at least 1 character")
        String name
) { }
