package ee.heikokarli.makordid.data.dto.response.song;

import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;

@Data
public class SongResponse {
    private final Long id;
    private final String name;
    private final String user;
    private final String author;
    private final Long userId;
    private final Long likeCount;
    private final String band;
    private final String suggestedBand;
    private final Long bandId;
    private final Song.SongStatus status;

    public SongResponse(Song song) {
        id = song.getId();
        name = song.getName();
        author = song.getAuthor();
        user = song.getUser().getUsername();
        userId = song.getUser().getId();
        likeCount = song.getLikeCount();
        status = song.getStatus();
        suggestedBand = song.getSuggestedBand();

        if (song.getBand() != null) {
            band = song.getBand().getName();
        } else {
            band = "";
        }

        if (song.getBand() != null) {
            bandId = song.getBand().getId();
        } else {
            bandId = null;
        }

    }
}
