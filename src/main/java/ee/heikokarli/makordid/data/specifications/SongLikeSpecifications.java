package ee.heikokarli.makordid.data.specifications;

import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import ee.heikokarli.makordid.data.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

public class SongLikeSpecifications extends AbstractSpecifications{
    public static Specification<SongLike> user(User user) {
        return (root, query, criteriaBuilder ) -> criteriaBuilder.equal(root.get("user"), user);
    }
    public static Specification<SongLike> status(Song.SongStatus status) {
        return (root, query, criteriaBuilder ) -> criteriaBuilder.equal(root.get("song").get("status"), status);
    }
}
