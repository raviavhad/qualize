package com.qualize.api.web.rest;

import static com.qualize.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.Expenses;
import com.qualize.api.repository.ExpensesRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExpensesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpensesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAID_BY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID_BY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CRYPTO_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CRYPTO_CURRENCY = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpensesMockMvc;

    private Expenses expenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expenses createEntity(EntityManager em) {
        Expenses expenses = new Expenses()
            .description(DEFAULT_DESCRIPTION)
            .paidBy(DEFAULT_PAID_BY)
            .amount(DEFAULT_AMOUNT)
            .cryptoCurrency(DEFAULT_CRYPTO_CURRENCY)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return expenses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expenses createUpdatedEntity(EntityManager em) {
        Expenses expenses = new Expenses()
            .description(UPDATED_DESCRIPTION)
            .paidBy(UPDATED_PAID_BY)
            .amount(UPDATED_AMOUNT)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return expenses;
    }

    @BeforeEach
    public void initTest() {
        expenses = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenses() throws Exception {
        int databaseSizeBeforeCreate = expensesRepository.findAll().size();
        // Create the Expenses
        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenses)))
            .andExpect(status().isCreated());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeCreate + 1);
        Expenses testExpenses = expensesList.get(expensesList.size() - 1);
        assertThat(testExpenses.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExpenses.getPaidBy()).isEqualByComparingTo(DEFAULT_PAID_BY);
        assertThat(testExpenses.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testExpenses.getCryptoCurrency()).isEqualTo(DEFAULT_CRYPTO_CURRENCY);
        assertThat(testExpenses.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testExpenses.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testExpenses.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createExpensesWithExistingId() throws Exception {
        // Create the Expenses with an existing ID
        expenses.setId(1L);

        int databaseSizeBeforeCreate = expensesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenses)))
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = expensesRepository.findAll().size();
        // set the field null
        expenses.setDescription(null);

        // Create the Expenses, which fails.

        restExpensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenses)))
            .andExpect(status().isBadRequest());

        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExpenses() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        // Get all the expensesList
        restExpensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].paidBy").value(hasItem(sameNumber(DEFAULT_PAID_BY))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].cryptoCurrency").value(hasItem(DEFAULT_CRYPTO_CURRENCY)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getExpenses() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        // Get the expenses
        restExpensesMockMvc
            .perform(get(ENTITY_API_URL_ID, expenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenses.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.paidBy").value(sameNumber(DEFAULT_PAID_BY)))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.cryptoCurrency").value(DEFAULT_CRYPTO_CURRENCY))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExpenses() throws Exception {
        // Get the expenses
        restExpensesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExpenses() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();

        // Update the expenses
        Expenses updatedExpenses = expensesRepository.findById(expenses.getId()).get();
        // Disconnect from session so that the updates on updatedExpenses are not directly saved in db
        em.detach(updatedExpenses);
        updatedExpenses
            .description(UPDATED_DESCRIPTION)
            .paidBy(UPDATED_PAID_BY)
            .amount(UPDATED_AMOUNT)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpenses))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
        Expenses testExpenses = expensesList.get(expensesList.size() - 1);
        assertThat(testExpenses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExpenses.getPaidBy()).isEqualByComparingTo(UPDATED_PAID_BY);
        assertThat(testExpenses.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testExpenses.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testExpenses.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testExpenses.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testExpenses.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expenses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpensesWithPatch() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();

        // Update the expenses using partial update
        Expenses partialUpdatedExpenses = new Expenses();
        partialUpdatedExpenses.setId(expenses.getId());

        partialUpdatedExpenses.description(UPDATED_DESCRIPTION).dateAdded(UPDATED_DATE_ADDED);

        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenses))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
        Expenses testExpenses = expensesList.get(expensesList.size() - 1);
        assertThat(testExpenses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExpenses.getPaidBy()).isEqualByComparingTo(DEFAULT_PAID_BY);
        assertThat(testExpenses.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testExpenses.getCryptoCurrency()).isEqualTo(DEFAULT_CRYPTO_CURRENCY);
        assertThat(testExpenses.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testExpenses.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testExpenses.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateExpensesWithPatch() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();

        // Update the expenses using partial update
        Expenses partialUpdatedExpenses = new Expenses();
        partialUpdatedExpenses.setId(expenses.getId());

        partialUpdatedExpenses
            .description(UPDATED_DESCRIPTION)
            .paidBy(UPDATED_PAID_BY)
            .amount(UPDATED_AMOUNT)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpenses))
            )
            .andExpect(status().isOk());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
        Expenses testExpenses = expensesList.get(expensesList.size() - 1);
        assertThat(testExpenses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExpenses.getPaidBy()).isEqualByComparingTo(UPDATED_PAID_BY);
        assertThat(testExpenses.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testExpenses.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testExpenses.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testExpenses.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testExpenses.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenses() throws Exception {
        int databaseSizeBeforeUpdate = expensesRepository.findAll().size();
        expenses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpensesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expenses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expenses in the database
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenses() throws Exception {
        // Initialize the database
        expensesRepository.saveAndFlush(expenses);

        int databaseSizeBeforeDelete = expensesRepository.findAll().size();

        // Delete the expenses
        restExpensesMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expenses> expensesList = expensesRepository.findAll();
        assertThat(expensesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
