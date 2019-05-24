package ee.heikokarli.makordid.data.specifications;

import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

public class SongSpecifications extends AbstractSpecifications{

    public static Specification<Song> name(String name) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("name"), name);
    }

    public static Specification<Song> suggestedBand(String name) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("suggestedBand"), name);
    }

    public static Specification<Song> band(String band) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("band").get("name"), band);
    }

    public static Specification<Song> user(String user) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("user").get("username"), user);
    }

    public static Specification<Song> specificUser(User user) {
        return (root, query, criteriaBuilder ) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Song> author(String author) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("author"), author);
    }

    public static Specification<Song> status(Song.SongStatus status) {
        return (root, query, criteriaBuilder ) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Song> search(String name) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("name"), name);
    }

}
