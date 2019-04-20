package ee.heikokarli.makordid.exception.band;

import ee.heikokarli.makordid.exception.EntityNotFoundException;

public class BandNotFoundException extends EntityNotFoundException {
    public BandNotFoundException() {
        super("Band not found.");
    }
}
