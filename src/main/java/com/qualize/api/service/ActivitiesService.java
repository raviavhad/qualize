package com.qualize.api.service;

import com.qualize.api.domain.Activities;
import com.qualize.api.repository.ActivitiesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Activities}.
 */
@Service
@Transactional
public class ActivitiesService {

    private final Logger log = LoggerFactory.getLogger(ActivitiesService.class);

    private final ActivitiesRepository activitiesRepository;

    public ActivitiesService(ActivitiesRepository activitiesRepository) {
        this.activitiesRepository = activitiesRepository;
    }

    /**
     * Save a activities.
     *
     * @param activities the entity to save.
     * @return the persisted entity.
     */
    public Activities save(Activities activities) {
        log.debug("Request to save Activities : {}", activities);
        return activitiesRepository.save(activities);
    }

    /**
     * Update a activities.
     *
     * @param activities the entity to save.
     * @return the persisted entity.
     */
    public Activities update(Activities activities) {
        log.debug("Request to save Activities : {}", activities);
        return activitiesRepository.save(activities);
    }

    /**
     * Partially update a activities.
     *
     * @param activities the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Activities> partialUpdate(Activities activities) {
        log.debug("Request to partially update Activities : {}", activities);

        return activitiesRepository
            .findById(activities.getId())
            .map(existingActivities -> {
                if (activities.getName() != null) {
                    existingActivities.setName(activities.getName());
                }
                if (activities.getDate() != null) {
                    existingActivities.setDate(activities.getDate());
                }
                if (activities.getType() != null) {
                    existingActivities.setType(activities.getType());
                }
                if (activities.getSortOrder() != null) {
                    existingActivities.setSortOrder(activities.getSortOrder());
                }
                if (activities.getDateAdded() != null) {
                    existingActivities.setDateAdded(activities.getDateAdded());
                }
                if (activities.getDateModified() != null) {
                    existingActivities.setDateModified(activities.getDateModified());
                }
                if (activities.getStatus() != null) {
                    existingActivities.setStatus(activities.getStatus());
                }

                return existingActivities;
            })
            .map(activitiesRepository::save);
    }

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Activities> findAll(Pageable pageable) {
        log.debug("Request to get all Activities");
        return activitiesRepository.findAll(pageable);
    }

    /**
     * Get one activities by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Activities> findOne(Long id) {
        log.debug("Request to get Activities : {}", id);
        return activitiesRepository.findById(id);
    }

    /**
     * Delete the activities by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Activities : {}", id);
        activitiesRepository.deleteById(id);
    }
}
