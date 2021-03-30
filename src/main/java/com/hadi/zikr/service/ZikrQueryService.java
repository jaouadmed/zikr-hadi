package com.hadi.zikr.service;

import com.hadi.zikr.domain.*; // for static metamodels
import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.ZikrRepository;
import com.hadi.zikr.service.criteria.ZikrCriteria;
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
 * Service for executing complex queries for {@link Zikr} entities in the database.
 * The main input is a {@link ZikrCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Zikr} or a {@link Page} of {@link Zikr} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ZikrQueryService extends QueryService<Zikr> {

    private final Logger log = LoggerFactory.getLogger(ZikrQueryService.class);

    private final ZikrRepository zikrRepository;

    public ZikrQueryService(ZikrRepository zikrRepository) {
        this.zikrRepository = zikrRepository;
    }

    /**
     * Return a {@link List} of {@link Zikr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Zikr> findByCriteria(ZikrCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Zikr> specification = createSpecification(criteria);
        return zikrRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Zikr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Zikr> findByCriteria(ZikrCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Zikr> specification = createSpecification(criteria);
        return zikrRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ZikrCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Zikr> specification = createSpecification(criteria);
        return zikrRepository.count(specification);
    }

    /**
     * Function to convert {@link ZikrCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Zikr> createSpecification(ZikrCriteria criteria) {
        Specification<Zikr> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Zikr_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Zikr_.content));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), Zikr_.count));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTypeId(), root -> root.join(Zikr_.type, JoinType.LEFT).get(Type_.id)));
            }
        }
        return specification;
    }
}
