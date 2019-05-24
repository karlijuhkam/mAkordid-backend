package ee.heikokarli.makordid.data.dto.response.song;

import ee.heikokarli.makordid.data.dto.song.SongDto;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import lombok.Data;

@Data
public class SongLikeResponse {
    private final SongDto song;

    public SongLikeResponse(SongLike songLike) {
        song = new SongDto(songLike.getSong());

    }
}

