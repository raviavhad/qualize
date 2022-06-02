package com.qualize.api.service;

import com.qualize.api.domain.CryptoTracker;
import com.qualize.api.repository.CryptoTrackerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CryptoTracker}.
 */
@Service
@Transactional
public class CryptoTrackerService {

    private final Logger log = LoggerFactory.getLogger(CryptoTrackerService.class);

    private final CryptoTrackerRepository cryptoTrackerRepository;

    public CryptoTrackerService(CryptoTrackerRepository cryptoTrackerRepository) {
        this.cryptoTrackerRepository = cryptoTrackerRepository;
    }

    /**
     * Save a cryptoTracker.
     *
     * @param cryptoTracker the entity to save.
     * @return the persisted entity.
     */
    public CryptoTracker save(CryptoTracker cryptoTracker) {
        log.debug("Request to save CryptoTracker : {}", cryptoTracker);
        return cryptoTrackerRepository.save(cryptoTracker);
    }

    /**
     * Update a cryptoTracker.
     *
     * @param cryptoTracker the entity to save.
     * @return the persisted entity.
     */
    public CryptoTracker update(CryptoTracker cryptoTracker) {
        log.debug("Request to save CryptoTracker : {}", cryptoTracker);
        return cryptoTrackerRepository.save(cryptoTracker);
    }

    /**
     * Partially update a cryptoTracker.
     *
     * @param cryptoTracker the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CryptoTracker> partialUpdate(CryptoTracker cryptoTracker) {
        log.debug("Request to partially update CryptoTracker : {}", cryptoTracker);

        return cryptoTrackerRepository
            .findById(cryptoTracker.getId())
            .map(existingCryptoTracker -> {
                if (cryptoTracker.getCurrency() != null) {
                    existingCryptoTracker.setCurrency(cryptoTracker.getCurrency());
                }
                if (cryptoTracker.getValue() != null) {
                    existingCryptoTracker.setValue(cryptoTracker.getValue());
                }
                if (cryptoTracker.getFeedDateTime() != null) {
                    existingCryptoTracker.setFeedDateTime(cryptoTracker.getFeedDateTime());
                }
                if (cryptoTracker.getDateAdded() != null) {
                    existingCryptoTracker.setDateAdded(cryptoTracker.getDateAdded());
                }
                if (cryptoTracker.getDateModified() != null) {
                    existingCryptoTracker.setDateModified(cryptoTracker.getDateModified());
                }

                return existingCryptoTracker;
            })
            .map(cryptoTrackerRepository::save);
    }

    /**
     * Get all the cryptoTrackers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CryptoTracker> findAll(Pageable pageable) {
        log.debug("Request to get all CryptoTrackers");
        return cryptoTrackerRepository.findAll(pageable);
    }

    /**
     * Get one cryptoTracker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CryptoTracker> findOne(Long id) {
        log.debug("Request to get CryptoTracker : {}", id);
        return cryptoTrackerRepository.findById(id);
    }

    /**
     * Delete the cryptoTracker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CryptoTracker : {}", id);
        cryptoTrackerRepository.deleteById(id);
    }
}
