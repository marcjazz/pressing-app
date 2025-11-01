package com.pressing.service.implementation;

import com.pressing.model.Transaction;
import com.pressing.repository.TransactionRepository;
import com.pressing.service.TransactionService;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_SALES_AGENT", "ROLE_ADMINISTRATION"})
public class TransactionServiceImpl implements TransactionService {

  @Autowired private TransactionRepository transactionRepository;

  @Override
  public Collection<Transaction> findAll() {

    Collection<Transaction> transactions = transactionRepository.findAll();
    return transactions;
  }

  @Override
  public Transaction findById(Long transactionId) {
    if (transactionId == null) {
      return null;
    }

    Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
    return transaction;
  }

  @Override
  public Collection<Transaction> findByCustomerId(Long customerId) {

    Collection<Transaction> transaction = transactionRepository.findByCustomerId(customerId);
    return transaction;
  }

  @Override
  public Collection<Transaction> findByItemId(Long itemId) {

    Collection<Transaction> transaction = transactionRepository.findByItemId(itemId);
    return transaction;
  }

  @Override
  public Transaction create(Transaction transaction) {
    if (transaction == null) {
      return null;
    }

    Transaction savedCustomerItem = transactionRepository.save(transaction);
    return savedCustomerItem;
  }

  @Override
  public Transaction update(Transaction transaction) {
    if (transaction == null || transaction.getId() == null) {
      return null;
    }

    Transaction updatedTransaction = transactionRepository.save(transaction);
    return updatedTransaction;
  }

  @Override
  public Collection<Transaction> findByDepositeDate(Date depositDate) {

    Collection<Transaction> transaction = transactionRepository.findByDepositDate(depositDate);
    return transaction;
  }

  @Override
  public Collection<Transaction> findByDueDate(Date dueDate) {

    Collection<Transaction> transaction = transactionRepository.findByDueDate(dueDate);
    return transaction;
  }

  @Override
  public Collection<Transaction> transactionsPastDueDate(Date dueDate) {

    Collection<Transaction> transactions =
        transactionRepository.findByDueDateBeforeAndStatusNot(dueDate, "COLLECTED");
    return transactions;
  }

  @Override
  public Collection<Transaction> findByStatus(Collection<String> status) {
    Collection<Transaction> transactions = transactionRepository.findByStatusIn(status);
    return transactions;
  }
}
