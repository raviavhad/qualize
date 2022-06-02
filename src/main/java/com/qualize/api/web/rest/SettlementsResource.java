package com.qualize.api.web.rest;

import com.qualize.api.domain.Settlements;
import com.qualize.api.repository.SettlementsRepository;
import com.qualize.api.service.SettlementsService;
import com.qualize.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.qualize.api.domain.Settlements}.
 */
@RestController
@RequestMapping("/api")
public class SettlementsResource {

    private final Logger log = LoggerFactory.getLogger(SettlementsResource.class);

    private static final String ENTITY_NAME = "settlements";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettlementsService settlementsService;

    private final SettlementsRepository settlementsRepository;

    public SettlementsResource(SettlementsService settlementsService, SettlementsRepository settlementsRepository) {
        this.settlementsService = settlementsService;
        this.settlementsRepository = settlementsRepository;
    }

    /**
     * {@code POST  /settlements} : Create a new settlements.
     *
     * @param settlements the settlements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settlements, or with status {@code 400 (Bad Request)} if the settlements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settlements")
    public ResponseEntity<Settlements> createSettlements(@Valid @RequestBody Settlements settlements) throws URISyntaxException {
        log.debug("REST request to save Settlements : {}", settlements);
        if (settlements.getId() != null) {
            throw new BadRequestAlertException("A new settlements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Settlements result = settlementsService.save(settlements);
        return ResponseEntity
            .created(new URI("/api/settlements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settlements/:id} : Updates an existing settlements.
     *
     * @param id the id of the settlements to save.
     * @param settlements the settlements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlements,
     * or with status {@code 400 (Bad Request)} if the settlements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settlements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settlements/{id}")
    public ResponseEntity<Settlements> updateSettlements(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Settlements settlements
    ) throws URISyntaxException {
        log.debug("REST request to update Settlements : {}, {}", id, settlements);
        if (settlements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Settlements result = settlementsService.update(settlements);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlements.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settlements/:id} : Partial updates given fields of an existing settlements, field will ignore if it is null
     *
     * @param id the id of the settlements to save.
     * @param settlements the settlements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlements,
     * or with status {@code 400 (Bad Request)} if the settlements is not valid,
     * or with status {@code 404 (Not Found)} if the settlements is not found,
     * or with status {@code 500 (Internal Server Error)} if the settlements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settlements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Settlements> partialUpdateSettlements(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Settlements settlements
    ) throws URISyntaxException {
        log.debug("REST request to partial update Settlements partially : {}, {}", id, settlements);
        if (settlements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Settlements> result = settlementsService.partialUpdate(settlements);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlements.getId().toString())
        );
    }

    /**
     * {@code GET  /settlements} : get all the settlements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settlements in body.
     */
    @GetMapping("/settlements")
    public ResponseEntity<List<Settlements>> getAllSettlements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Settlements");
        Page<Settlements> page = settlementsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settlements/:id} : get the "id" settlements.
     *
     * @param id the id of the settlements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settlements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settlements/{id}")
    public ResponseEntity<Settlements> getSettlements(@PathVariable Long id) {
        log.debug("REST request to get Settlements : {}", id);
        Optional<Settlements> settlements = settlementsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settlements);
    }

    /**
     * {@code DELETE  /settlements/:id} : delete the "id" settlements.
     *
     * @param id the id of the settlements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/settlements/{id}")
    public ResponseEntity<Void> deleteSettlements(@PathVariable Long id) {
        log.debug("REST request to delete Settlements : {}", id);
        settlementsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
