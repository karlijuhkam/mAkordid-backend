package ee.heikokarli.makordid.data.dto.request.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}