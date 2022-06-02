package com.qualize.api.service;

import com.qualize.api.domain.Expenses;
import com.qualize.api.repository.ExpensesRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Expenses}.
 */
@Service
@Transactional
public class ExpensesService {

    private final Logger log = LoggerFactory.getLogger(ExpensesService.class);

    private final ExpensesRepository expensesRepository;

    public ExpensesService(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }

    /**
     * Save a expenses.
     *
     * @param expenses the entity to save.
     * @return the persisted entity.
     */
    public Expenses save(Expenses expenses) {
        log.debug("Request to save Expenses : {}", expenses);
        return expensesRepository.save(expenses);
    }

    /**
     * Update a expenses.
     *
     * @param expenses the entity to save.
     * @return the persisted entity.
     */
    public Expenses update(Expenses expenses) {
        log.debug("Request to save Expenses : {}", expenses);
        return expensesRepository.save(expenses);
    }

    /**
     * Partially update a expenses.
     *
     * @param expenses the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Expenses> partialUpdate(Expenses expenses) {
        log.debug("Request to partially update Expenses : {}", expenses);

        return expensesRepository
            .findById(expenses.getId())
            .map(existingExpenses -> {
                if (expenses.getDescription() != null) {
                    existingExpenses.setDescription(expenses.getDescription());
                }
                if (expenses.getPaidBy() != null) {
                    existingExpenses.setPaidBy(expenses.getPaidBy());
                }
                if (expenses.getAmount() != null) {
                    existingExpenses.setAmount(expenses.getAmount());
                }
                if (expenses.getCryptoCurrency() != null) {
                    existingExpenses.setCryptoCurrency(expenses.getCryptoCurrency());
                }
                if (expenses.getSortOrder() != null) {
                    existingExpenses.setSortOrder(expenses.getSortOrder());
                }
                if (expenses.getDateAdded() != null) {
                    existingExpenses.setDateAdded(expenses.getDateAdded());
                }
                if (expenses.getDateModified() != null) {
                    existingExpenses.setDateModified(expenses.getDateModified());
                }

                return existingExpenses;
            })
            .map(expensesRepository::save);
    }

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Expenses> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expensesRepository.findAll(pageable);
    }

    /**
     *  Get all the expenses where GroupName is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Expenses> findAllWhereGroupNameIsNull() {
        log.debug("Request to get all expenses where GroupName is null");
        return StreamSupport
            .stream(expensesRepository.findAll().spliterator(), false)
            .filter(expenses -> expenses.getGroupName() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the expenses where Activities is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Expenses> findAllWhereActivitiesIsNull() {
        log.debug("Request to get all expenses where Activities is null");
        return StreamSupport
            .stream(expensesRepository.findAll().spliterator(), false)
            .filter(expenses -> expenses.getActivities() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one expenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Expenses> findOne(Long id) {
        log.debug("Request to get Expenses : {}", id);
        return expensesRepository.findById(id);
    }

    /**
     * Delete the expenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expenses : {}", id);
        expensesRepository.deleteById(id);
    }
}
