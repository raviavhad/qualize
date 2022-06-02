package com.qualize.api.service;

import com.qualize.api.domain.Accounts;
import com.qualize.api.repository.AccountsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Accounts}.
 */
@Service
@Transactional
public class AccountsService {

    private final Logger log = LoggerFactory.getLogger(AccountsService.class);

    private final AccountsRepository accountsRepository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    /**
     * Save a accounts.
     *
     * @param accounts the entity to save.
     * @return the persisted entity.
     */
    public Accounts save(Accounts accounts) {
        log.debug("Request to save Accounts : {}", accounts);
        return accountsRepository.save(accounts);
    }

    /**
     * Update a accounts.
     *
     * @param accounts the entity to save.
     * @return the persisted entity.
     */
    public Accounts update(Accounts accounts) {
        log.debug("Request to save Accounts : {}", accounts);
        return accountsRepository.save(accounts);
    }

    /**
     * Partially update a accounts.
     *
     * @param accounts the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Accounts> partialUpdate(Accounts accounts) {
        log.debug("Request to partially update Accounts : {}", accounts);

        return accountsRepository
            .findById(accounts.getId())
            .map(existingAccounts -> {
                if (accounts.getAmountYouOwe() != null) {
                    existingAccounts.setAmountYouOwe(accounts.getAmountYouOwe());
                }
                if (accounts.getAmountFriendOwes() != null) {
                    existingAccounts.setAmountFriendOwes(accounts.getAmountFriendOwes());
                }
                if (accounts.getNetReceivablePayable() != null) {
                    existingAccounts.setNetReceivablePayable(accounts.getNetReceivablePayable());
                }
                if (accounts.getCryptoCurrency() != null) {
                    existingAccounts.setCryptoCurrency(accounts.getCryptoCurrency());
                }
                if (accounts.getCurrencyValue() != null) {
                    existingAccounts.setCurrencyValue(accounts.getCurrencyValue());
                }
                if (accounts.getCryptoReceivablePayable() != null) {
                    existingAccounts.setCryptoReceivablePayable(accounts.getCryptoReceivablePayable());
                }
                if (accounts.getSortOrder() != null) {
                    existingAccounts.setSortOrder(accounts.getSortOrder());
                }
                if (accounts.getDateAdded() != null) {
                    existingAccounts.setDateAdded(accounts.getDateAdded());
                }
                if (accounts.getDateModified() != null) {
                    existingAccounts.setDateModified(accounts.getDateModified());
                }
                if (accounts.getAccountStatus() != null) {
                    existingAccounts.setAccountStatus(accounts.getAccountStatus());
                }

                return existingAccounts;
            })
            .map(accountsRepository::save);
    }

    /**
     * Get all the accounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Accounts> findAll(Pageable pageable) {
        log.debug("Request to get all Accounts");
        return accountsRepository.findAll(pageable);
    }

    /**
     * Get one accounts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Accounts> findOne(Long id) {
        log.debug("Request to get Accounts : {}", id);
        return accountsRepository.findById(id);
    }

    /**
     * Delete the accounts by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Accounts : {}", id);
        accountsRepository.deleteById(id);
    }
}
