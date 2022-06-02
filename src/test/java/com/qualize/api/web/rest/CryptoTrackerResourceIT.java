package com.qualize.api.web.rest;

import static com.qualize.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.CryptoTracker;
import com.qualize.api.repository.CryptoTrackerRepository;
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
 * Integration tests for the {@link CryptoTrackerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CryptoTrackerResourceIT {

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final LocalDate DEFAULT_FEED_DATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FEED_DATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/crypto-trackers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CryptoTrackerRepository cryptoTrackerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCryptoTrackerMockMvc;

    private CryptoTracker cryptoTracker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CryptoTracker createEntity(EntityManager em) {
        CryptoTracker cryptoTracker = new CryptoTracker()
            .currency(DEFAULT_CURRENCY)
            .value(DEFAULT_VALUE)
            .feedDateTime(DEFAULT_FEED_DATE_TIME)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return cryptoTracker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CryptoTracker createUpdatedEntity(EntityManager em) {
        CryptoTracker cryptoTracker = new CryptoTracker()
            .currency(UPDATED_CURRENCY)
            .value(UPDATED_VALUE)
            .feedDateTime(UPDATED_FEED_DATE_TIME)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return cryptoTracker;
    }

    @BeforeEach
    public void initTest() {
        cryptoTracker = createEntity(em);
    }

    @Test
    @Transactional
    void createCryptoTracker() throws Exception {
        int databaseSizeBeforeCreate = cryptoTrackerRepository.findAll().size();
        // Create the CryptoTracker
        restCryptoTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptoTracker)))
            .andExpect(status().isCreated());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeCreate + 1);
        CryptoTracker testCryptoTracker = cryptoTrackerList.get(cryptoTrackerList.size() - 1);
        assertThat(testCryptoTracker.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCryptoTracker.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testCryptoTracker.getFeedDateTime()).isEqualTo(DEFAULT_FEED_DATE_TIME);
        assertThat(testCryptoTracker.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testCryptoTracker.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createCryptoTrackerWithExistingId() throws Exception {
        // Create the CryptoTracker with an existing ID
        cryptoTracker.setId(1L);

        int databaseSizeBeforeCreate = cryptoTrackerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCryptoTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptoTracker)))
            .andExpect(status().isBadRequest());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = cryptoTrackerRepository.findAll().size();
        // set the field null
        cryptoTracker.setCurrency(null);

        // Create the CryptoTracker, which fails.

        restCryptoTrackerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptoTracker)))
            .andExpect(status().isBadRequest());

        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCryptoTrackers() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        // Get all the cryptoTrackerList
        restCryptoTrackerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptoTracker.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].feedDateTime").value(hasItem(DEFAULT_FEED_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getCryptoTracker() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        // Get the cryptoTracker
        restCryptoTrackerMockMvc
            .perform(get(ENTITY_API_URL_ID, cryptoTracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cryptoTracker.getId().intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.feedDateTime").value(DEFAULT_FEED_DATE_TIME.toString()))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCryptoTracker() throws Exception {
        // Get the cryptoTracker
        restCryptoTrackerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCryptoTracker() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();

        // Update the cryptoTracker
        CryptoTracker updatedCryptoTracker = cryptoTrackerRepository.findById(cryptoTracker.getId()).get();
        // Disconnect from session so that the updates on updatedCryptoTracker are not directly saved in db
        em.detach(updatedCryptoTracker);
        updatedCryptoTracker
            .currency(UPDATED_CURRENCY)
            .value(UPDATED_VALUE)
            .feedDateTime(UPDATED_FEED_DATE_TIME)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restCryptoTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCryptoTracker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCryptoTracker))
            )
            .andExpect(status().isOk());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
        CryptoTracker testCryptoTracker = cryptoTrackerList.get(cryptoTrackerList.size() - 1);
        assertThat(testCryptoTracker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCryptoTracker.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testCryptoTracker.getFeedDateTime()).isEqualTo(UPDATED_FEED_DATE_TIME);
        assertThat(testCryptoTracker.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testCryptoTracker.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cryptoTracker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cryptoTracker))
            )
            .andExpect(status().isBadRequest());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cryptoTracker))
            )
            .andExpect(status().isBadRequest());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptoTracker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCryptoTrackerWithPatch() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();

        // Update the cryptoTracker using partial update
        CryptoTracker partialUpdatedCryptoTracker = new CryptoTracker();
        partialUpdatedCryptoTracker.setId(cryptoTracker.getId());

        partialUpdatedCryptoTracker.value(UPDATED_VALUE).dateModified(UPDATED_DATE_MODIFIED);

        restCryptoTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCryptoTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCryptoTracker))
            )
            .andExpect(status().isOk());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
        CryptoTracker testCryptoTracker = cryptoTrackerList.get(cryptoTrackerList.size() - 1);
        assertThat(testCryptoTracker.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCryptoTracker.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testCryptoTracker.getFeedDateTime()).isEqualTo(DEFAULT_FEED_DATE_TIME);
        assertThat(testCryptoTracker.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testCryptoTracker.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCryptoTrackerWithPatch() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();

        // Update the cryptoTracker using partial update
        CryptoTracker partialUpdatedCryptoTracker = new CryptoTracker();
        partialUpdatedCryptoTracker.setId(cryptoTracker.getId());

        partialUpdatedCryptoTracker
            .currency(UPDATED_CURRENCY)
            .value(UPDATED_VALUE)
            .feedDateTime(UPDATED_FEED_DATE_TIME)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restCryptoTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCryptoTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCryptoTracker))
            )
            .andExpect(status().isOk());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
        CryptoTracker testCryptoTracker = cryptoTrackerList.get(cryptoTrackerList.size() - 1);
        assertThat(testCryptoTracker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCryptoTracker.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testCryptoTracker.getFeedDateTime()).isEqualTo(UPDATED_FEED_DATE_TIME);
        assertThat(testCryptoTracker.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testCryptoTracker.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cryptoTracker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cryptoTracker))
            )
            .andExpect(status().isBadRequest());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cryptoTracker))
            )
            .andExpect(status().isBadRequest());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCryptoTracker() throws Exception {
        int databaseSizeBeforeUpdate = cryptoTrackerRepository.findAll().size();
        cryptoTracker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptoTrackerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cryptoTracker))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CryptoTracker in the database
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCryptoTracker() throws Exception {
        // Initialize the database
        cryptoTrackerRepository.saveAndFlush(cryptoTracker);

        int databaseSizeBeforeDelete = cryptoTrackerRepository.findAll().size();

        // Delete the cryptoTracker
        restCryptoTrackerMockMvc
            .perform(delete(ENTITY_API_URL_ID, cryptoTracker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CryptoTracker> cryptoTrackerList = cryptoTrackerRepository.findAll();
        assertThat(cryptoTrackerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
