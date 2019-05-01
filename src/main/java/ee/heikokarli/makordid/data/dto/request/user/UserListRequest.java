package ee.heikokarli.makordid.data.dto.request.user;

import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;

@Data
public class UserListRequest {
    private final String username;
    private final String email;
    private final String roles;
    private final User.UserStatus status;
}
