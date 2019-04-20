package ee.heikokarli.makordid.data.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

abstract class AbstractSpecifications {

    static Predicate lowerLike(CriteriaBuilder cb, Expression<String> field, String q) {
        if (q == null) return field.isNull();

        return cb.like(cb.lower(field), "%" + q.toLowerCase() + "%");
    }

}
