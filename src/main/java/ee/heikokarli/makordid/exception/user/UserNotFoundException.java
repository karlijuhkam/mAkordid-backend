package ee.heikokarli.makordid.exception.user;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() { super("User not found."); }
}