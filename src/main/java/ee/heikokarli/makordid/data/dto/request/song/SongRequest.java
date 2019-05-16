package ee.heikokarli.makordid.data.dto.request.song;

import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;

import java.io.Serializable;

@Data
public class SongRequest implements Serializable {
    String name;
    Song.SongStatus status;
    String content;
    String author;
    Band band;
    String suggestedBand;
    String youtubeUrl;
    Long userId;
}
