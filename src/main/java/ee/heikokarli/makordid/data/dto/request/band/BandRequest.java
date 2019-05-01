package ee.heikokarli.makordid.data.dto.request.band;

import lombok.Data;

import java.io.Serializable;

@Data
public class BandRequest implements Serializable {
    String name;
    String introduction;
}
