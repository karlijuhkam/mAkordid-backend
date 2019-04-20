package ee.heikokarli.makordid.data.dto.song;

import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.dto.user.UserMinimizedDto;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SongDto implements Serializable {

    public SongDto(Song song) {
        this.id = song.getId();
        this.name = song.getName();
        this.content = song.getContent();
        this.band = new BandMinimizedDto(song.getBand());
        this.user = new UserMinimizedDto(song.getUser());
    }
    private Long id;
    private String name;
    private String content;
    private BandMinimizedDto band;
    private UserMinimizedDto user;
}
