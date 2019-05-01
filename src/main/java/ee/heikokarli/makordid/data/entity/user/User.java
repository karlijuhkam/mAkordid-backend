package ee.heikokarli.makordid.data.entity.user;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
})
@Table(name = "user_acc")
public class User implements Serializable {

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Formula("(SELECT COUNT(*) FROM user_likes_song us WHERE us.user_id = id)")
    private Long likedSongsCount;

    @Formula("(SELECT COUNT(*) FROM song s WHERE s.user_id = id AND s.status = 'active')")
    private Long addedSongsCount;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinTable(name = "user_has_role",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "status", nullable = false, columnDefinition = "active_status")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private UserStatus status;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createTime = new Date();

    @Column(name = "update_date", nullable = false)
    private Date updateTime = new Date();

    @PreUpdate
    @PrePersist
    public void onCreateOnUpdate() {
        updateTime = new Date();
    }

    public enum UserStatus {
        active, temporarily_inactive, inactive
    }
}
