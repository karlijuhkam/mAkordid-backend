package ee.heikokarli.makordid.exception.user;

import ee.heikokarli.makordid.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String errorCode, String message) {
        super(HttpStatus.CONFLICT, errorCode, message);
    }
    public UserAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(), message);
    }
    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(), "User already exists");
    }
}
