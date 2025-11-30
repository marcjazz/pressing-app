package com.pressing.service;

import com.pressing.model.Counter;
import com.pressing.model.Customer;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for customers */
public interface CustomerService {

  /**
   * Get all customers in the system.
   *
   * @return Collection of all customers
   */
  public Page<Customer> findAll(Pageable pageable);

  /**
   * Find a customer by Id.
   *
   * @param id
   * @return Customer object if found, else return null
   */
  public Customer findById(Long id);

  /**
   * Find a customer by email.
   *
   * @param email
   * @return Customer object if found, else return null
   */
  public Customer findByEmail(String email);

  /**
   * Find a customers with active/ deactivated accounts.
   *
   * @param isActive
   * @return Collection of customers
   */
  public Collection<Customer> findByIsActive(boolean isActive);

  public Page<Customer> findByCounter(Counter counter, Pageable pageable);

  public Page<Customer> findByIsActiveAndCounter(
      boolean isActive, Counter counter, Pageable pageable);

  /**
   * Create a new customer account.
   *
   * @param customer
   * @return Customer object (Created customer object)
   */
  public Customer create(Customer customer);

  /**
   * Update an existing customer account's information.
   *
   * @param customer
   * @return Customer object (Updated customer object)
   */
  public Customer update(Customer customer);

  /**
   * Deactivate a customer account from the system.
   *
   * @param id
   */
  public void deactivate(Long id);
}
