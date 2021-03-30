package com.hadi.zikr.web.rest;

import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.ZikrRepository;
import com.hadi.zikr.service.ZikrQueryService;
import com.hadi.zikr.service.ZikrService;
import com.hadi.zikr.service.criteria.ZikrCriteria;
import com.hadi.zikr.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hadi.zikr.domain.Zikr}.
 */
@RestController
@RequestMapping("/api")
public class ZikrResource {

    private final Logger log = LoggerFactory.getLogger(ZikrResource.class);

    private static final String ENTITY_NAME = "zikr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZikrService zikrService;

    private final ZikrRepository zikrRepository;

    private final ZikrQueryService zikrQueryService;

    public ZikrResource(ZikrService zikrService, ZikrRepository zikrRepository, ZikrQueryService zikrQueryService) {
        this.zikrService = zikrService;
        this.zikrRepository = zikrRepository;
        this.zikrQueryService = zikrQueryService;
    }

    /**
     * {@code POST  /zikrs} : Create a new zikr.
     *
     * @param zikr the zikr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zikr, or with status {@code 400 (Bad Request)} if the zikr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zikrs")
    public ResponseEntity<Zikr> createZikr(@RequestBody Zikr zikr) throws URISyntaxException {
        log.debug("REST request to save Zikr : {}", zikr);
        if (zikr.getId() != null) {
            throw new BadRequestAlertException("A new zikr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Zikr result = zikrService.save(zikr);
        return ResponseEntity
            .created(new URI("/api/zikrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zikrs/:id} : Updates an existing zikr.
     *
     * @param id the id of the zikr to save.
     * @param zikr the zikr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zikr,
     * or with status {@code 400 (Bad Request)} if the zikr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zikr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zikrs/{id}")
    public ResponseEntity<Zikr> updateZikr(@PathVariable(value = "id", required = false) final Long id, @RequestBody Zikr zikr)
        throws URISyntaxException {
        log.debug("REST request to update Zikr : {}, {}", id, zikr);
        if (zikr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zikr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zikrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Zikr result = zikrService.save(zikr);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zikr.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /zikrs/:id} : Partial updates given fields of an existing zikr, field will ignore if it is null
     *
     * @param id the id of the zikr to save.
     * @param zikr the zikr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zikr,
     * or with status {@code 400 (Bad Request)} if the zikr is not valid,
     * or with status {@code 404 (Not Found)} if the zikr is not found,
     * or with status {@code 500 (Internal Server Error)} if the zikr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/zikrs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Zikr> partialUpdateZikr(@PathVariable(value = "id", required = false) final Long id, @RequestBody Zikr zikr)
        throws URISyntaxException {
        log.debug("REST request to partial update Zikr partially : {}, {}", id, zikr);
        if (zikr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zikr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zikrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Zikr> result = zikrService.partialUpdate(zikr);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zikr.getId().toString())
        );
    }

    /**
     * {@code GET  /zikrs} : get all the zikrs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zikrs in body.
     */
    @GetMapping("/zikrs")
    public ResponseEntity<List<Zikr>> getAllZikrs(ZikrCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Zikrs by criteria: {}", criteria);
        Page<Zikr> page = zikrQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /zikrs/count} : count all the zikrs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/zikrs/count")
    public ResponseEntity<Long> countZikrs(ZikrCriteria criteria) {
        log.debug("REST request to count Zikrs by criteria: {}", criteria);
        return ResponseEntity.ok().body(zikrQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /zikrs/:id} : get the "id" zikr.
     *
     * @param id the id of the zikr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zikr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zikrs/{id}")
    public ResponseEntity<Zikr> getZikr(@PathVariable Long id) {
        log.debug("REST request to get Zikr : {}", id);
        Optional<Zikr> zikr = zikrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zikr);
    }

    /**
     * {@code DELETE  /zikrs/:id} : delete the "id" zikr.
     *
     * @param id the id of the zikr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zikrs/{id}")
    public ResponseEntity<Void> deleteZikr(@PathVariable Long id) {
        log.debug("REST request to delete Zikr : {}", id);
        zikrService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
