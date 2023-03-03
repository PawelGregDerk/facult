package study.spring.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "curses")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AttributeOverride(name = "id", column = @Column(name = "curs_id"))
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor_ = {@NonNull})
@ToString(callSuper = true, exclude = {"users", "materials"})
public class Curse extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    @Column(nullable = false, unique = true)
    @NonNull
    private String curseName;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "curses", cascade = {
        CascadeType.PERSIST, // CascadeType.ALL будет пытаться удалять также юзера
        CascadeType.MERGE
    })
    @OnDelete(action = OnDeleteAction.CASCADE) // без этого будет ошибка удаления методом delete, т. к. может быть связанная запись в табл. users_curses
    private Set<User> users = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "curse", cascade = CascadeType.ALL)
    private Set<Material> materials = new HashSet<>();
}
