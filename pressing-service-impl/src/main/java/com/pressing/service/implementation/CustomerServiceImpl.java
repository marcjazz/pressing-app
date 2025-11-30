package com.pressing.service.implementation;

import com.pressing.model.Counter;
import com.pressing.model.Customer;
import com.pressing.repository.CustomerRepository;
import com.pressing.service.CustomerService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class CustomerServiceImpl implements CustomerService {

  @Autowired private CustomerRepository customerRepository;

  @Override
  @Cacheable("customers")
  public Page<Customer> findAll(Pageable pageable) {
    return customerRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "customersByCounter", key = "#counter.id")
  public Page<Customer> findByCounter(Counter counter, Pageable pageable) {
    return customerRepository.findByCounter(counter, pageable);
  }

  @Override
  @Cacheable(value = "customersByActiveAndCounter", key = "#isActive + '-' + #counter.id")
  public Page<Customer> findByIsActiveAndCounter(
      boolean isActive, Counter counter, Pageable pageable) {
    return customerRepository.findByIsActiveAndCounter(isActive, counter, pageable);
  }

  @Override
  @Cacheable(value = "customer", key = "#id")
  public Customer findById(Long id) {
    if (id == null) {
      return null;
    }

    Customer customer = customerRepository.findById(id).orElse(null);
    return customer;
  }

  @Override
  public Customer findByEmail(String email) {

    Customer customer = customerRepository.findByEmail(email);
    return customer;
  }

  @Override
  public Collection<Customer> findByIsActive(boolean isActive) {

    Collection<Customer> customers = customerRepository.findByIsActive(isActive);
    return customers;
  }

  @Override
  @CacheEvict(
      value = {"customers", "customer", "customersByCounter", "customersByActiveAndCounter"},
      allEntries = true)
  public Customer create(Customer customer) {

    if (findByEmail(customer.getEmail()) == null) {
      Customer savedCustomer = customerRepository.save(customer);
      return savedCustomer;
    }

    return null;
  }

  @Override
  @CacheEvict(
      value = {"customers", "customer", "customersByCounter", "customersByActiveAndCounter"},
      allEntries = true)
  public Customer update(Customer customer) {
    if (customer == null) {
      return null;
    }

    Customer savedCustomer = customerRepository.save(customer);
    return savedCustomer;
  }

  @Override
  @CacheEvict(
      value = {"customers", "customer", "customersByCounter", "customersByActiveAndCounter"},
      allEntries = true)
  public void deactivate(Long id) {

    Customer customer = findById(id);
    if (customer == null) {
      return;
    }

    customer.setActive(false);
  }
}
