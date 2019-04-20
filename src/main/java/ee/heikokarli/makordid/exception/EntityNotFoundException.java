package ee.heikokarli.makordid.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString(), "Entity not found");
    }
    public EntityNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString(), message);
    }
}
