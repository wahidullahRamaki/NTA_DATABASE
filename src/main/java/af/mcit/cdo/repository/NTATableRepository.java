package af.mcit.cdo.repository;

import af.mcit.cdo.domain.NTATable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NTATable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NTATableRepository extends JpaRepository<NTATable, Long>, JpaSpecificationExecutor<NTATable> {}
