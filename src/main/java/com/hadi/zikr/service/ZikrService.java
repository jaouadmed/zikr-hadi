package com.hadi.zikr.service;

import com.hadi.zikr.domain.Zikr;
import com.hadi.zikr.repository.ZikrRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Zikr}.
 */
@Service
@Transactional
public class ZikrService {

    private final Logger log = LoggerFactory.getLogger(ZikrService.class);

    private final ZikrRepository zikrRepository;

    public ZikrService(ZikrRepository zikrRepository) {
        this.zikrRepository = zikrRepository;
    }

    /**
     * Save a zikr.
     *
     * @param zikr the entity to save.
     * @return the persisted entity.
     */
    public Zikr save(Zikr zikr) {
        log.debug("Request to save Zikr : {}", zikr);
        return zikrRepository.save(zikr);
    }

    /**
     * Partially update a zikr.
     *
     * @param zikr the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Zikr> partialUpdate(Zikr zikr) {
        log.debug("Request to partially update Zikr : {}", zikr);

        return zikrRepository
            .findById(zikr.getId())
            .map(
                existingZikr -> {
                    if (zikr.getContent() != null) {
                        existingZikr.setContent(zikr.getContent());
                    }
                    if (zikr.getCount() != null) {
                        existingZikr.setCount(zikr.getCount());
                    }

                    return existingZikr;
                }
            )
            .map(zikrRepository::save);
    }

    /**
     * Get all the zikrs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Zikr> findAll(Pageable pageable) {
        log.debug("Request to get all Zikrs");
        return zikrRepository.findAll(pageable);
    }

    /**
     * Get one zikr by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Zikr> findOne(Long id) {
        log.debug("Request to get Zikr : {}", id);
        return zikrRepository.findById(id);
    }

    /**
     * Delete the zikr by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Zikr : {}", id);
        zikrRepository.deleteById(id);
    }
}
