package com.hadi.zikr.repository;

import com.hadi.zikr.domain.Zikr;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Zikr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZikrRepository extends JpaRepository<Zikr, Long> {}
