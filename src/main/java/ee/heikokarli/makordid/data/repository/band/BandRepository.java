package ee.heikokarli.makordid.data.repository.band;

import ee.heikokarli.makordid.data.entity.band.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandRepository extends JpaRepository<Band, Long>, JpaSpecificationExecutor<Band> {

    List<Band> findAllByOrderByNameAsc();
}
