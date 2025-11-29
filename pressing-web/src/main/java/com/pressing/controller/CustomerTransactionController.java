package com.pressing.controller;

import com.pressing.model.CustomerItem;
import com.pressing.model.Payment;
import com.pressing.model.PaymentDTO;
import com.pressing.model.Transaction;
import com.pressing.service.PaymentMethodService;
import com.pressing.service.PaymentService;
import com.pressing.service.TransactionService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
    origins = "*",
    methods = {
      RequestMethod.POST,
      RequestMethod.GET,
      RequestMethod.PUT,
      RequestMethod.DELETE,
      RequestMethod.OPTIONS
    })
@RequestMapping("api/v1/customertransactions")
public class CustomerTransactionController {

  @Autowired private TransactionService transactionService;

  @Autowired private PaymentService paymentService;

  @Autowired private PaymentMethodService paymentMethodService;

  @Autowired private com.pressing.service.UserService userService;

  /**
   * Get all transactions or a customer or item transaction with a given id.
   *
   * @param depositDate, dueDate, customerId, itemId
   * @return Collection of transactions or a customer's transactions with the given id.
   * @throws ParseException
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Transaction>> getTransactions(
      @RequestParam(value = "depositDate", required = false) String depositDate,
      @RequestParam(value = "dueDate", required = false) String dueDate,
      @RequestParam(value = "customerId", required = false) Long customerId,
      @RequestParam(value = "itemId", required = false) Long itemId,
      @RequestParam(value = "greatestDueDate", required = false) String greatestDueDate,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sort)
      throws ParseException {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

    Page<Transaction> transactions;
    if (depositDate != null) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = formatter.parse(depositDate);
      transactions = transactionService.findByDepositeDate(date, pageable);

    } else if (dueDate != null) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = formatter.parse(dueDate);
      transactions = transactionService.findByDueDate(date, pageable);

    } else if (greatestDueDate != null) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = formatter.parse(greatestDueDate);
      transactions = transactionService.transactionsPastDueDate(date, pageable);

    } else if (customerId != null) {
      transactions = transactionService.findByCustomerId(customerId, pageable);

    } else if (itemId != null) {
      transactions = transactionService.findByItemId(itemId, pageable);

    } else if (status != null) {
      List<String> statusList = new ArrayList<>();
      if (status.equalsIgnoreCase("clean")) {
        statusList.add("READY_AND_NOT_PAID");
        statusList.add("READY_AND_PAID");
        statusList.add("COLLECTED");
      } else if (status.equalsIgnoreCase("dirty")) {
        statusList.add("PENDING");
        statusList.add("WASHING");
      }
      transactions = transactionService.findByStatus(statusList, pageable);

    } else {
      transactions = transactionService.findAll(pageable);
    }

    return new ResponseEntity<>(transactions, HttpStatus.OK);
  }

  /**
   * Get transaction with given transaction id.
   *
   * @param transactionId
   * @return Transaction object or 404 if transaction is not found
   */
  @RequestMapping(
      value = "/{transactionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Transaction> getTransactionById(
      @NonNull @PathVariable("transactionId") Long transactionId) {

    Transaction transaction = transactionService.findById(transactionId);
    if (transaction == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }

  /**
   * Get payments made on transaction with given id.
   *
   * @param transactionId
   * @return collection of payments for a transaction or 404 if transactions is not found
   */
  @RequestMapping(
      value = "/{transactionId}/payments",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<Payment>> getTransactionPayments(
      @NonNull @PathVariable("transactionId") Long transactionId) {

    Collection<Payment> payments = transactionService.findById(transactionId).getPayments();
    if (payments == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(payments, HttpStatus.OK);
  }

  /**
   * Create new transaction record.
   *
   * @param customerItem
   * @return Transaction object (created Transaction object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Transaction> createTransaction(@RequestBody CustomerItem customerItem) {

    Transaction transaction =
        new Transaction(
            null, // id
            customerItem.getCustomer(),
            customerItem.getItem(),
            customerItem.getQuantity(),
            customerItem.getStatus(),
            customerItem.getLabel(),
            customerItem.getDepositDate(),
            customerItem.getDueDate(),
            userService.getCurrentUser().getCounter(),
            null // payments
            );
    transaction = transactionService.create(transaction);
    if (transaction == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
  }

  /**
   * Update customer item transaction object.
   *
   * @param customerItem
   * @return Transaction object (updated CustomerItem object).
   */
  @RequestMapping(
      value = "/{transactionId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Transaction> updateCustomerTransaction(
      @RequestBody CustomerItem customerItem) {

    Transaction transaction = transactionService.findById(customerItem.getId());
    transaction.setQuantity(customerItem.getQuantity());
    transaction.setStatus(customerItem.getStatus());
    transaction = transactionService.update(transaction);
    if (transaction == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }

  /**
   * Get Add payment to customer transaction.
   *
   * @param payment
   * @return corresponding Transaction object
   */
  @RequestMapping(
      value = "/{transactionId}/payments",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Payment> depositePayment(@RequestBody PaymentDTO paymentDTO) {
    Payment payment =
        new Payment(
            null, // id
            paymentDTO.getAmount(),
            paymentDTO.getPaymentDate(),
            transactionService.findById(paymentDTO.getCustomerItemId()),
            paymentMethodService.findById(paymentDTO.getPaymentMethodId()),
            userService.getCurrentUser().getCounter());
    Payment createdPayment = paymentService.create(payment);
    if (createdPayment == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(createdPayment, HttpStatus.OK);
  }
}
