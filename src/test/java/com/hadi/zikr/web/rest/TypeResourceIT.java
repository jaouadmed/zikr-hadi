package com.hadi.zikr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hadi.zikr.IntegrationTest;
import com.hadi.zikr.domain.Type;
import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.TypeRepository;
import com.hadi.zikr.service.criteria.TypeCriteria;
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
 * Integration tests for the {@link TypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeMockMvc;

    private Type type;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Type createEntity(EntityManager em) {
        Type type = new Type().title(DEFAULT_TITLE).color(DEFAULT_COLOR);
        return type;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Type createUpdatedEntity(EntityManager em) {
        Type type = new Type().title(UPDATED_TITLE).color(UPDATED_COLOR);
        return type;
    }

    @BeforeEach
    public void initTest() {
        type = createEntity(em);
    }

    @Test
    @Transactional
    void createType() throws Exception {
        int databaseSizeBeforeCreate = typeRepository.findAll().size();
        // Create the Type
        restTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isCreated());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate + 1);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testType.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void createTypeWithExistingId() throws Exception {
        // Create the Type with an existing ID
        type.setId(1L);

        int databaseSizeBeforeCreate = typeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypes() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(type.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)));
    }

    @Test
    @Transactional
    void getType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get the type
        restTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(type.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR));
    }

    @Test
    @Transactional
    void getTypesByIdFiltering() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        Long id = type.getId();

        defaultTypeShouldBeFound("id.equals=" + id);
        defaultTypeShouldNotBeFound("id.notEquals=" + id);

        defaultTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title equals to DEFAULT_TITLE
        defaultTypeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the typeList where title equals to UPDATED_TITLE
        defaultTypeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTypesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title not equals to DEFAULT_TITLE
        defaultTypeShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the typeList where title not equals to UPDATED_TITLE
        defaultTypeShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTypesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTypeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the typeList where title equals to UPDATED_TITLE
        defaultTypeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTypesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title is not null
        defaultTypeShouldBeFound("title.specified=true");

        // Get all the typeList where title is null
        defaultTypeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTypesByTitleContainsSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title contains DEFAULT_TITLE
        defaultTypeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the typeList where title contains UPDATED_TITLE
        defaultTypeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTypesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where title does not contain DEFAULT_TITLE
        defaultTypeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the typeList where title does not contain UPDATED_TITLE
        defaultTypeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTypesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color equals to DEFAULT_COLOR
        defaultTypeShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the typeList where color equals to UPDATED_COLOR
        defaultTypeShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTypesByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color not equals to DEFAULT_COLOR
        defaultTypeShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the typeList where color not equals to UPDATED_COLOR
        defaultTypeShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTypesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultTypeShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the typeList where color equals to UPDATED_COLOR
        defaultTypeShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTypesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color is not null
        defaultTypeShouldBeFound("color.specified=true");

        // Get all the typeList where color is null
        defaultTypeShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    void getAllTypesByColorContainsSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color contains DEFAULT_COLOR
        defaultTypeShouldBeFound("color.contains=" + DEFAULT_COLOR);

        // Get all the typeList where color contains UPDATED_COLOR
        defaultTypeShouldNotBeFound("color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTypesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList where color does not contain DEFAULT_COLOR
        defaultTypeShouldNotBeFound("color.doesNotContain=" + DEFAULT_COLOR);

        // Get all the typeList where color does not contain UPDATED_COLOR
        defaultTypeShouldBeFound("color.doesNotContain=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTypesByZikrIsEqualToSomething() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);
        Zikr zikr = ZikrResourceIT.createEntity(em);
        em.persist(zikr);
        em.flush();
        type.addZikr(zikr);
        typeRepository.saveAndFlush(type);
        Long zikrId = zikr.getId();

        // Get all the typeList where zikr equals to zikrId
        defaultTypeShouldBeFound("zikrId.equals=" + zikrId);

        // Get all the typeList where zikr equals to (zikrId + 1)
        defaultTypeShouldNotBeFound("zikrId.equals=" + (zikrId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeShouldBeFound(String filter) throws Exception {
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(type.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)));

        // Check, that the count call also returns 1
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeShouldNotBeFound(String filter) throws Exception {
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingType() throws Exception {
        // Get the type
        restTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type
        Type updatedType = typeRepository.findById(type.getId()).get();
        // Disconnect from session so that the updates on updatedType are not directly saved in db
        em.detach(updatedType);
        updatedType.title(UPDATED_TITLE).color(UPDATED_COLOR);

        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedType))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testType.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void putNonExistingType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, type.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(type))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(type))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeWithPatch() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type using partial update
        Type partialUpdatedType = new Type();
        partialUpdatedType.setId(type.getId());

        partialUpdatedType.title(UPDATED_TITLE);

        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedType))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testType.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void fullUpdateTypeWithPatch() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type using partial update
        Type partialUpdatedType = new Type();
        partialUpdatedType.setId(type.getId());

        partialUpdatedType.title(UPDATED_TITLE).color(UPDATED_COLOR);

        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedType))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testType.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void patchNonExistingType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, type.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(type))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(type))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeDelete = typeRepository.findAll().size();

        // Delete the type
        restTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, type.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
