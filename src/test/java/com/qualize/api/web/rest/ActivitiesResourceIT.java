package com.qualize.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qualize.api.IntegrationTest;
import com.qualize.api.domain.Activities;
import com.qualize.api.domain.enumeration.ActivitiyStatus;
import com.qualize.api.repository.ActivitiesRepository;
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
 * Integration tests for the {@link ActivitiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivitiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final ActivitiyStatus DEFAULT_STATUS = ActivitiyStatus.COMPLETED;
    private static final ActivitiyStatus UPDATED_STATUS = ActivitiyStatus.INPROGRESS;

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivitiesMockMvc;

    private Activities activities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activities createEntity(EntityManager em) {
        Activities activities = new Activities()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .status(DEFAULT_STATUS);
        return activities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activities createUpdatedEntity(EntityManager em) {
        Activities activities = new Activities()
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);
        return activities;
    }

    @BeforeEach
    public void initTest() {
        activities = createEntity(em);
    }

    @Test
    @Transactional
    void createActivities() throws Exception {
        int databaseSizeBeforeCreate = activitiesRepository.findAll().size();
        // Create the Activities
        restActivitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activities)))
            .andExpect(status().isCreated());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeCreate + 1);
        Activities testActivities = activitiesList.get(activitiesList.size() - 1);
        assertThat(testActivities.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivities.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testActivities.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivities.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testActivities.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testActivities.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testActivities.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createActivitiesWithExistingId() throws Exception {
        // Create the Activities with an existing ID
        activities.setId(1L);

        int databaseSizeBeforeCreate = activitiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activities)))
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = activitiesRepository.findAll().size();
        // set the field null
        activities.setName(null);

        // Create the Activities, which fails.

        restActivitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activities)))
            .andExpect(status().isBadRequest());

        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        // Get all the activitiesList
        restActivitiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activities.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        // Get the activities
        restActivitiesMockMvc
            .perform(get(ENTITY_API_URL_ID, activities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activities.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingActivities() throws Exception {
        // Get the activities
        restActivitiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();

        // Update the activities
        Activities updatedActivities = activitiesRepository.findById(activities.getId()).get();
        // Disconnect from session so that the updates on updatedActivities are not directly saved in db
        em.detach(updatedActivities);
        updatedActivities
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActivities))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
        Activities testActivities = activitiesList.get(activitiesList.size() - 1);
        assertThat(testActivities.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivities.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActivities.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivities.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testActivities.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testActivities.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testActivities.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activities)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivitiesWithPatch() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();

        // Update the activities using partial update
        Activities partialUpdatedActivities = new Activities();
        partialUpdatedActivities.setId(activities.getId());

        partialUpdatedActivities.sortOrder(UPDATED_SORT_ORDER).dateModified(UPDATED_DATE_MODIFIED);

        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivities))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
        Activities testActivities = activitiesList.get(activitiesList.size() - 1);
        assertThat(testActivities.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivities.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testActivities.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivities.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testActivities.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testActivities.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testActivities.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateActivitiesWithPatch() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();

        // Update the activities using partial update
        Activities partialUpdatedActivities = new Activities();
        partialUpdatedActivities.setId(activities.getId());

        partialUpdatedActivities
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivities))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
        Activities testActivities = activitiesList.get(activitiesList.size() - 1);
        assertThat(testActivities.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivities.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActivities.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivities.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testActivities.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testActivities.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testActivities.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivities() throws Exception {
        int databaseSizeBeforeUpdate = activitiesRepository.findAll().size();
        activities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(activities))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activities in the database
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        int databaseSizeBeforeDelete = activitiesRepository.findAll().size();

        // Delete the activities
        restActivitiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, activities.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Activities> activitiesList = activitiesRepository.findAll();
        assertThat(activitiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
