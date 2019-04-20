package ee.heikokarli.makordid.exception;

import org.springframework.http.HttpStatus;

public class EntityNotActiveException extends ApiException {
    public EntityNotActiveException() {
        super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.toString(), "Entity not active");
    }
    public EntityNotActiveException(String message) {
        super(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.toString(), message);
    }
}
