package ee.heikokarli.makordid.data.specifications;

import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSpecifications extends AbstractSpecifications {
    public static Specification<User> username(String username) {
        return (root, query, criteriaBuilder) -> lowerLike(criteriaBuilder, root.get("username"), username);
    }

    public static Specification<User> email(String email) {
        return (root, query, criteriaBuilder) -> lowerLike(criteriaBuilder, root.get("email"), email);
    }

    public static Specification<User> status(User.UserStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<User> hasRoles(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<String> list = new ArrayList<>(Arrays.asList(searchTerm.split(",")));
            return root.join("roles").get("name").in(list);
        };
    }

}
