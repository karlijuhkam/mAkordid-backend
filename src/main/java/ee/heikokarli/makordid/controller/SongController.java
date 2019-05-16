package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.request.band.BandListRequest;
import ee.heikokarli.makordid.data.dto.request.song.SongListRequest;
import ee.heikokarli.makordid.data.dto.request.song.SongRequest;
import ee.heikokarli.makordid.data.dto.response.GenericMessageResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.dto.response.song.CheckLikeResponse;
import ee.heikokarli.makordid.data.dto.response.song.SongResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.PageRequest.of;

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
    public SongDto getActiveSongById(@PathVariable Long id){
        Song song = songService.getSongById(id);
        if (song.getStatus() == Song.SongStatus.inactive){
            throw new SongNotActiveException();
        } else {
            return new SongDto(song);
        }
    }

    @ApiOperation(
            value = "Search for songs",
            tags = "Songs"
    )
    @RequestMapping(path = "/searchsongs", method = RequestMethod.GET)
    public Page<SongResponse> getSongSearch(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String search
    ) {
        return songService.getSongSearch(search, of(page, 10, Sort.Direction.ASC, "name"))
                .map(SongResponse::new);
    }

    @ApiOperation(
            value = "Get all songs paginated/filtered/sorted",
            tags = "Songs"
    )
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/songs", method = RequestMethod.GET)
    public Page<SongResponse> getSongList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String suggestedBand,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String band,
            @RequestParam(required = false) Song.SongStatus status,
            @RequestParam(defaultValue = "createTime") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir
    ) {

        SongListRequest request = new SongListRequest(name, suggestedBand, user, band, author,status);

        return songService.getAllSongs(request, of(page, size, sortDir, sort))
                .map(SongResponse::new);
    }

    @ApiOperation(
            value = "Get any song by Id",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/songs/{id}", method = RequestMethod.GET)
    public SongDto getSongById(@PathVariable Long id){
        Song song = songService.getSongById(id);
        return new SongDto(song);
    }

    @ApiOperation(
            value = "Add a new song",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @RequestMapping(path = "/songs", method = RequestMethod.POST)
    public ResponseEntity<SongResponse> addSong(@Valid @RequestBody SongRequest request) {
        return new ResponseEntity<>(new SongResponse(songService.addSong(request)), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Delete a song",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/songs/{id}", method = RequestMethod.DELETE)
    public void deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
    }

    @ApiOperation(
            value = "Patch song",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Access denied", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/songs/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<SongDto> patchSong(@PathVariable Long id, @Valid @RequestBody SongRequest request){
        Song song = songService.modifySong(id, request);
        return new ResponseEntity<>(new SongDto(song), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Like/unlike song",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Song not active.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
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
            value = "Get like status",
            tags = "Songs"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Song not active.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Song not found.", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @RequestMapping(path = "/likecheck/{songId}", method = RequestMethod.GET)
    public ResponseEntity<CheckLikeResponse> likeCheck(@PathVariable Long songId){
        Song song = songService.getSongById(songId);
        User user = userService.getCurrentUser();
        SongLike songLike = songLikeService.getLikeBySongAndUser(song, user);
        if (songLike != null) {
            return new ResponseEntity<>(new CheckLikeResponse(true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CheckLikeResponse(false), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get top 5 songs by create time",
            tags = "Songs"
    )
    @RequestMapping(path = "/recentsongs", method = RequestMethod.GET)
    public Page<SongDto> getTopTenByCreateTime() {
        return songService.getSpecifiedSongs(of(0, 5, Sort.Direction.DESC, "createTime"))
                .map(SongDto::new);
    }

    @ApiOperation(
            value = "Get top 5 songs by like count",
            tags = "Songs"
    )
    @RequestMapping(path = "/popularsongs", method = RequestMethod.GET)
    public Page<SongDto> getTopTenByLikeCount() {
        return songService.getSpecifiedSongs(of(0, 5, Sort.Direction.DESC, "likeCount"))
                .map(SongDto::new);
    }

}
