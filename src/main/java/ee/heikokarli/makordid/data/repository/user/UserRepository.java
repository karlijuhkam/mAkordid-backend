package ee.heikokarli.makordid.data.repository.user;

import ee.heikokarli.makordid.data.entity.user.Role;
import ee.heikokarli.makordid.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByRoles(Role role);
}