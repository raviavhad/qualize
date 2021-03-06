package com.qualize.api.web.rest;

import com.qualize.api.domain.Activities;
import com.qualize.api.repository.ActivitiesRepository;
import com.qualize.api.service.ActivitiesService;
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
 * REST controller for managing {@link com.qualize.api.domain.Activities}.
 */
@RestController
@RequestMapping("/api")
public class ActivitiesResource {

    private final Logger log = LoggerFactory.getLogger(ActivitiesResource.class);

    private static final String ENTITY_NAME = "activities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivitiesService activitiesService;

    private final ActivitiesRepository activitiesRepository;

    public ActivitiesResource(ActivitiesService activitiesService, ActivitiesRepository activitiesRepository) {
        this.activitiesService = activitiesService;
        this.activitiesRepository = activitiesRepository;
    }

    /**
     * {@code POST  /activities} : Create a new activities.
     *
     * @param activities the activities to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activities, or with status {@code 400 (Bad Request)} if the activities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activities")
    public ResponseEntity<Activities> createActivities(@Valid @RequestBody Activities activities) throws URISyntaxException {
        log.debug("REST request to save Activities : {}", activities);
        if (activities.getId() != null) {
            throw new BadRequestAlertException("A new activities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Activities result = activitiesService.save(activities);
        return ResponseEntity
            .created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activities/:id} : Updates an existing activities.
     *
     * @param id the id of the activities to save.
     * @param activities the activities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activities,
     * or with status {@code 400 (Bad Request)} if the activities is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activities/{id}")
    public ResponseEntity<Activities> updateActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Activities activities
    ) throws URISyntaxException {
        log.debug("REST request to update Activities : {}, {}", id, activities);
        if (activities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Activities result = activitiesService.update(activities);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activities.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /activities/:id} : Partial updates given fields of an existing activities, field will ignore if it is null
     *
     * @param id the id of the activities to save.
     * @param activities the activities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activities,
     * or with status {@code 400 (Bad Request)} if the activities is not valid,
     * or with status {@code 404 (Not Found)} if the activities is not found,
     * or with status {@code 500 (Internal Server Error)} if the activities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/activities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activities> partialUpdateActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Activities activities
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activities partially : {}, {}", id, activities);
        if (activities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activities> result = activitiesService.partialUpdate(activities);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activities.getId().toString())
        );
    }

    /**
     * {@code GET  /activities} : get all the activities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activities in body.
     */
    @GetMapping("/activities")
    public ResponseEntity<List<Activities>> getAllActivities(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Activities");
        Page<Activities> page = activitiesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activities/:id} : get the "id" activities.
     *
     * @param id the id of the activities to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activities, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activities/{id}")
    public ResponseEntity<Activities> getActivities(@PathVariable Long id) {
        log.debug("REST request to get Activities : {}", id);
        Optional<Activities> activities = activitiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activities);
    }

    /**
     * {@code DELETE  /activities/:id} : delete the "id" activities.
     *
     * @param id the id of the activities to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activities/{id}")
    public ResponseEntity<Void> deleteActivities(@PathVariable Long id) {
        log.debug("REST request to delete Activities : {}", id);
        activitiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
