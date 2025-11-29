package com.pressing.repository;

import com.pressing.model.Transaction;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Collection<Transaction> findByDepositDate(@Param("date") Date date);

  Collection<Transaction> findByDueDate(@Param("date") Date date);

  Collection<Transaction> findByCustomerId(@Param("id") Long id);

  Collection<Transaction> findByItemId(@Param("id") Long id);

  Collection<Transaction> findByDueDateBeforeAndStatusNot(
      @Param("date") Date date, @Param("status") String status);

  Collection<Transaction> findByStatusIn(@Param("status") Collection<String> status);
}
