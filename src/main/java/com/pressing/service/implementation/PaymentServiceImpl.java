package com.pressing.service.implementation;

import com.pressing.model.Payment;
import com.pressing.model.PaymentMethod;
import com.pressing.model.Transaction;
import com.pressing.repository.PaymentRepository;
import com.pressing.service.PaymentMethodService;
import com.pressing.service.PaymentService;
import com.pressing.service.TransactionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_SALES_AGENT", "ROLE_ADMINISTRATION"})
public class PaymentServiceImpl implements PaymentService {

  @Autowired private PaymentRepository paymentRepository;

  @Autowired private TransactionService transactionService;

  @Autowired private PaymentMethodService paymentMethodService;

  @Override
  public Collection<Payment> findAll() {

    Collection<Payment> payments = paymentRepository.findAll();
    return payments;
  }

  @Override
  public Collection<Payment> findPaymentByTransaction(Long transactionId) {

    Transaction transaction = transactionService.findById(transactionId);
    if (transaction == null) {
      return new ArrayList<>();
    }
    Collection<Payment> payments = paymentRepository.findByCustomerItem(transaction);
    return payments;
  }

  @Override
  public Collection<Payment> findByPaymentByTime(Date time) {

    Collection<Payment> payments = paymentRepository.findByTime(time);
    return payments;
  }

  @Override
  public Collection<Payment> findPaymentWithPaymentMethod(Long methodId) {

    PaymentMethod method = paymentMethodService.findById(methodId);
    if (method == null) {
      return new ArrayList<>();
    }
    Collection<Payment> payments = paymentRepository.findByPaymentMethod(method);
    System.out.println("Checking error");
    return payments;
  }

  @Override
  public Payment create(Payment payment) {

    Payment savedPayment = paymentRepository.save(payment);
    return savedPayment;
  }

  @Override
  public Payment update(Payment payment) {

    Payment updatedPayment = paymentRepository.save(payment);
    return updatedPayment;
  }
}
