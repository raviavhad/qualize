package com.qualize.api.web.rest;

import static com.qualize.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.Accounts;
import com.qualize.api.domain.enumeration.AccountStatus;
import com.qualize.api.repository.AccountsRepository;
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
 * Integration tests for the {@link AccountsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountsResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT_YOU_OWE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_YOU_OWE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT_FRIEND_OWES = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_FRIEND_OWES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NET_RECEIVABLE_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_RECEIVABLE_PAYABLE = new BigDecimal(2);

    private static final String DEFAULT_CRYPTO_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CRYPTO_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CURRENCY_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENCY_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CRYPTO_RECEIVABLE_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CRYPTO_RECEIVABLE_PAYABLE = new BigDecimal(2);

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final AccountStatus DEFAULT_ACCOUNT_STATUS = AccountStatus.GREEN;
    private static final AccountStatus UPDATED_ACCOUNT_STATUS = AccountStatus.RED;

    private static final String ENTITY_API_URL = "/api/accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountsMockMvc;

    private Accounts accounts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accounts createEntity(EntityManager em) {
        Accounts accounts = new Accounts()
            .amountYouOwe(DEFAULT_AMOUNT_YOU_OWE)
            .amountFriendOwes(DEFAULT_AMOUNT_FRIEND_OWES)
            .netReceivablePayable(DEFAULT_NET_RECEIVABLE_PAYABLE)
            .cryptoCurrency(DEFAULT_CRYPTO_CURRENCY)
            .currencyValue(DEFAULT_CURRENCY_VALUE)
            .cryptoReceivablePayable(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .accountStatus(DEFAULT_ACCOUNT_STATUS);
        return accounts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accounts createUpdatedEntity(EntityManager em) {
        Accounts accounts = new Accounts()
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .amountFriendOwes(UPDATED_AMOUNT_FRIEND_OWES)
            .netReceivablePayable(UPDATED_NET_RECEIVABLE_PAYABLE)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .accountStatus(UPDATED_ACCOUNT_STATUS);
        return accounts;
    }

    @BeforeEach
    public void initTest() {
        accounts = createEntity(em);
    }

    @Test
    @Transactional
    void createAccounts() throws Exception {
        int databaseSizeBeforeCreate = accountsRepository.findAll().size();
        // Create the Accounts
        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accounts)))
            .andExpect(status().isCreated());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeCreate + 1);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getAmountYouOwe()).isEqualByComparingTo(DEFAULT_AMOUNT_YOU_OWE);
        assertThat(testAccounts.getAmountFriendOwes()).isEqualByComparingTo(DEFAULT_AMOUNT_FRIEND_OWES);
        assertThat(testAccounts.getNetReceivablePayable()).isEqualByComparingTo(DEFAULT_NET_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getCryptoCurrency()).isEqualTo(DEFAULT_CRYPTO_CURRENCY);
        assertThat(testAccounts.getCurrencyValue()).isEqualByComparingTo(DEFAULT_CURRENCY_VALUE);
        assertThat(testAccounts.getCryptoReceivablePayable()).isEqualByComparingTo(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testAccounts.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testAccounts.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testAccounts.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    void createAccountsWithExistingId() throws Exception {
        // Create the Accounts with an existing ID
        accounts.setId(1L);

        int databaseSizeBeforeCreate = accountsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accounts)))
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get all the accountsList
        restAccountsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accounts.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountYouOwe").value(hasItem(sameNumber(DEFAULT_AMOUNT_YOU_OWE))))
            .andExpect(jsonPath("$.[*].amountFriendOwes").value(hasItem(sameNumber(DEFAULT_AMOUNT_FRIEND_OWES))))
            .andExpect(jsonPath("$.[*].netReceivablePayable").value(hasItem(sameNumber(DEFAULT_NET_RECEIVABLE_PAYABLE))))
            .andExpect(jsonPath("$.[*].cryptoCurrency").value(hasItem(DEFAULT_CRYPTO_CURRENCY)))
            .andExpect(jsonPath("$.[*].currencyValue").value(hasItem(sameNumber(DEFAULT_CURRENCY_VALUE))))
            .andExpect(jsonPath("$.[*].cryptoReceivablePayable").value(hasItem(sameNumber(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE))))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get the accounts
        restAccountsMockMvc
            .perform(get(ENTITY_API_URL_ID, accounts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accounts.getId().intValue()))
            .andExpect(jsonPath("$.amountYouOwe").value(sameNumber(DEFAULT_AMOUNT_YOU_OWE)))
            .andExpect(jsonPath("$.amountFriendOwes").value(sameNumber(DEFAULT_AMOUNT_FRIEND_OWES)))
            .andExpect(jsonPath("$.netReceivablePayable").value(sameNumber(DEFAULT_NET_RECEIVABLE_PAYABLE)))
            .andExpect(jsonPath("$.cryptoCurrency").value(DEFAULT_CRYPTO_CURRENCY))
            .andExpect(jsonPath("$.currencyValue").value(sameNumber(DEFAULT_CURRENCY_VALUE)))
            .andExpect(jsonPath("$.cryptoReceivablePayable").value(sameNumber(DEFAULT_CRYPTO_RECEIVABLE_PAYABLE)))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccounts() throws Exception {
        // Get the accounts
        restAccountsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();

        // Update the accounts
        Accounts updatedAccounts = accountsRepository.findById(accounts.getId()).get();
        // Disconnect from session so that the updates on updatedAccounts are not directly saved in db
        em.detach(updatedAccounts);
        updatedAccounts
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .amountFriendOwes(UPDATED_AMOUNT_FRIEND_OWES)
            .netReceivablePayable(UPDATED_NET_RECEIVABLE_PAYABLE)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .accountStatus(UPDATED_ACCOUNT_STATUS);

        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccounts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccounts))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getAmountYouOwe()).isEqualByComparingTo(UPDATED_AMOUNT_YOU_OWE);
        assertThat(testAccounts.getAmountFriendOwes()).isEqualByComparingTo(UPDATED_AMOUNT_FRIEND_OWES);
        assertThat(testAccounts.getNetReceivablePayable()).isEqualByComparingTo(UPDATED_NET_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testAccounts.getCurrencyValue()).isEqualByComparingTo(UPDATED_CURRENCY_VALUE);
        assertThat(testAccounts.getCryptoReceivablePayable()).isEqualByComparingTo(UPDATED_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAccounts.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAccounts.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testAccounts.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accounts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accounts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accounts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accounts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountsWithPatch() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();

        // Update the accounts using partial update
        Accounts partialUpdatedAccounts = new Accounts();
        partialUpdatedAccounts.setId(accounts.getId());

        partialUpdatedAccounts
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .amountFriendOwes(UPDATED_AMOUNT_FRIEND_OWES)
            .netReceivablePayable(UPDATED_NET_RECEIVABLE_PAYABLE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccounts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccounts))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getAmountYouOwe()).isEqualByComparingTo(UPDATED_AMOUNT_YOU_OWE);
        assertThat(testAccounts.getAmountFriendOwes()).isEqualByComparingTo(UPDATED_AMOUNT_FRIEND_OWES);
        assertThat(testAccounts.getNetReceivablePayable()).isEqualByComparingTo(UPDATED_NET_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getCryptoCurrency()).isEqualTo(DEFAULT_CRYPTO_CURRENCY);
        assertThat(testAccounts.getCurrencyValue()).isEqualByComparingTo(DEFAULT_CURRENCY_VALUE);
        assertThat(testAccounts.getCryptoReceivablePayable()).isEqualByComparingTo(UPDATED_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testAccounts.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testAccounts.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testAccounts.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAccountsWithPatch() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();

        // Update the accounts using partial update
        Accounts partialUpdatedAccounts = new Accounts();
        partialUpdatedAccounts.setId(accounts.getId());

        partialUpdatedAccounts
            .amountYouOwe(UPDATED_AMOUNT_YOU_OWE)
            .amountFriendOwes(UPDATED_AMOUNT_FRIEND_OWES)
            .netReceivablePayable(UPDATED_NET_RECEIVABLE_PAYABLE)
            .cryptoCurrency(UPDATED_CRYPTO_CURRENCY)
            .currencyValue(UPDATED_CURRENCY_VALUE)
            .cryptoReceivablePayable(UPDATED_CRYPTO_RECEIVABLE_PAYABLE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .accountStatus(UPDATED_ACCOUNT_STATUS);

        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccounts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccounts))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getAmountYouOwe()).isEqualByComparingTo(UPDATED_AMOUNT_YOU_OWE);
        assertThat(testAccounts.getAmountFriendOwes()).isEqualByComparingTo(UPDATED_AMOUNT_FRIEND_OWES);
        assertThat(testAccounts.getNetReceivablePayable()).isEqualByComparingTo(UPDATED_NET_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getCryptoCurrency()).isEqualTo(UPDATED_CRYPTO_CURRENCY);
        assertThat(testAccounts.getCurrencyValue()).isEqualByComparingTo(UPDATED_CURRENCY_VALUE);
        assertThat(testAccounts.getCryptoReceivablePayable()).isEqualByComparingTo(UPDATED_CRYPTO_RECEIVABLE_PAYABLE);
        assertThat(testAccounts.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAccounts.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAccounts.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testAccounts.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accounts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accounts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accounts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
        accounts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accounts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        int databaseSizeBeforeDelete = accountsRepository.findAll().size();

        // Delete the accounts
        restAccountsMockMvc
            .perform(delete(ENTITY_API_URL_ID, accounts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
