package com.qualize.api.repository;

import com.qualize.api.domain.CryptoTracker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CryptoTracker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CryptoTrackerRepository extends JpaRepository<CryptoTracker, Long> {}
