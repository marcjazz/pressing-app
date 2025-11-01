package com.pressing.service.implementation;

import com.pressing.model.Customer;
import com.pressing.repository.CustomerRepository;
import com.pressing.service.CustomerService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class CustomerServiceImpl implements CustomerService {

  @Autowired private CustomerRepository customerRepository;

  @Override
  public Collection<Customer> findAll() {

    Collection<Customer> customers = customerRepository.findAll();
    return customers;
  }

  @Override
  public Customer findById(Long id) {

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
  public Customer create(Customer customer) {

    if (findByEmail(customer.getEmail()) == null) {
      Customer savedCustomer = customerRepository.save(customer);
      return savedCustomer;
    }

    return null;
  }

  @Override
  public Customer update(Customer customer) {

    Customer savedCustomer = customerRepository.save(customer);
    return savedCustomer;
  }

  @Override
  public void deactivate(Long id) {

    Customer customer = findById(id);
    if (customer == null) {
      return;
    }

    customer.setActive(false);
  }
}
