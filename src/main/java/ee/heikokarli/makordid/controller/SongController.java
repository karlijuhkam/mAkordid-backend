package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.response.GenericMessageResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.dto.response.song.CheckLikeResponse;
import ee.heikokarli.makordid.data.dto.song.SongDto;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.data.entity.song.SongLike;
import ee.heikokarli.makordid.data.entity.user.User;
import ee.heikokarli.makordid.exception.song.SongNotActiveException;
import ee.heikokarli.makordid.service.SongLikeService;
import ee.heikokarli.makordid.service.SongService;
import ee.heikokarli.makordid.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"Songs"})
public class SongController extends AbstractApiController {

    private SongService songService;
    private UserService userService;
    private SongLikeService songLikeService;

    @Autowired
    public SongController(SongService songService,
                          SongLikeService songLikeService,
                          UserService userService) {
        this.songService = songService;
        this.songLikeService = songLikeService;
        this.userService = userService;
    }

    @ApiOperation(
            value = "Get active songs by band",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Band not found.", response = ErrorResponse.class)
    })
    @RequestMapping(path = "/bandsongs/{bandId}", method = RequestMethod.GET)
    public List<SongDto> getSongsByBand(@PathVariable Long bandId){
        return songService.getActiveBandSongs(bandId).stream().map(SongDto::new).collect(Collectors.toList());
    }

    @ApiOperation(
            value = "Get an active song by Id",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Song not active.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class)
    })
    @RequestMapping(path = "/activesongs/{id}", method = RequestMethod.GET)
    public SongDto getSongById(@PathVariable Long id){
        Song song = songService.getSongById(id);
        if (song.getStatus() == Song.SongStatus.inactive){
            throw new SongNotActiveException();
        } else {
            return new SongDto(song);
        }
    }

    @ApiOperation(
            value = "Like/unlike song",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Song not active.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class)
    })
    @RequestMapping(path = "/likesong/{songId}", method = RequestMethod.GET)
    public ResponseEntity<GenericMessageResponse> likeSong(@PathVariable Long songId){
        Song song = songService.getSongById(songId);
        User user = userService.getCurrentUser();
        SongLike songLike = songLikeService.getLikeBySongAndUser(song, user);
        if (song.getStatus() == Song.SongStatus.inactive) {
            throw new SongNotActiveException();
        }
        if (songLike != null) {
            songLikeService.unlikeSong(song, user);
            return new ResponseEntity<>(new GenericMessageResponse("Laul lemmikutest eemaldatud"), HttpStatus.OK);
        }
        songLikeService.likeSong(song, user);
        return new ResponseEntity<>(new GenericMessageResponse("Laul lisatud lemmikutesse"), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get like count and like status",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Song not active.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class)
    })
    @RequestMapping(path = "/likecheck/{songId}", method = RequestMethod.GET)
    public ResponseEntity<CheckLikeResponse> likeCheck(@PathVariable Long songId){
        Song song = songService.getSongById(songId);
        User user = userService.getCurrentUser();
        SongLike songLike = songLikeService.getLikeBySongAndUser(song, user);
        Long likeCount = songLikeService.getSongLikeCount(song);
        if (songLike != null) {
            return new ResponseEntity<>(new CheckLikeResponse(true, likeCount), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CheckLikeResponse(false, likeCount), HttpStatus.OK);
    }

}
