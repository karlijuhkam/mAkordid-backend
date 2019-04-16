package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.repository.user.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Value("${auth.token.expiry.seconds}")
    private int tokenExpirySeconds;

    private AuthTokenRepository authTokenRepository;

    public AuthenticationService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    public AuthToken getToken(String token) {
        return authTokenRepository.findById(token).orElse(null);
    }

    public AuthToken saveToken(String username) {
        AuthToken authToken = new AuthToken();
        authToken.setEmail(username);
        final String token = UUID.randomUUID().toString();
        authToken.setId(token);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, tokenExpirySeconds);
        authToken.setExpiry(now.getTime());
        return authTokenRepository.save(authToken);
    }

    public void removeToken(AuthToken token) {
        authTokenRepository.delete(token);
    }
}
