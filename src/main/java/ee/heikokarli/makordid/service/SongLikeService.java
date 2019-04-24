package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.song.SongLikeRepository;
import ee.heikokarli.makordid.data.repository.song.SongRepository;
import ee.heikokarli.makordid.exception.song.SongNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Long getSongLikeCount(Song song) {
        return songLikeRepository.countSongLikeBySong(song);
    }

}
