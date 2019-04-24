package ee.heikokarli.makordid.data.repository.song;

import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import ee.heikokarli.makordid.data.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongLikeRepository extends JpaRepository<SongLike, Long> {

    Long countSongLikeBySong(Song song);

    SongLike findBySongAndUser(Song song, User user);

}
