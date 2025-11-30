package com.pressing.repository;

import com.pressing.model.Transaction;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findByDepositDate(@Param("date") Date date, Pageable pageable);

  Page<Transaction> findByDueDate(@Param("date") Date date, Pageable pageable);

  Page<Transaction> findByCustomerId(@Param("id") Long id, Pageable pageable);

  Page<Transaction> findByItemId(@Param("id") Long id, Pageable pageable);

  Page<Transaction> findByDueDateBeforeAndStatusNot(
      @Param("date") Date date, @Param("status") String status, Pageable pageable);

  Page<Transaction> findByStatusIn(@Param("status") Collection<String> status, Pageable pageable);
}
