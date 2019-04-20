package ee.heikokarli.makordid.exception.song;

import ee.heikokarli.makordid.exception.EntityNotFoundException;

public class SongNotFoundException extends EntityNotFoundException {
    public SongNotFoundException() {
        super("Song not found.");
    }
}
