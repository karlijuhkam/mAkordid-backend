package ee.heikokarli.makordid.data.dto.request.user;

import ee.heikokarli.makordid.data.entity.user.Role;
import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class PatchUserRequest implements Serializable {
    String name;
    String phone;
    Integer age;
    String firstName;
    String lastName;
    @Size(min = 8, message = "Min length 8")
    String password;
    String oldPassword;
    List<Role> roles;
    User.UserStatus status;
}
