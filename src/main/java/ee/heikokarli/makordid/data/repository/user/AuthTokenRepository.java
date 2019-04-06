package ee.heikokarli.makordid.data.repository.user;

import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {

    AuthToken findAuthTokenById(String token);

    AuthToken findByUsername(String username);
}