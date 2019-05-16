package ee.heikokarli.makordid.data.dto.request.song;

import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;

@Data
public class SongListRequest {
    private final String name;
    private final String suggestedBand;
    private final String user;
    private final String band;
    private final String author;
    private final Song.SongStatus status;
}
