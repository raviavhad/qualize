package com.qualize.api.web.rest;

import static com.qualize.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.Settlements;
import com.qualize.api.domain.enumeration.SettlementStatus;
import com.qualize.api.repository.SettlementsRepository;
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
 * Integration tests for the {@link SettlementsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SettlementsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT_YOU_OWE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_YOU_OWE = new BigDecimal(2);

    private static final SettlementStatus DEFAULT_SETTLEMENT_STATUS = SettlementStatus.COMPLETED;
    private static final SettlementStatus UPDATED_SETTLEMENT_STATUS = SettlementStatus.INPROGRESS;

    private static final String DEFAULT_CRYPTO_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CRYPTO_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CURRENCY_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENCY_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CRYPTO_RECEIVABLE_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CRYPTO_RECEIVABLE_PAYABLE = new BigDecimal(2);

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/settlements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettlementsRepository settlementsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettlementsMockMvc;

    private Settlements settlements;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Settlements createEntity(EntityManager em) {
        Settlements settlements = new Settlements()
            .description(DEFAULT_DESCRIPTION)
            .amountYouOwe(DEFAULT_AMOUNT_YOU_OWE)
            .settlementStatus(DEFAULT_SETTLEMENT_STATUS)
            .cryptoCurrency(DEFAULT_CRYPTO_CURRENCY)
            .currencyValue(DEFAULT_CURRENCY_VALUE)
            .cryptoReceivablePayable(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(DEFAULT_SORT_ORDER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return settlements;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Settlements createUpdatedEntity(EntityManager em) {
        Settlements settlements = new Settlements()
            .description(UPDATED_DESCRIPTION)
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .settlementStatus(UPDATED_SETTLEMENT_STATUS)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);
        return settlements;
    }

    @BeforeEach
    public void initTest() {
        settlements = createEntity(em);
    }

    @Test
    @Transactional
    void createSettlements() throws Exception {
        int databaseSizeBeforeCreate = settlementsRepository.findAll().size();
        // Create the Settlements
        restSettlementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlements)))
            .andExpect(status().isCreated());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeCreate + 1);
        Settlements testSettlements = settlementsList.get(settlementsList.size() - 1);
        assertThat(testSettlements.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSettlements.getAmountYouOwe()).isEqualByComparingTo(DEFAULT_AMOUNT_YOU_OWE);
        assertThat(testSettlements.getSettlementStatus()).isEqualTo(DEFAULT_SETTLEMENT_STATUS);
        assertThat(testSettlements.getCryptoCurrency()).isEqualTo(DEFAULT_CRYPTO_CURRENCY);
        assertThat(testSettlements.getCurrencyValue()).isEqualByComparingTo(DEFAULT_CURRENCY_VALUE);
        assertThat(testSettlements.getCryptoReceivablePayable()).isEqualByComparingTo(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testSettlements.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testSettlements.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSettlements.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createSettlementsWithExistingId() throws Exception {
        // Create the Settlements with an existing ID
        settlements.setId(1L);

        int databaseSizeBeforeCreate = settlementsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettlementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlements)))
            .andExpect(status().isBadRequest());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementsRepository.findAll().size();
        // set the field null
        settlements.setDescription(null);

        // Create the Settlements, which fails.

        restSettlementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlements)))
            .andExpect(status().isBadRequest());

        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSettlements() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        // Get all the settlementsList
        restSettlementsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlements.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amountYouOwe").value(hasItem(sameNumber(DEFAULT_AMOUNT_YOU_OWE))))
            .andExpect(jsonPath("$.[*].settlementStatus").value(hasItem(DEFAULT_SETTLEMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cryptoCurrency").value(hasItem(DEFAULT_CRYPTO_CURRENCY)))
            .andExpect(jsonPath("$.[*].currencyValue").value(hasItem(sameNumber(DEFAULT_CURRENCY_VALUE))))
            .andExpect(jsonPath("$.[*].cryptoReceivablePayable").value(hasItem(sameNumber(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE))))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getSettlements() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        // Get the settlements
        restSettlementsMockMvc
            .perform(get(ENTITY_API_URL_ID, settlements.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settlements.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amountYouOwe").value(sameNumber(DEFAULT_AMOUNT_YOU_OWE)))
            .andExpect(jsonPath("$.settlementStatus").value(DEFAULT_SETTLEMENT_STATUS.toString()))
            .andExpect(jsonPath("$.cryptoCurrency").value(DEFAULT_CRYPTO_CURRENCY))
            .andExpect(jsonPath("$.currencyValue").value(sameNumber(DEFAULT_CURRENCY_VALUE)))
            .andExpect(jsonPath("$.cryptoReceivablePayable").value(sameNumber(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE)))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSettlements() throws Exception {
        // Get the settlements
        restSettlementsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettlements() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();

        // Update the settlements
        Settlements updatedSettlements = settlementsRepository.findById(settlements.getId()).get();
        // Disconnect from session so that the updates on updatedSettlements are not directly saved in db
        em.detach(updatedSettlements);
        updatedSettlements
            .description(UPDATED_DESCRIPTION)
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .settlementStatus(UPDATED_SETTLEMENT_STATUS)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restSettlementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSettlements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSettlements))
            )
            .andExpect(status().isOk());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
        Settlements testSettlements = settlementsList.get(settlementsList.size() - 1);
        assertThat(testSettlements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlements.getAmountYouOwe()).isEqualByComparingTo(UPDATED_AMOUNT_YOU_OWE);
        assertThat(testSettlements.getSettlementStatus()).isEqualTo(UPDATED_SETTLEMENT_STATUS);
        assertThat(testSettlements.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testSettlements.getCurrencyValue()).isEqualByComparingTo(UPDATED_CURRENCY_VALUE);
        assertThat(testSettlements.getCryptoReceivablePayable()).isEqualByComparingTo(UPDATED_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testSettlements.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testSettlements.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSettlements.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSettlementsWithPatch() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();

        // Update the settlements using partial update
        Settlements partialUpdatedSettlements = new Settlements();
        partialUpdatedSettlements.setId(settlements.getId());

        partialUpdatedSettlements
            .description(UPDATED_DESCRIPTION)
            .settlementStatus(UPDATED_SETTLEMENT_STATUS)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateModified(UPDATED_DATE_MODIFIED);

        restSettlementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlements))
            )
            .andExpect(status().isOk());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
        Settlements testSettlements = settlementsList.get(settlementsList.size() - 1);
        assertThat(testSettlements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlements.getAmountYouOwe()).isEqualByComparingTo(DEFAULT_AMOUNT_YOU_OWE);
        assertThat(testSettlements.getSettlementStatus()).isEqualTo(UPDATED_SETTLEMENT_STATUS);
        assertThat(testSettlements.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testSettlements.getCurrencyValue()).isEqualByComparingTo(UPDATED_CURRENCY_VALUE);
        assertThat(testSettlements.getCryptoReceivablePayable()).isEqualByComparingTo(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testSettlements.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testSettlements.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSettlements.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateSettlementsWithPatch() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();

        // Update the settlements using partial update
        Settlements partialUpdatedSettlements = new Settlements();
        partialUpdatedSettlements.setId(settlements.getId());

        partialUpdatedSettlements
            .description(UPDATED_DESCRIPTION)
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .settlementStatus(UPDATED_SETTLEMENT_STATUS)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restSettlementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlements))
            )
            .andExpect(status().isOk());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
        Settlements testSettlements = settlementsList.get(settlementsList.size() - 1);
        assertThat(testSettlements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlements.getAmountYouOwe()).isEqualByComparingTo(UPDATED_AMOUNT_YOU_OWE);
        assertThat(testSettlements.getSettlementStatus()).isEqualTo(UPDATED_SETTLEMENT_STATUS);
        assertThat(testSettlements.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testSettlements.getCurrencyValue()).isEqualByComparingTo(UPDATED_CURRENCY_VALUE);
        assertThat(testSettlements.getCryptoReceivablePayable()).isEqualByComparingTo(UPDATED_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testSettlements.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testSettlements.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSettlements.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settlements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettlements() throws Exception {
        int databaseSizeBeforeUpdate = settlementsRepository.findAll().size();
        settlements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(settlements))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Settlements in the database
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSettlements() throws Exception {
        // Initialize the database
        settlementsRepository.saveAndFlush(settlements);

        int databaseSizeBeforeDelete = settlementsRepository.findAll().size();

        // Delete the settlements
        restSettlementsMockMvc
            .perform(delete(ENTITY_API_URL_ID, settlements.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Settlements> settlementsList = settlementsRepository.findAll();
        assertThat(settlementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
