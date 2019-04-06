package ee.heikokarli.makordid.data.entity.band;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "band")
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "picture", nullable = false)
    private String picture;

    @OneToMany(mappedBy = "band")
    private List<Song> songs = new ArrayList<>();

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
