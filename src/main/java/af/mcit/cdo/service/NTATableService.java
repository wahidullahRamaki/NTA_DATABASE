package af.mcit.cdo.service;

import af.mcit.cdo.domain.NTATable;
import af.mcit.cdo.repository.NTATableRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link af.mcit.cdo.domain.NTATable}.
 */
@Service
@Transactional
public class NTATableService {

    private final Logger log = LoggerFactory.getLogger(NTATableService.class);

    private final NTATableRepository nTATableRepository;

    public NTATableService(NTATableRepository nTATableRepository) {
        this.nTATableRepository = nTATableRepository;
    }

    /**
     * Save a nTATable.
     *
     * @param nTATable the entity to save.
     * @return the persisted entity.
     */
    public NTATable save(NTATable nTATable) {
        log.debug("Request to save NTATable : {}", nTATable);
        return nTATableRepository.save(nTATable);
    }

    /**
     * Update a nTATable.
     *
     * @param nTATable the entity to save.
     * @return the persisted entity.
     */
    public NTATable update(NTATable nTATable) {
        log.debug("Request to update NTATable : {}", nTATable);
        return nTATableRepository.save(nTATable);
    }

    /**
     * Partially update a nTATable.
     *
     * @param nTATable the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NTATable> partialUpdate(NTATable nTATable) {
        log.debug("Request to partially update NTATable : {}", nTATable);

        return nTATableRepository
            .findById(nTATable.getId())
            .map(existingNTATable -> {
                if (nTATable.getFullName() != null) {
                    existingNTATable.setFullName(nTATable.getFullName());
                }
                if (nTATable.getFatherName() != null) {
                    existingNTATable.setFatherName(nTATable.getFatherName());
                }
                if (nTATable.getJobTitle() != null) {
                    existingNTATable.setJobTitle(nTATable.getJobTitle());
                }
                if (nTATable.getStep() != null) {
                    existingNTATable.setStep(nTATable.getStep());
                }
                if (nTATable.getEducationDegree() != null) {
                    existingNTATable.setEducationDegree(nTATable.getEducationDegree());
                }
                if (nTATable.getStartDate() != null) {
                    existingNTATable.setStartDate(nTATable.getStartDate());
                }
                if (nTATable.getEndDate() != null) {
                    existingNTATable.setEndDate(nTATable.getEndDate());
                }
                if (nTATable.getSalary() != null) {
                    existingNTATable.setSalary(nTATable.getSalary());
                }
                if (nTATable.getSignature() != null) {
                    existingNTATable.setSignature(nTATable.getSignature());
                }

                return existingNTATable;
            })
            .map(nTATableRepository::save);
    }

    /**
     * Get one nTATable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NTATable> findOne(Long id) {
        log.debug("Request to get NTATable : {}", id);
        return nTATableRepository.findById(id);
    }

    /**
     * Delete the nTATable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NTATable : {}", id);
        nTATableRepository.deleteById(id);
    }
}
