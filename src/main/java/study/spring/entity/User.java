package study.spring.entity;

import static com.google.common.collect.FluentIterable.from;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class User extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 60, unique = true)
    private String email;
    @Column(nullable = false)
    private Boolean rate;
    @Column(nullable = false)
    private LocalDate experience;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
    })
    @JoinTable(name = "users_curses",
            joinColumns = { @JoinColumn(name = "id") },
            inverseJoinColumns = { @JoinColumn(name = "curs_id") })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Curse> curses = new HashSet<>();

    public void addCurse(Curse curse) {
        curses.add(curse);
        curse.getUsers().add(this);
    }

    public void removeCurse(Curse curse) {
        curses.remove(curse);
        curse.getUsers().remove(this);
    }

    public boolean hasRole(String role) {
        return from(roles).anyMatch(r -> role.equals(r.getName()));
    }
}