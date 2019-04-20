package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.repository.song.SongRepository;
import ee.heikokarli.makordid.exception.song.SongNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private SongRepository songRepository;
    private BandService bandService;
    @Autowired
    public SongService(SongRepository songRepository,
                       BandService bandService) {
        this.songRepository = songRepository;
        this.bandService = bandService;
    }

    public List<Song> getActiveBandSongs(Long bandId) {
        Band band = bandService.getBandById(bandId);
        return songRepository.findByBandAndStatus(band, Song.SongStatus.active);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElseThrow(SongNotFoundException::new);
    }

}
