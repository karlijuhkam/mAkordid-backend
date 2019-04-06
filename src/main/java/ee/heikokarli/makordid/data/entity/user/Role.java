package ee.heikokarli.makordid.data.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "create_date", nullable = false, updatable = false)
    @JsonIgnore
    private Date createTime = new Date();

    @Column(name = "update_date", nullable = false)
    @JsonIgnore
    private Date updateTime = new Date();

    @PreUpdate
    @PrePersist
    public void onCreateOnUpdate() {
        updateTime = new Date();
    }

}