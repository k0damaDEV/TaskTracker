package hexlet.code.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public TaskStatus(String name) {
        this.name = name;
    }
}
