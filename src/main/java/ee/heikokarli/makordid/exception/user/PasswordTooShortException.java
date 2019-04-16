package ee.heikokarli.makordid.exception.user;

import ee.heikokarli.makordid.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordTooShortException extends ApiException {
    public PasswordTooShortException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }
    public PasswordTooShortException(String message) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), message);
    }
    public PasswordTooShortException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), "Password length must be 8 or more characters");
    }
}
