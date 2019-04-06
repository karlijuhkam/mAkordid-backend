package ee.heikokarli.makordid.data.dto.user;

import ee.heikokarli.makordid.data.entity.user.Role;
import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.roles = user.getRoles();
        this.phone = user.getPhone();
        this.status = user.getStatus();
    }
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private Set<Role> roles;
    private String phone;
    private User.UserStatus status;
}