package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.song.SongLikeRepository;
import ee.heikokarli.makordid.data.repository.song.SongRepository;
import ee.heikokarli.makordid.exception.song.SongNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static ee.heikokarli.makordid.data.specifications.SongLikeSpecifications.status;
import static ee.heikokarli.makordid.data.specifications.SongLikeSpecifications.user;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class SongLikeService {

    private SongRepository songRepository;
    private SongLikeRepository songLikeRepository;
    private UserService userService;
    private SongService songService;
    @Autowired
    public SongLikeService(SongRepository songRepository,
                           UserService userService,
                           SongLikeRepository songLikeRepository,
                           SongService songService) {
        this.songRepository = songRepository;
        this.userService = userService;
        this.songLikeRepository = songLikeRepository;
        this.songService = songService;
    }

    public SongLike getLikeBySongAndUser(Song song, User user) {
        return songLikeRepository.findBySongAndUser(song, user);
    }

    public void likeSong(Song song, User user) {
        SongLike songLike = new SongLike();
        songLike.setSong(song);
        songLike.setUser(user);
        songLikeRepository.save(songLike);
    }

    public void unlikeSong(Song song, User user) {
        SongLike songLike = getLikeBySongAndUser(song, user);
        songLikeRepository.delete(songLike);
    }

    public Page<SongLike> getLikedSongs(Pageable pageable) {
        Specification<SongLike> spec = where(null);
        spec = spec.and(user(userService.getCurrentUser()));
        spec = spec.and(status(Song.SongStatus.active));
        return songLikeRepository.findAll(spec, pageable);
    }

}
