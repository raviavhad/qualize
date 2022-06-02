package com.qualize.api.web.rest;

import com.qualize.api.domain.CryptoTracker;
import com.qualize.api.repository.CryptoTrackerRepository;
import com.qualize.api.service.CryptoTrackerService;
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
 * REST controller for managing {@link com.qualize.api.domain.CryptoTracker}.
 */
@RestController
@RequestMapping("/api")
public class CryptoTrackerResource {

    private final Logger log = LoggerFactory.getLogger(CryptoTrackerResource.class);

    private static final String ENTITY_NAME = "cryptoTracker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CryptoTrackerService cryptoTrackerService;

    private final CryptoTrackerRepository cryptoTrackerRepository;

    public CryptoTrackerResource(CryptoTrackerService cryptoTrackerService, CryptoTrackerRepository cryptoTrackerRepository) {
        this.cryptoTrackerService = cryptoTrackerService;
        this.cryptoTrackerRepository = cryptoTrackerRepository;
    }

    /**
     * {@code POST  /crypto-trackers} : Create a new cryptoTracker.
     *
     * @param cryptoTracker the cryptoTracker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cryptoTracker, or with status {@code 400 (Bad Request)} if the cryptoTracker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crypto-trackers")
    public ResponseEntity<CryptoTracker> createCryptoTracker(@Valid @RequestBody CryptoTracker cryptoTracker) throws URISyntaxException {
        log.debug("REST request to save CryptoTracker : {}", cryptoTracker);
        if (cryptoTracker.getId() != null) {
            throw new BadRequestAlertException("A new cryptoTracker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CryptoTracker result = cryptoTrackerService.save(cryptoTracker);
        return ResponseEntity
            .created(new URI("/api/crypto-trackers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crypto-trackers/:id} : Updates an existing cryptoTracker.
     *
     * @param id the id of the cryptoTracker to save.
     * @param cryptoTracker the cryptoTracker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cryptoTracker,
     * or with status {@code 400 (Bad Request)} if the cryptoTracker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cryptoTracker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crypto-trackers/{id}")
    public ResponseEntity<CryptoTracker> updateCryptoTracker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CryptoTracker cryptoTracker
    ) throws URISyntaxException {
        log.debug("REST request to update CryptoTracker : {}, {}", id, cryptoTracker);
        if (cryptoTracker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cryptoTracker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cryptoTrackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CryptoTracker result = cryptoTrackerService.update(cryptoTracker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cryptoTracker.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crypto-trackers/:id} : Partial updates given fields of an existing cryptoTracker, field will ignore if it is null
     *
     * @param id the id of the cryptoTracker to save.
     * @param cryptoTracker the cryptoTracker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cryptoTracker,
     * or with status {@code 400 (Bad Request)} if the cryptoTracker is not valid,
     * or with status {@code 404 (Not Found)} if the cryptoTracker is not found,
     * or with status {@code 500 (Internal Server Error)} if the cryptoTracker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crypto-trackers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CryptoTracker> partialUpdateCryptoTracker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CryptoTracker cryptoTracker
    ) throws URISyntaxException {
        log.debug("REST request to partial update CryptoTracker partially : {}, {}", id, cryptoTracker);
        if (cryptoTracker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cryptoTracker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cryptoTrackerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CryptoTracker> result = cryptoTrackerService.partialUpdate(cryptoTracker);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cryptoTracker.getId().toString())
        );
    }

    /**
     * {@code GET  /crypto-trackers} : get all the cryptoTrackers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cryptoTrackers in body.
     */
    @GetMapping("/crypto-trackers")
    public ResponseEntity<List<CryptoTracker>> getAllCryptoTrackers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CryptoTrackers");
        Page<CryptoTracker> page = cryptoTrackerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crypto-trackers/:id} : get the "id" cryptoTracker.
     *
     * @param id the id of the cryptoTracker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cryptoTracker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crypto-trackers/{id}")
    public ResponseEntity<CryptoTracker> getCryptoTracker(@PathVariable Long id) {
        log.debug("REST request to get CryptoTracker : {}", id);
        Optional<CryptoTracker> cryptoTracker = cryptoTrackerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cryptoTracker);
    }

    /**
     * {@code DELETE  /crypto-trackers/:id} : delete the "id" cryptoTracker.
     *
     * @param id the id of the cryptoTracker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crypto-trackers/{id}")
    public ResponseEntity<Void> deleteCryptoTracker(@PathVariable Long id) {
        log.debug("REST request to delete CryptoTracker : {}", id);
        cryptoTrackerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
