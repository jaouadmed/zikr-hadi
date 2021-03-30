package com.hadi.zikr.web.rest;

import com.hadi.zikr.domain.Type;
import com.hadi.zikr.repository.TypeRepository;
import com.hadi.zikr.service.TypeQueryService;
import com.hadi.zikr.service.TypeService;
import com.hadi.zikr.service.criteria.TypeCriteria;
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
 * REST controller for managing {@link com.hadi.zikr.domain.Type}.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {

    private final Logger log = LoggerFactory.getLogger(TypeResource.class);

    private static final String ENTITY_NAME = "type";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeService typeService;

    private final TypeRepository typeRepository;

    private final TypeQueryService typeQueryService;

    public TypeResource(TypeService typeService, TypeRepository typeRepository, TypeQueryService typeQueryService) {
        this.typeService = typeService;
        this.typeRepository = typeRepository;
        this.typeQueryService = typeQueryService;
    }

    /**
     * {@code POST  /types} : Create a new type.
     *
     * @param type the type to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new type, or with status {@code 400 (Bad Request)} if the type has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/types")
    public ResponseEntity<Type> createType(@RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to save Type : {}", type);
        if (type.getId() != null) {
            throw new BadRequestAlertException("A new type cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Type result = typeService.save(type);
        return ResponseEntity
            .created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /types/:id} : Updates an existing type.
     *
     * @param id the id of the type to save.
     * @param type the type to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated type,
     * or with status {@code 400 (Bad Request)} if the type is not valid,
     * or with status {@code 500 (Internal Server Error)} if the type couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/types/{id}")
    public ResponseEntity<Type> updateType(@PathVariable(value = "id", required = false) final Long id, @RequestBody Type type)
        throws URISyntaxException {
        log.debug("REST request to update Type : {}, {}", id, type);
        if (type.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, type.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Type result = typeService.save(type);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, type.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /types/:id} : Partial updates given fields of an existing type, field will ignore if it is null
     *
     * @param id the id of the type to save.
     * @param type the type to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated type,
     * or with status {@code 400 (Bad Request)} if the type is not valid,
     * or with status {@code 404 (Not Found)} if the type is not found,
     * or with status {@code 500 (Internal Server Error)} if the type couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Type> partialUpdateType(@PathVariable(value = "id", required = false) final Long id, @RequestBody Type type)
        throws URISyntaxException {
        log.debug("REST request to partial update Type partially : {}, {}", id, type);
        if (type.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, type.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Type> result = typeService.partialUpdate(type);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, type.getId().toString())
        );
    }

    /**
     * {@code GET  /types} : get all the types.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of types in body.
     */
    @GetMapping("/types")
    public ResponseEntity<List<Type>> getAllTypes(TypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Types by criteria: {}", criteria);
        Page<Type> page = typeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /types/count} : count all the types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/types/count")
    public ResponseEntity<Long> countTypes(TypeCriteria criteria) {
        log.debug("REST request to count Types by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /types/:id} : get the "id" type.
     *
     * @param id the id of the type to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the type, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/types/{id}")
    public ResponseEntity<Type> getType(@PathVariable Long id) {
        log.debug("REST request to get Type : {}", id);
        Optional<Type> type = typeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(type);
    }

    /**
     * {@code DELETE  /types/:id} : delete the "id" type.
     *
     * @param id the id of the type to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.debug("REST request to delete Type : {}", id);
        typeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
