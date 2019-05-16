package ee.heikokarli.makordid.exception;

import org.springframework.http.HttpStatus;

public class EntityNotActiveException extends ApiException {
    public EntityNotActiveException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "Entity not active");
    }
    public EntityNotActiveException(String message) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), message);
    }
}
