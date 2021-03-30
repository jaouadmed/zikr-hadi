package com.hadi.zikr.service;

import com.hadi.zikr.domain.Type;
import com.hadi.zikr.repository.TypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Type}.
 */
@Service
@Transactional
public class TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeService.class);

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Save a type.
     *
     * @param type the entity to save.
     * @return the persisted entity.
     */
    public Type save(Type type) {
        log.debug("Request to save Type : {}", type);
        return typeRepository.save(type);
    }

    /**
     * Partially update a type.
     *
     * @param type the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Type> partialUpdate(Type type) {
        log.debug("Request to partially update Type : {}", type);

        return typeRepository
            .findById(type.getId())
            .map(
                existingType -> {
                    if (type.getTitle() != null) {
                        existingType.setTitle(type.getTitle());
                    }
                    if (type.getColor() != null) {
                        existingType.setColor(type.getColor());
                    }

                    return existingType;
                }
            )
            .map(typeRepository::save);
    }

    /**
     * Get all the types.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Type> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typeRepository.findAll(pageable);
    }

    /**
     * Get one type by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Type> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }
}
