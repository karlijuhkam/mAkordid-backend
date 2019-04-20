package ee.heikokarli.makordid.data.specifications;

import ee.heikokarli.makordid.data.entity.band.Band;
import org.springframework.data.jpa.domain.Specification;

public class BandSpecifications extends AbstractSpecifications{

    public static Specification<Band> search(String name) {
        return (root, query, criteriaBuilder ) -> lowerLike(criteriaBuilder, root.get("name"), name);
    }

}
