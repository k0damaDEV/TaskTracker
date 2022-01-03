package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreationDto {
    @Size(min = 1, message = "Name must contains at least one character.")
    private String name;
    @Nullable
    private String description;
    @Nullable
    private Long executorId;
    @NotNull
    private Long taskStatusId;
    @Nullable
    private List<Long> labelIds;
}
