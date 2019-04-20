package ee.heikokarli.makordid.controller;
import ee.heikokarli.makordid.data.dto.band.BandDto;
import ee.heikokarli.makordid.data.dto.band.BandMinimizedDto;
import ee.heikokarli.makordid.data.dto.request.band.BandListRequest;
import ee.heikokarli.makordid.data.dto.response.band.BandResponse;
import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.service.BandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
            value = "Filtered/sorted/paginated bands",
            tags = "Bands"
    )
    @RequestMapping(path = "/bandlist", method = RequestMethod.GET)
    public Page<BandResponse> getServicesList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        BandListRequest request = new BandListRequest(search);

        Sort.Direction direction;

        if (sortDir.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        return bandService.getBandsList(request, of(page, 10, direction, sort))
                .map(BandResponse::new);
    }
}
