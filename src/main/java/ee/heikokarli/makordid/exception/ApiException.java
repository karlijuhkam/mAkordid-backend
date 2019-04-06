package ee.heikokarli.makordid.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 3741734054690769288L;

    public static final String BAD_REQUEST = "BAD_REQUEST";

    private final HttpStatus statusCode;
    private final String errorCode;

    public ApiException(String description) {
        this(HttpStatus.BAD_REQUEST, ApiException.BAD_REQUEST, description);
    }

    public ApiException(HttpStatus statusCode, String errorCode, String description) {
        super(description);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return getMessage();
    }

}