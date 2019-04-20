package ee.heikokarli.makordid.data.dto.user;

import ee.heikokarli.makordid.data.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserMinimizedDto implements Serializable {

    public UserMinimizedDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
    private Long id;
    private String username;
}
