package com.qualize.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.Friends;
import com.qualize.api.repository.FriendsRepository;
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
 * Integration tests for the {@link FriendsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FriendsResourceIT {

    private static final String DEFAULT_FRIEND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FRIEND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;

    private static final String DEFAULT_WALLET_ID = "AAAAAAAAAA";
    private static final String UPDATED_WALLET_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_CRYPTO_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_CRYPTO_CURRENCY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/friends";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFriendsMockMvc;

    private Friends friends;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friends createEntity(EntityManager em) {
        Friends friends = new Friends()
            .friendName(DEFAULT_FRIEND_NAME)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .walletId(DEFAULT_WALLET_ID)
            .defaultCryptoCurrency(DEFAULT_DEFAULT_CRYPTO_CURRENCY)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return friends;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friends createUpdatedEntity(EntityManager em) {
        Friends friends = new Friends()
            .friendName(UPDATED_FRIEND_NAME)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletId(UPDATED_WALLET_ID)
            .defaultCryptoCurrency(UPDATED_DEFAULT_CRYPTO_CURRENCY)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return friends;
    }

    @BeforeEach
    public void initTest() {
        friends = createEntity(em);
    }

    @Test
    @Transactional
    void createFriends() throws Exception {
        int databaseSizeBeforeCreate = friendsRepository.findAll().size();
        // Create the Friends
        restFriendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friends)))
            .andExpect(status().isCreated());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeCreate + 1);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getFriendName()).isEqualTo(DEFAULT_FRIEND_NAME);
        assertThat(testFriends.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFriends.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testFriends.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testFriends.getWalletId()).isEqualTo(DEFAULT_WALLET_ID);
        assertThat(testFriends.getDefaultCryptoCurrency()).isEqualTo(DEFAULT_DEFAULT_CRYPTO_CURRENCY);
        assertThat(testFriends.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testFriends.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createFriendsWithExistingId() throws Exception {
        // Create the Friends with an existing ID
        friends.setId(1L);

        int databaseSizeBeforeCreate = friendsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friends)))
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        // Get all the friendsList
        restFriendsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friends.getId().intValue())))
            .andExpect(jsonPath("$.[*].friendName").value(hasItem(DEFAULT_FRIEND_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].walletId").value(hasItem(DEFAULT_WALLET_ID)))
            .andExpect(jsonPath("$.[*].defaultCryptoCurrency").value(hasItem(DEFAULT_DEFAULT_CRYPTO_CURRENCY)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        // Get the friends
        restFriendsMockMvc
            .perform(get(ENTITY_API_URL_ID, friends.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(friends.getId().intValue()))
            .andExpect(jsonPath("$.friendName").value(DEFAULT_FRIEND_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.walletId").value(DEFAULT_WALLET_ID))
            .andExpect(jsonPath("$.defaultCryptoCurrency").value(DEFAULT_DEFAULT_CRYPTO_CURRENCY))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFriends() throws Exception {
        // Get the friends
        restFriendsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends
        Friends updatedFriends = friendsRepository.findById(friends.getId()).get();
        // Disconnect from session so that the updates on updatedFriends are not directly saved in db
        em.detach(updatedFriends);
        updatedFriends
            .friendName(UPDATED_FRIEND_NAME)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletId(UPDATED_WALLET_ID)
            .defaultCryptoCurrency(UPDATED_DEFAULT_CRYPTO_CURRENCY)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFriends.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFriends))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getFriendName()).isEqualTo(UPDATED_FRIEND_NAME);
        assertThat(testFriends.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFriends.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFriends.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testFriends.getWalletId()).isEqualTo(UPDATED_WALLET_ID);
        assertThat(testFriends.getDefaultCryptoCurrency()).isEqualTo(UPDATED_DEFAULT_CRYPTO_CURRENCY);
        assertThat(testFriends.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testFriends.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, friends.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friends)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFriendsWithPatch() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends using partial update
        Friends partialUpdatedFriends = new Friends();
        partialUpdatedFriends.setId(friends.getId());

        partialUpdatedFriends
            .friendName(UPDATED_FRIEND_NAME)
            .telephone(UPDATED_TELEPHONE)
            .defaultCryptoCurrency(UPDATED_DEFAULT_CRYPTO_CURRENCY);

        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriends))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getFriendName()).isEqualTo(UPDATED_FRIEND_NAME);
        assertThat(testFriends.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFriends.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFriends.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testFriends.getWalletId()).isEqualTo(DEFAULT_WALLET_ID);
        assertThat(testFriends.getDefaultCryptoCurrency()).isEqualTo(UPDATED_DEFAULT_CRYPTO_CURRENCY);
        assertThat(testFriends.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testFriends.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateFriendsWithPatch() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends using partial update
        Friends partialUpdatedFriends = new Friends();
        partialUpdatedFriends.setId(friends.getId());

        partialUpdatedFriends
            .friendName(UPDATED_FRIEND_NAME)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletId(UPDATED_WALLET_ID)
            .defaultCryptoCurrency(UPDATED_DEFAULT_CRYPTO_CURRENCY)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriends))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getFriendName()).isEqualTo(UPDATED_FRIEND_NAME);
        assertThat(testFriends.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFriends.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFriends.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testFriends.getWalletId()).isEqualTo(UPDATED_WALLET_ID);
        assertThat(testFriends.getDefaultCryptoCurrency()).isEqualTo(UPDATED_DEFAULT_CRYPTO_CURRENCY);
        assertThat(testFriends.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testFriends.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, friends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(friends)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeDelete = friendsRepository.findAll().size();

        // Delete the friends
        restFriendsMockMvc
            .perform(delete(ENTITY_API_URL_ID, friends.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
