package ee.heikokarli.makordid.security.auth;

public class AuthenticationException extends org.springframework.security.core.AuthenticationException {

    private static final long serialVersionUID = 7407784037543982837L;

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}