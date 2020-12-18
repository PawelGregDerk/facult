package study.spring.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "materials")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "m_id"))
public class Material extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = -3024355557388711373L;
    @EqualsAndHashCode.Include // если иквалс и хэшкод делать только по айди,
    @Column(nullable = false, unique = true) // все объекты Material тут будут равны, т. к. айди у всех null.
    private String path; // Поэтому эти методы надо делать ещё и по paths
    private String mediaType;
    @Column(nullable = false)
    private LocalDate loadDate;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "curs_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Curse curse;

    public Material(String path, String mediaType, LocalDate loadDate, Curse curse) {
        this.path = path;
        this.mediaType = mediaType;
        this.loadDate = loadDate;
        this.curse = curse;
    }
}
