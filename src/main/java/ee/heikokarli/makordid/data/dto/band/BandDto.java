package ee.heikokarli.makordid.data.dto.band;

import ee.heikokarli.makordid.data.entity.band.Band;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BandDto implements Serializable {

    public BandDto(Band band) {
        this.id = band.getId();
        this.name = band.getName();
        this.introduction = band.getIntroduction();
        this.songCount = band.getSongCount();
    }
    private Long id;
    private String name;
    private String introduction;
    private Long songCount;
}
