package ee.heikokarli.makordid.security.auth;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AuthorizationFilter extends AbstractAuthenticationProcessingFilter {

    private String headerName = "X-Auth-Token";

    public AuthorizationFilter() {
        super("/");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = obtainToken(request, response);
        if (!ObjectUtils.isEmpty(token)) {
            AuthorizationToken authenticationToken = new AuthorizationToken(token);
            return getAuthenticationManager().authenticate(authenticationToken);
        }
        throw new AuthenticationCredentialsNotFoundException("No authentication token found in request");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authentication);
        chain.doFilter(request, response);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        return true;
    }

    private String obtainToken(HttpServletRequest request, HttpServletResponse response) {

        List<String> headerNames = Collections.list(request.getHeaderNames());
        if (headerNames.contains(headerName.toLowerCase())) {
            return request.getHeader(headerName);
        }

        return null;
    }
}