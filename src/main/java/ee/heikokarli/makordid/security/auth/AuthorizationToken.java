package ee.heikokarli.makordid.security.auth;

import ee.heikokarli.makordid.security.UserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthorizationToken extends AbstractAuthenticationToken {

    private String token;
    private final UserDetails principal;

    public AuthorizationToken(String token) {
        super(null);
        this.principal = null;
        this.token = token;
    }

    public AuthorizationToken(UserDetails principal, String token) {
        super(null);
        this.token = token;
        this.principal = principal;
    }

    public AuthorizationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}