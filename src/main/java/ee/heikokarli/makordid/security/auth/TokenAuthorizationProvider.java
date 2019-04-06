package ee.heikokarli.makordid.security.auth;

import ee.heikokarli.makordid.data.repository.user.AuthTokenRepository;
import ee.heikokarli.makordid.data.repository.user.UserRepository;
import ee.heikokarli.makordid.exception.BadRequestException;
import ee.heikokarli.makordid.security.MakordidUserDetailsService;
import ee.heikokarli.makordid.security.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthorizationProvider implements AuthenticationProvider {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MakordidUserDetailsService userDetailsService;

    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public TokenAuthorizationProvider(
            MakordidUserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthorizationToken token = (AuthorizationToken) authentication;

        UserDetails userDetails;
        try {
            userDetails = (UserDetails) userDetailsService.loadUserByAccessToken(token.getToken());
        } catch (BadRequestException e) {
            logger.error(e.getMessage(), e);
            throw new ee.heikokarli.makordid.security.auth.AuthenticationException(e.getMessage(), e);
        }
        List<SimpleGrantedAuthority> authorities = userDetails.getUser().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        token = new AuthorizationToken(userDetails, token.getToken(), authorities);
        token.setDetails(null);
        token.setAuthenticated(true);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthorizationToken.class.isAssignableFrom(authentication);
    }

}