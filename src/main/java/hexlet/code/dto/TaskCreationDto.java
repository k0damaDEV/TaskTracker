package hexlet.code.dto;

import org.springframework.lang.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record TaskCreationDto(
        @Size(min = 1, message = "Name must contains at least one character.")
        String name,
        @Nullable String description,
        @Nullable Long executorId,
        @NotNull Long taskStatusId,
        @Nullable List<Long> labelIds

) { }
