package com.qualize.api.service;

import com.qualize.api.domain.Settlements;
import com.qualize.api.repository.SettlementsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Settlements}.
 */
@Service
@Transactional
public class SettlementsService {

    private final Logger log = LoggerFactory.getLogger(SettlementsService.class);

    private final SettlementsRepository settlementsRepository;

    public SettlementsService(SettlementsRepository settlementsRepository) {
        this.settlementsRepository = settlementsRepository;
    }

    /**
     * Save a settlements.
     *
     * @param settlements the entity to save.
     * @return the persisted entity.
     */
    public Settlements save(Settlements settlements) {
        log.debug("Request to save Settlements : {}", settlements);
        return settlementsRepository.save(settlements);
    }

    /**
     * Update a settlements.
     *
     * @param settlements the entity to save.
     * @return the persisted entity.
     */
    public Settlements update(Settlements settlements) {
        log.debug("Request to save Settlements : {}", settlements);
        return settlementsRepository.save(settlements);
    }

    /**
     * Partially update a settlements.
     *
     * @param settlements the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Settlements> partialUpdate(Settlements settlements) {
        log.debug("Request to partially update Settlements : {}", settlements);

        return settlementsRepository
            .findById(settlements.getId())
            .map(existingSettlements -> {
                if (settlements.getDescription() != null) {
                    existingSettlements.setDescription(settlements.getDescription());
                }
                if (settlements.getAmountYouOwe() != null) {
                    existingSettlements.setAmountYouOwe(settlements.getAmountYouOwe());
                }
                if (settlements.getSettlementStatus() != null) {
                    existingSettlements.setSettlementStatus(settlements.getSettlementStatus());
                }
                if (settlements.getCryptoCurrency() != null) {
                    existingSettlements.setCryptoCurrency(settlements.getCryptoCurrency());
                }
                if (settlements.getCurrencyValue() != null) {
                    existingSettlements.setCurrencyValue(settlements.getCurrencyValue());
                }
                if (settlements.getCryptoReceivablePayable() != null) {
                    existingSettlements.setCryptoReceivablePayable(settlements.getCryptoReceivablePayable());
                }
                if (settlements.getSortOrder() != null) {
                    existingSettlements.setSortOrder(settlements.getSortOrder());
                }
                if (settlements.getTransactionDate() != null) {
                    existingSettlements.setTransactionDate(settlements.getTransactionDate());
                }
                if (settlements.getDateModified() != null) {
                    existingSettlements.setDateModified(settlements.getDateModified());
                }

                return existingSettlements;
            })
            .map(settlementsRepository::save);
    }

    /**
     * Get all the settlements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Settlements> findAll(Pageable pageable) {
        log.debug("Request to get all Settlements");
        return settlementsRepository.findAll(pageable);
    }

    /**
     * Get one settlements by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Settlements> findOne(Long id) {
        log.debug("Request to get Settlements : {}", id);
        return settlementsRepository.findById(id);
    }

    /**
     * Delete the settlements by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Settlements : {}", id);
        settlementsRepository.deleteById(id);
    }
}
