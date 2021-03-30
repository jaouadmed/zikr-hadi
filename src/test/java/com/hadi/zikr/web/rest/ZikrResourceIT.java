package com.hadi.zikr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hadi.zikr.IntegrationTest;
import com.hadi.zikr.domain.Type;
import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.ZikrRepository;
import com.hadi.zikr.service.criteria.ZikrCriteria;
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
 * Integration tests for the {@link ZikrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZikrResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;
    private static final Long SMALLER_COUNT = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/zikrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZikrRepository zikrRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZikrMockMvc;

    private Zikr zikr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zikr createEntity(EntityManager em) {
        Zikr zikr = new Zikr().content(DEFAULT_CONTENT).count(DEFAULT_COUNT);
        return zikr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zikr createUpdatedEntity(EntityManager em) {
        Zikr zikr = new Zikr().content(UPDATED_CONTENT).count(UPDATED_COUNT);
        return zikr;
    }

    @BeforeEach
    public void initTest() {
        zikr = createEntity(em);
    }

    @Test
    @Transactional
    void createZikr() throws Exception {
        int databaseSizeBeforeCreate = zikrRepository.findAll().size();
        // Create the Zikr
        restZikrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zikr)))
            .andExpect(status().isCreated());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeCreate + 1);
        Zikr testZikr = zikrList.get(zikrList.size() - 1);
        assertThat(testZikr.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testZikr.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    void createZikrWithExistingId() throws Exception {
        // Create the Zikr with an existing ID
        zikr.setId(1L);

        int databaseSizeBeforeCreate = zikrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZikrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zikr)))
            .andExpect(status().isBadRequest());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZikrs() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList
        restZikrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zikr.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }

    @Test
    @Transactional
    void getZikr() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get the zikr
        restZikrMockMvc
            .perform(get(ENTITY_API_URL_ID, zikr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zikr.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    void getZikrsByIdFiltering() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        Long id = zikr.getId();

        defaultZikrShouldBeFound("id.equals=" + id);
        defaultZikrShouldNotBeFound("id.notEquals=" + id);

        defaultZikrShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultZikrShouldNotBeFound("id.greaterThan=" + id);

        defaultZikrShouldBeFound("id.lessThanOrEqual=" + id);
        defaultZikrShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllZikrsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content equals to DEFAULT_CONTENT
        defaultZikrShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the zikrList where content equals to UPDATED_CONTENT
        defaultZikrShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllZikrsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content not equals to DEFAULT_CONTENT
        defaultZikrShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the zikrList where content not equals to UPDATED_CONTENT
        defaultZikrShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllZikrsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultZikrShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the zikrList where content equals to UPDATED_CONTENT
        defaultZikrShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllZikrsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content is not null
        defaultZikrShouldBeFound("content.specified=true");

        // Get all the zikrList where content is null
        defaultZikrShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllZikrsByContentContainsSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content contains DEFAULT_CONTENT
        defaultZikrShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the zikrList where content contains UPDATED_CONTENT
        defaultZikrShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllZikrsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where content does not contain DEFAULT_CONTENT
        defaultZikrShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the zikrList where content does not contain UPDATED_CONTENT
        defaultZikrShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count equals to DEFAULT_COUNT
        defaultZikrShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the zikrList where count equals to UPDATED_COUNT
        defaultZikrShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count not equals to DEFAULT_COUNT
        defaultZikrShouldNotBeFound("count.notEquals=" + DEFAULT_COUNT);

        // Get all the zikrList where count not equals to UPDATED_COUNT
        defaultZikrShouldBeFound("count.notEquals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultZikrShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the zikrList where count equals to UPDATED_COUNT
        defaultZikrShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count is not null
        defaultZikrShouldBeFound("count.specified=true");

        // Get all the zikrList where count is null
        defaultZikrShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count is greater than or equal to DEFAULT_COUNT
        defaultZikrShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the zikrList where count is greater than or equal to UPDATED_COUNT
        defaultZikrShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count is less than or equal to DEFAULT_COUNT
        defaultZikrShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the zikrList where count is less than or equal to SMALLER_COUNT
        defaultZikrShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count is less than DEFAULT_COUNT
        defaultZikrShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the zikrList where count is less than UPDATED_COUNT
        defaultZikrShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        // Get all the zikrList where count is greater than DEFAULT_COUNT
        defaultZikrShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the zikrList where count is greater than SMALLER_COUNT
        defaultZikrShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllZikrsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);
        Type type = TypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        zikr.setType(type);
        zikrRepository.saveAndFlush(zikr);
        Long typeId = type.getId();

        // Get all the zikrList where type equals to typeId
        defaultZikrShouldBeFound("typeId.equals=" + typeId);

        // Get all the zikrList where type equals to (typeId + 1)
        defaultZikrShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultZikrShouldBeFound(String filter) throws Exception {
        restZikrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zikr.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));

        // Check, that the count call also returns 1
        restZikrMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultZikrShouldNotBeFound(String filter) throws Exception {
        restZikrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restZikrMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingZikr() throws Exception {
        // Get the zikr
        restZikrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewZikr() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();

        // Update the zikr
        Zikr updatedZikr = zikrRepository.findById(zikr.getId()).get();
        // Disconnect from session so that the updates on updatedZikr are not directly saved in db
        em.detach(updatedZikr);
        updatedZikr.content(UPDATED_CONTENT).count(UPDATED_COUNT);

        restZikrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZikr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedZikr))
            )
            .andExpect(status().isOk());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
        Zikr testZikr = zikrList.get(zikrList.size() - 1);
        assertThat(testZikr.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testZikr.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    void putNonExistingZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zikr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zikr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zikr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zikr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZikrWithPatch() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();

        // Update the zikr using partial update
        Zikr partialUpdatedZikr = new Zikr();
        partialUpdatedZikr.setId(zikr.getId());

        partialUpdatedZikr.content(UPDATED_CONTENT);

        restZikrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZikr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZikr))
            )
            .andExpect(status().isOk());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
        Zikr testZikr = zikrList.get(zikrList.size() - 1);
        assertThat(testZikr.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testZikr.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    void fullUpdateZikrWithPatch() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();

        // Update the zikr using partial update
        Zikr partialUpdatedZikr = new Zikr();
        partialUpdatedZikr.setId(zikr.getId());

        partialUpdatedZikr.content(UPDATED_CONTENT).count(UPDATED_COUNT);

        restZikrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZikr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZikr))
            )
            .andExpect(status().isOk());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
        Zikr testZikr = zikrList.get(zikrList.size() - 1);
        assertThat(testZikr.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testZikr.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    void patchNonExistingZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zikr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zikr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zikr))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZikr() throws Exception {
        int databaseSizeBeforeUpdate = zikrRepository.findAll().size();
        zikr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZikrMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(zikr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zikr in the database
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZikr() throws Exception {
        // Initialize the database
        zikrRepository.saveAndFlush(zikr);

        int databaseSizeBeforeDelete = zikrRepository.findAll().size();

        // Delete the zikr
        restZikrMockMvc
            .perform(delete(ENTITY_API_URL_ID, zikr.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zikr> zikrList = zikrRepository.findAll();
        assertThat(zikrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
