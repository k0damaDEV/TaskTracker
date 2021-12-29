package hexlet.code.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Nullable
    @Column(name = "description")
    private String description;
    @NotNull
    @ManyToOne
    private TaskStatus taskStatus;
    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;
    @ManyToOne
    @Nullable
    @JoinColumn(name = "executorId")
    private User executor;
    @JoinTable(name = "TASK_LABELS")
    @Nullable
    @ManyToMany
    List<Label> labels;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    public Task(String name, @Nullable String description, TaskStatus taskStatus, User author, @Nullable User executor) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.author = author;
        this.executor = executor;
    }
}
