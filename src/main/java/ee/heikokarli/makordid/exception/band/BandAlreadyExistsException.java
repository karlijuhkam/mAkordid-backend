package ee.heikokarli.makordid.exception.band;

import ee.heikokarli.makordid.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BandAlreadyExistsException extends ApiException {
    public BandAlreadyExistsException(String errorCode, String message) {
        super(HttpStatus.CONFLICT, errorCode, message);
    }
    public BandAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(), message);
    }
    public BandAlreadyExistsException() {
        super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.toString(), "Band already exists");
    }
}
