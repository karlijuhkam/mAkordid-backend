package ee.heikokarli.makordid.data.dto.response.song;

import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;

import java.util.Date;

@Data
public class SongResponse {
    private final Long id;
    private final String name;
    private final Song.SongStatus status;

    public SongResponse(Song song) {
        id = song.getId();
        name = song.getName();
        status = song.getStatus();
    }
}
