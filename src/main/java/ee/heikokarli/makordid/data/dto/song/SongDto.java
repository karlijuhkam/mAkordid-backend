package ee.heikokarli.makordid.data.dto.song;

import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.dto.user.UserMinimizedDto;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class SongDto implements Serializable {

    public SongDto(Song song) {
        this.id = song.getId();
        this.name = song.getName();
        this.content = song.getContent();
        this.likeCount = song.getLikeCount();
        this.band = new BandMinimizedDto(song.getBand());
        this.user = new UserMinimizedDto(song.getUser());
        this.createTime = song.getCreateTime();
        this.updateTime = song.getUpdateTime();
        this.status = song.getStatus();
    }
    private Long id;
    private String name;
    private String content;
    private Long likeCount;
    private BandMinimizedDto band;
    private UserMinimizedDto user;
    private Date createTime;
    private Date updateTime;
    private Song.SongStatus status;
}
