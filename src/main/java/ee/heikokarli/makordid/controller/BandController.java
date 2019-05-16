package ee.heikokarli.makordid.controller;
import ee.heikokarli.makordid.data.dto.band.BandDto;
import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.dto.request.band.BandListRequest;
import ee.heikokarli.makordid.data.dto.request.band.BandRequest;
import ee.heikokarli.makordid.data.dto.response.band.BandResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.dto.user.UserDto;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.entity.song.Song;
import ee.heikokarli.makordid.service.BandService;
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
@Api(tags = {"Bands"})
public class BandController extends AbstractApiController {

    private BandService bandService;

    @Autowired
    public BandController(BandService bandService) {
        this.bandService = bandService;
    }

    @ApiOperation(
            value = "Get all bands paginated/filtered/sorted",
            tags = "Bands"
    )
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/bands", method = RequestMethod.GET)
    public Page<BandDto> getBandList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir
    ) {
        return bandService.getBandSearch(name, of(page, size, sortDir, sort))
                .map(BandDto::new);
    }

    @ApiOperation(
            value = "Get band by Id",
            tags = "Bands"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Band not found.", response = ErrorResponse.class)
    })
    @RequestMapping(path = "/allbands/{id}", method = RequestMethod.GET)
    public BandDto getBandById(@PathVariable Long id) {
        return new BandDto(bandService.getBandById(id));
    }

    @ApiOperation(
            value = "Get all bands",
            tags = "Bands"
    )
    @RequestMapping(path = "/allbands", method = RequestMethod.GET)
    public List<BandMinimizedDto> getBandList(){
        return bandService.getAllBands().stream().map(BandMinimizedDto::new).collect(Collectors.toList());
    }

    @ApiOperation(
            value = "Add a new band",
            tags = "Bands"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Access denied", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/bands", method = RequestMethod.POST)
    public ResponseEntity<BandDto> addBand(@Valid @RequestBody BandRequest request) {
        return new ResponseEntity<>(new BandDto(bandService.addBand(request)), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Patch band",
            tags = "Bands"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, message = "Access denied", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/bands/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<BandDto> patchBand(@PathVariable Long id, @Valid @RequestBody BandRequest request){
        Band band = bandService.modifyBand(id, request);
        return new ResponseEntity<>(new BandDto(band), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Delete a band",
            tags = "Bands"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message = "UNAUTHORIZED")
    })
    @PreAuthorize("hasAnyAuthority('admin', 'moderator')")
    @RequestMapping(path = "/bands/{id}", method = RequestMethod.DELETE)
    public void deleteBand(@PathVariable Long id) {
        bandService.deleteBand(id);
    }

    @ApiOperation(
            value = "Search for bands",
            tags = "Bands"
    )
    @RequestMapping(path = "/searchbands", method = RequestMethod.GET)
    public Page<BandResponse> getBandSearch(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String search
    ) {
        return bandService.getBandSearch(search, of(page, 10, Sort.Direction.ASC, "name"))
                .map(BandResponse::new);
    }
}
