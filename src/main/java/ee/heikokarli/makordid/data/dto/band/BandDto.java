package ee.heikokarli.makordid.data.dto.band;

import ee.heikokarli.makordid.data.dto.song.SongMinimizedDto;
import ee.heikokarli.makordid.data.entity.band.Band;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BandDto implements Serializable {

    public BandDto(Band band) {
        this.id = band.getId();
        this.name = band.getName();
        this.introduction = band.getIntroduction();
        this.songs = band.getSongs().stream().map(SongMinimizedDto::new).collect(Collectors.toList());
    }
    private Long id;
    private String name;
    private String introduction;
    private List<SongMinimizedDto> songs;
}
