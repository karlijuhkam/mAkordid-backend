package ee.heikokarli.makordid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends ApiException {
    private static final long serialVersionUID = 4043436953989551177L;

    public BadRequestException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), message);
    }
}