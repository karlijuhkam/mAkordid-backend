package ee.heikokarli.makordid.data.dto.response.band;

import ee.heikokarli.makordid.data.entity.band.Band;
import lombok.Data;

@Data
public class BandResponse {
    private final Long id;
    private final String name;

    public BandResponse(Band band) {
        id = band.getId();
        name = band.getName();
    }
}
