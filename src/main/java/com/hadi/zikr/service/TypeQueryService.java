package com.hadi.zikr.service;

import com.hadi.zikr.domain.*; // for static metamodels
import com.hadi.zikr.domain.Type;
import com.hadi.zikr.repository.TypeRepository;
import com.hadi.zikr.service.criteria.TypeCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Type} entities in the database.
 * The main input is a {@link TypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Type} or a {@link Page} of {@link Type} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeQueryService extends QueryService<Type> {

    private final Logger log = LoggerFactory.getLogger(TypeQueryService.class);

    private final TypeRepository typeRepository;

    public TypeQueryService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Return a {@link List} of {@link Type} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Type> findByCriteria(TypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Type> specification = createSpecification(criteria);
        return typeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Type} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Type> findByCriteria(TypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Type> specification = createSpecification(criteria);
        return typeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Type> specification = createSpecification(criteria);
        return typeRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Type> createSpecification(TypeCriteria criteria) {
        Specification<Type> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Type_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Type_.title));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), Type_.color));
            }
            if (criteria.getImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg(), Type_.img));
            }
            if (criteria.getZikrId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getZikrId(), root -> root.join(Type_.zikrs, JoinType.LEFT).get(Zikr_.id))
                    );
            }
        }
        return specification;
    }
}
