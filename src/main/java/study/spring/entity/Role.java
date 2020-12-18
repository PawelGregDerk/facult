package study.spring.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor = @__( @NonNull))
public class Role extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = 6146467702864087733L;
    @NonNull
    @Column(nullable = false, unique = true)
    private String name;
}
