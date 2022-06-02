package com.qualize.api.service;

import com.qualize.api.domain.Groups;
import com.qualize.api.repository.GroupsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Groups}.
 */
@Service
@Transactional
public class GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsService.class);

    private final GroupsRepository groupsRepository;

    public GroupsService(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    /**
     * Save a groups.
     *
     * @param groups the entity to save.
     * @return the persisted entity.
     */
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        return groupsRepository.save(groups);
    }

    /**
     * Update a groups.
     *
     * @param groups the entity to save.
     * @return the persisted entity.
     */
    public Groups update(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        return groupsRepository.save(groups);
    }

    /**
     * Partially update a groups.
     *
     * @param groups the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Groups> partialUpdate(Groups groups) {
        log.debug("Request to partially update Groups : {}", groups);

        return groupsRepository
            .findById(groups.getId())
            .map(existingGroups -> {
                if (groups.getName() != null) {
                    existingGroups.setName(groups.getName());
                }
                if (groups.getSortOrder() != null) {
                    existingGroups.setSortOrder(groups.getSortOrder());
                }
                if (groups.getDateAdded() != null) {
                    existingGroups.setDateAdded(groups.getDateAdded());
                }
                if (groups.getDateModified() != null) {
                    existingGroups.setDateModified(groups.getDateModified());
                }

                return existingGroups;
            })
            .map(groupsRepository::save);
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Groups> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll(pageable);
    }

    /**
     * Get one groups by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Groups> findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        return groupsRepository.findById(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.deleteById(id);
    }
}
