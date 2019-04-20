package ee.heikokarli.makordid.data.dto.song;

import ee.heikokarli.makordid.data.entity.song.Song;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SongMinimizedDto implements Serializable {

    public SongMinimizedDto(Song song) {
        this.id = song.getId();
        this.name = song.getName();
        this.status = song.getStatus();
    }
    private Long id;
    private String name;
    private Song.SongStatus status;
}
