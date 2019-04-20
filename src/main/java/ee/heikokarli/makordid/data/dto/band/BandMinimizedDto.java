package ee.heikokarli.makordid.data.dto.band;

import ee.heikokarli.makordid.data.entity.band.Band;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BandMinimizedDto implements Serializable {

    public BandMinimizedDto(Band band) {
        this.id = band.getId();
        this.name = band.getName();
    }
    private Long id;
    private String name;
}
