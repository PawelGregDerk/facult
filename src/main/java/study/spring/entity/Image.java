package study.spring.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "photos")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"sketch", "user"})
public class Image extends PersistentEntity implements Serializable {
    private static final long serialVersionUID = -7385033820597845805L;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] sketch;
    private String path;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
