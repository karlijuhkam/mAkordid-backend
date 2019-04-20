package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.band.BandListRequest;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.repository.band.BandRepository;
import ee.heikokarli.makordid.exception.band.BandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static ee.heikokarli.makordid.data.specifications.BandSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class BandService {

    private BandRepository bandRepository;

    @Autowired
    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public Band getBandById(Long id) {
        return bandRepository.findById(id).orElseThrow(BandNotFoundException::new);
    }

    public List<Band> getAllBands() {
        return bandRepository.findAll();
    }

    public Page<Band> getBandsList(BandListRequest request, Pageable pageable) {
        Specification<Band> spec = where(null);

        if (request.getSearch() != null) spec = spec.and(search(request.getSearch()));

        return bandRepository.findAll(spec, pageable);
    }
}
