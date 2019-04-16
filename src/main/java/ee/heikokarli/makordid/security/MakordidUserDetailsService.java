package ee.heikokarli.makordid.security;

import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.user.UserRepository;
import ee.heikokarli.makordid.exception.BadRequestException;
import ee.heikokarli.makordid.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

public class MakordidUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public org.springframework.security.core.userdetails.UserDetails loadUserByAccessToken(String token) throws BadRequestException {

        AuthToken authToken = authenticationService.getToken(token);
        if (authToken == null) {
            throw new TokenNotFoundException("Token " + token + " not found");
        }
        if (authToken.getExpiry().before(new Date())) {
            throw new TokenNotFoundException("Token expired");
        }
        User user = userRepository.findByEmail(authToken.getEmail());
        if (user == null) {
            throw new TokenNotFoundException("User with token " + token + " not found");
        }
        return new UserDetails(user, authToken);
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetails(user, null);
    }

}
