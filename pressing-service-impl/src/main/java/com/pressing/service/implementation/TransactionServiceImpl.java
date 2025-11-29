package com.pressing.service.implementation;

import com.pressing.model.Transaction;
import com.pressing.repository.TransactionRepository;
import com.pressing.service.TransactionService;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_SALES_AGENT", "ROLE_ADMINISTRATION"})
public class TransactionServiceImpl implements TransactionService {

  @Autowired private TransactionRepository transactionRepository;

  @Override
  @Cacheable("transactions")
  public Page<Transaction> findAll(Pageable pageable) {
    return transactionRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "transaction", key = "#transactionId")
  public Transaction findById(Long transactionId) {
    if (transactionId == null) {
      return null;
    }

    Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
    return transaction;
  }

  @Override
  @Cacheable(value = "transactionsByCustomer", key = "#customerId")
  public Page<Transaction> findByCustomerId(Long customerId, Pageable pageable) {
    return transactionRepository.findByCustomerId(customerId, pageable);
  }

  @Override
  @Cacheable(value = "transactionsByItem", key = "#itemId")
  public Page<Transaction> findByItemId(Long itemId, Pageable pageable) {
    return transactionRepository.findByItemId(itemId, pageable);
  }

  @Override
  @CacheEvict(value = {"transactions", "transaction", "transactionsByCustomer", "transactionsByItem", "transactionsByDate", "transactionsByDueDate", "transactionsPastDueDate", "transactionsByStatus"}, allEntries = true)
  public Transaction create(Transaction transaction) {
    if (transaction == null) {
      return null;
    }

    Transaction savedCustomerItem = transactionRepository.save(transaction);
    return savedCustomerItem;
  }

  @Override
  @CacheEvict(value = {"transactions", "transaction", "transactionsByCustomer", "transactionsByItem", "transactionsByDate", "transactionsByDueDate", "transactionsPastDueDate", "transactionsByStatus"}, allEntries = true)
  public Transaction update(Transaction transaction) {
    if (transaction == null || transaction.getId() == null) {
      return null;
    }

    Transaction updatedTransaction = transactionRepository.save(transaction);
    return updatedTransaction;
  }

  @Override
  @Cacheable(value = "transactionsByDate", key = "#depositDate.toString()")
  public Page<Transaction> findByDepositeDate(Date depositDate, Pageable pageable) {
    return transactionRepository.findByDepositDate(depositDate, pageable);
  }

  @Override
  @Cacheable(value = "transactionsByDueDate", key = "#dueDate.toString()")
  public Page<Transaction> findByDueDate(Date dueDate, Pageable pageable) {
    return transactionRepository.findByDueDate(dueDate, pageable);
  }

  @Override
  @Cacheable(value = "transactionsPastDueDate", key = "#dueDate.toString()")
  public Page<Transaction> transactionsPastDueDate(Date dueDate, Pageable pageable) {
    return transactionRepository.findByDueDateBeforeAndStatusNot(dueDate, "COLLECTED", pageable);
  }

  @Override
  @Cacheable(value = "transactionsByStatus", key = "#status.toString()")
  public Page<Transaction> findByStatus(Collection<String> status, Pageable pageable) {
    return transactionRepository.findByStatusIn(status, pageable);
  }
}
