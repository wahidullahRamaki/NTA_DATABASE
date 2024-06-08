package af.mcit.cdo.web.rest;

import af.mcit.cdo.domain.NTATable;
import af.mcit.cdo.repository.NTATableRepository;
import af.mcit.cdo.service.NTATableQueryService;
import af.mcit.cdo.service.NTATableService;
import af.mcit.cdo.service.criteria.NTATableCriteria;
import af.mcit.cdo.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link af.mcit.cdo.domain.NTATable}.
 */
@RestController
@RequestMapping("/api/nta-tables")
public class NTATableResource {

    private final Logger log = LoggerFactory.getLogger(NTATableResource.class);

    private static final String ENTITY_NAME = "nTATable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NTATableService nTATableService;

    private final NTATableRepository nTATableRepository;

    private final NTATableQueryService nTATableQueryService;

    public NTATableResource(
        NTATableService nTATableService,
        NTATableRepository nTATableRepository,
        NTATableQueryService nTATableQueryService
    ) {
        this.nTATableService = nTATableService;
        this.nTATableRepository = nTATableRepository;
        this.nTATableQueryService = nTATableQueryService;
    }

    /**
     * {@code POST  /nta-tables} : Create a new nTATable.
     *
     * @param nTATable the nTATable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nTATable, or with status {@code 400 (Bad Request)} if the nTATable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NTATable> createNTATable(@Valid @RequestBody NTATable nTATable) throws URISyntaxException {
        log.debug("REST request to save NTATable : {}", nTATable);
        if (nTATable.getId() != null) {
            throw new BadRequestAlertException("A new nTATable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nTATable = nTATableService.save(nTATable);
        return ResponseEntity.created(new URI("/api/nta-tables/" + nTATable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nTATable.getId().toString()))
            .body(nTATable);
    }

    /**
     * {@code PUT  /nta-tables/:id} : Updates an existing nTATable.
     *
     * @param id the id of the nTATable to save.
     * @param nTATable the nTATable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nTATable,
     * or with status {@code 400 (Bad Request)} if the nTATable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nTATable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NTATable> updateNTATable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NTATable nTATable
    ) throws URISyntaxException {
        log.debug("REST request to update NTATable : {}, {}", id, nTATable);
        if (nTATable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nTATable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nTATableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nTATable = nTATableService.update(nTATable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nTATable.getId().toString()))
            .body(nTATable);
    }

    /**
     * {@code PATCH  /nta-tables/:id} : Partial updates given fields of an existing nTATable, field will ignore if it is null
     *
     * @param id the id of the nTATable to save.
     * @param nTATable the nTATable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nTATable,
     * or with status {@code 400 (Bad Request)} if the nTATable is not valid,
     * or with status {@code 404 (Not Found)} if the nTATable is not found,
     * or with status {@code 500 (Internal Server Error)} if the nTATable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NTATable> partialUpdateNTATable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NTATable nTATable
    ) throws URISyntaxException {
        log.debug("REST request to partial update NTATable partially : {}, {}", id, nTATable);
        if (nTATable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nTATable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nTATableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NTATable> result = nTATableService.partialUpdate(nTATable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nTATable.getId().toString())
        );
    }

    /**
     * {@code GET  /nta-tables} : get all the nTATables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nTATables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NTATable>> getAllNTATables(
        NTATableCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get NTATables by criteria: {}", criteria);

        Page<NTATable> page = nTATableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nta-tables/count} : count all the nTATables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNTATables(NTATableCriteria criteria) {
        log.debug("REST request to count NTATables by criteria: {}", criteria);
        return ResponseEntity.ok().body(nTATableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nta-tables/:id} : get the "id" nTATable.
     *
     * @param id the id of the nTATable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nTATable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NTATable> getNTATable(@PathVariable("id") Long id) {
        log.debug("REST request to get NTATable : {}", id);
        Optional<NTATable> nTATable = nTATableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nTATable);
    }

    /**
     * {@code DELETE  /nta-tables/:id} : delete the "id" nTATable.
     *
     * @param id the id of the nTATable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNTATable(@PathVariable("id") Long id) {
        log.debug("REST request to delete NTATable : {}", id);
        nTATableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
