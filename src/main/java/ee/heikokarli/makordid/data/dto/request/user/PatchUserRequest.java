package ee.heikokarli.makordid.data.dto.request.user;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

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
}
