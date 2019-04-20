package ee.heikokarli.makordid.exception.song;

import ee.heikokarli.makordid.exception.EntityNotActiveException;

public class SongNotActiveException extends EntityNotActiveException {
    public SongNotActiveException() {
        super("Song not active.");
    }
}
