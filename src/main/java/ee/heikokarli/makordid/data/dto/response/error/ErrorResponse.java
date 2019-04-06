package ee.heikokarli.makordid.data.dto.response.error;

public class ErrorResponse {

    private final String errorCode;
    private final String errorDescription;

    public ErrorResponse(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}