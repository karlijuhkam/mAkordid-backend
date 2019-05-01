package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.song.SongListRequest;
import ee.heikokarli.makordid.data.dto.request.song.SongRequest;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.data.repository.song.SongRepository;
import ee.heikokarli.makordid.exception.song.SongNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static ee.heikokarli.makordid.data.specifications.SongSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class SongService {

    private SongRepository songRepository;
    private UserService userService;
    private BandService bandService;
    @Autowired
    public SongService(SongRepository songRepository,
                       UserService userService,
                       BandService bandService) {
        this.songRepository = songRepository;
        this.userService = userService;
        this.bandService = bandService;
    }

    public List<Song> getActiveBandSongs(Long bandId) {
        Band band = bandService.getBandById(bandId);
        return songRepository.findByBandAndStatus(band, Song.SongStatus.active);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElseThrow(SongNotFoundException::new);
    }

    public Page<Song> getSpecifiedSongs(Pageable pageable) {
        Specification<Song> spec = where(null);
        spec = spec.and(status(Song.SongStatus.active));
        return songRepository.findAll(spec, pageable);
    }

    public Page<Song> getSongSearch(String searchTerm, Pageable pageable) {
        Specification<Song> spec = where(null);

        if (searchTerm != null) spec = spec.and(search(searchTerm));
        spec = spec.and(status(Song.SongStatus.active));

        return songRepository.findAll(spec, pageable);
    }

    public Page<Song> getAllSongs(SongListRequest request, Pageable pageable) {
        Specification<Song> spec = where(null);

        if (request.getName() != null) spec = spec.and(name(request.getName()));
        if (request.getBand() != null) spec = spec.and(band(request.getBand()));
        if (request.getStatus() != null) spec = spec.and(status(request.getStatus()));
        if (request.getUser() != null) spec = spec.and(user(request.getUser()));

        return songRepository.findAll(spec, pageable);
    }

    public Song modifySong(Long songId, SongRequest request) {
        Song song = this.getSongById(songId);
        return this.setSongData(request, song);
    }

    public Song addSong(SongRequest request){
        Song song = new Song();
        song.setUser(userService.getCurrentUser());
        song.setStatus(Song.SongStatus.inactive);
        return this.setSongData(request, song);
    }

    public void deleteSong(Long songId) {
        Song song = this.getSongById(songId);
        songRepository.delete(song);
    }

    public Song setSongData(SongRequest request, Song song) {
        if (request.getName() != null) {
            song.setName(request.getName());
        }

        if (request.getContent() != null) {
            song.setContent(request.getContent());
        }

        if (request.getStatus() != null) {
            song.setStatus(request.getStatus());
        }

        if (request.getBand() != null) {
            song.setBand(request.getBand());
        }

        if (request.getSuggestedBand() != null) {
            song.setSuggestedBand(request.getSuggestedBand());
        }

        return songRepository.save(song);
    }

}
