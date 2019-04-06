package ee.heikokarli.makordid.data.dto.response.auth;

import ee.heikokarli.makordid.data.dto.user.UserDto;
import ee.heikokarli.makordid.data.entity.auth.AuthToken;
import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponse implements Serializable {
    private String message = "Login successful!";
    private AuthToken token;
    private UserDto user;
    public LoginResponse(AuthToken token, User user) {
        this.token = token;
        this.user = new UserDto(user);
    }
}