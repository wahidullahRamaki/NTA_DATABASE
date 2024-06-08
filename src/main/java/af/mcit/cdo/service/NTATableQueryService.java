package af.mcit.cdo.service;

import af.mcit.cdo.domain.*; // for static metamodels
import af.mcit.cdo.domain.NTATable;
import af.mcit.cdo.repository.NTATableRepository;
import af.mcit.cdo.service.criteria.NTATableCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link NTATable} entities in the database.
 * The main input is a {@link NTATableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NTATable} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NTATableQueryService extends QueryService<NTATable> {

    private final Logger log = LoggerFactory.getLogger(NTATableQueryService.class);

    private final NTATableRepository nTATableRepository;

    public NTATableQueryService(NTATableRepository nTATableRepository) {
        this.nTATableRepository = nTATableRepository;
    }

    /**
     * Return a {@link Page} of {@link NTATable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NTATable> findByCriteria(NTATableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NTATable> specification = createSpecification(criteria);
        return nTATableRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NTATableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NTATable> specification = createSpecification(criteria);
        return nTATableRepository.count(specification);
    }

    /**
     * Function to convert {@link NTATableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NTATable> createSpecification(NTATableCriteria criteria) {
        Specification<NTATable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NTATable_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), NTATable_.fullName));
            }
            if (criteria.getFatherName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFatherName(), NTATable_.fatherName));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), NTATable_.jobTitle));
            }
            if (criteria.getStep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStep(), NTATable_.step));
            }
            if (criteria.getEducationDegree() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEducationDegree(), NTATable_.educationDegree));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), NTATable_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), NTATable_.endDate));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), NTATable_.salary));
            }
            if (criteria.getSignature() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSignature(), NTATable_.signature));
            }
        }
        return specification;
    }
}
