package com.qualize.api.repository;

import com.qualize.api.domain.Settlements;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Settlements entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettlementsRepository extends JpaRepository<Settlements, Long> {}
