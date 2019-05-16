package ee.heikokarli.makordid.exception.user;

import ee.heikokarli.makordid.exception.EntityNotActiveException;

public class UserInactiveException extends EntityNotActiveException {
    public UserInactiveException() {
        super("Kasutaja pole aktiivne.");
    }
}
