package study.spring.entity;

import lombok.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor = @__( @NonNull))
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = "users")
@AttributeOverride(name = "id", column = @Column(name = "post_id"))
public class Post extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @NonNull
    @Column(length = 30, unique = true, nullable = false)
    private String post;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    private Set<User> users = new HashSet<>();
}