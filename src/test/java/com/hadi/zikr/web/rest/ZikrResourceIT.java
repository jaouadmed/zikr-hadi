package com.hadi.zikr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hadi.zikr.IntegrationTest;
import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.ZikrRepository;
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
