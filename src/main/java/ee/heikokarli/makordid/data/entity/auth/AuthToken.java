package ee.heikokarli.makordid.data.entity.auth;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@RedisHash("auth-token")
@Data
public class AuthToken implements Serializable {

    @Id
    private String id;
    private String username;
    private Date expiry;

}

