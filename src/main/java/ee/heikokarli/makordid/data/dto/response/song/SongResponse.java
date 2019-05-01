package ee.heikokarli.makordid.data.dto.response.song;

import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;

import java.util.Date;

@Data
public class SongResponse {
    private final Long id;
    private final String name;
    private final Long likeCount;
    private final Song.SongStatus status;
    private final Date createTime;
    private final BandMinimizedDto band;

    public SongResponse(Song song) {
        id = song.getId();
        name = song.getName();
        likeCount = song.getLikeCount();
        status = song.getStatus();
        createTime = song.getCreateTime();
        band = new BandMinimizedDto(song.getBand());
    }
}
