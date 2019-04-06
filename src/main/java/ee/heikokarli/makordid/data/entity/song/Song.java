package ee.heikokarli.makordid.data.entity.song;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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