package ee.heikokarli.makordid.data.entity.song;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

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

    @Column(name = "suggested_band", nullable = false)
    private String suggestedBand;

    @Column(name = "content", nullable = false)
    private String content;

    @Formula("(SELECT COUNT(*) FROM user_likes_song us WHERE us.song_id = id)")
    private Long likeCount;

    @ManyToOne
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status", nullable = false, columnDefinition = "song_status")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private SongStatus status;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createTime = new Date();

    @Column(name = "update_date", nullable = false)
    private Date updateTime = new Date();

    @PreUpdate
    @PrePersist
    public void onCreateOnUpdate() {
        updateTime = new Date();
    }

    public enum SongStatus {
        active, inactive
    }

}
