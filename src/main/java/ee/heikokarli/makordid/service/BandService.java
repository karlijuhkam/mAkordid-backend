package ee.heikokarli.makordid.service;

import ee.heikokarli.makordid.data.dto.request.band.BandRequest;
import ee.heikokarli.makordid.data.entity.band.Band;
import ee.heikokarli.makordid.data.repository.band.BandRepository;
import ee.heikokarli.makordid.exception.band.BandAlreadyExistsException;
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

    public Band getBandByName(String name) {
        return bandRepository.findByNameIgnoreCase(name);
    }

    public List<Band> getAllBands() {
        return bandRepository.findAllByOrderByNameAsc();
    }

    public Page<Band> getBandSearch(String searchTerm, Pageable pageable) {
        Specification<Band> spec = where(null);

        if (searchTerm != null) spec = spec.and(search(searchTerm));

        return bandRepository.findAll(spec, pageable);
    }

    public Band modifyBand(Long bandId, BandRequest request) {
        Band band = this.getBandById(bandId);
        return this.setBandData(request, band);
    }

    public Band addBand(BandRequest request){
        return this.setBandData(request, new Band());
    }

    public Band setBandData(BandRequest request, Band band) {
        if (request.getName() != null) {
            Band bandCheck = getBandByName(request.getName());
            if (bandCheck == null) {
                band.setName(request.getName());
            } else {
                throw new BandAlreadyExistsException();
            }
        }

        if (request.getIntroduction() != null) {
            band.setIntroduction(request.getIntroduction());
        }

        return bandRepository.save(band);
    }

    public void deleteBand(Long bandId) {
        Band band = this.getBandById(bandId);
        bandRepository.delete(band);
    }
}
