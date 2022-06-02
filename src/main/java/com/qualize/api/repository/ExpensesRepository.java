package com.qualize.api.repository;

import com.qualize.api.domain.Expenses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Expenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {}
