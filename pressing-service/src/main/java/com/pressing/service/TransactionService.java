package com.pressing.service;

import com.pressing.model.Transaction;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for transactions */
public interface TransactionService {

  /**
   * Get all transactions in the system.
   *
   * @return Collection of all transactions in the system
   */
  public Page<Transaction> findAll(Pageable pageable);

  /**
   * Find a transaction by id.
   *
   * @param id
   * @return Transaction object if found else null
   */
  public Transaction findById(Long transactionId);

  /**
   * Find transactions made by customer with id, customerId.
   *
   * @param id
   * @return Collection of Transaction objects
   */
  public Page<Transaction> findByCustomerId(Long customerId, Pageable pageable);

  /**
   * Find transactions with item id, itemId.
   *
   * @param id
   * @return Collection of Transaction objects
   */
  public Page<Transaction> findByItemId(Long itemId, Pageable pageable);

  /**
   * Find transactions with status.
   *
   * @param status
   * @return Collection of Transaction objects
   */
  public Page<Transaction> findByStatus(Collection<String> status, Pageable pageable);

  /**
   * Find transactions with deposit date, deposit date.
   *
   * @param depositDate
   * @return Collection of Transaction objects
   */
  public Page<Transaction> findByDepositeDate(Date depositDate, Pageable pageable);

  /**
   * Find transactions with due date, dueDate.
   *
   * @param dueDate
   * @return Collection of Transaction objects
   */
  public Page<Transaction> findByDueDate(Date dueDate, Pageable pageable);

  /**
   * Find transactions with due date less then dueDate.
   *
   * @param dueDate
   * @return Collection of Transaction objects
   */
  public Page<Transaction> transactionsPastDueDate(Date dueDate, Pageable pageable);

  /**
   * Create a new transaction.
   *
   * @param transaction
   * @return Transaction object (created Transaction object)
   */
  public Transaction create(Transaction transaction);

  /**
   * Update an existing transaction's information.
   *
   * @param transaction
   * @return Transaction object (updated Transaction object)
   */
  public Transaction update(Transaction transaction);
}
