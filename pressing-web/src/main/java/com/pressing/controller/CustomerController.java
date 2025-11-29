package com.pressing.controller;

import com.pressing.model.Counter;
import com.pressing.model.Customer;
import com.pressing.service.CustomerService;
import com.pressing.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/v1/customers")
public class CustomerController {

  @Autowired private CustomerService customerService;
  @Autowired private UserService userService;

  /**
   * Get all customers or collection of active customer or customer with a given email.
   *
   * @param customerEmail, isActive
   * @return Collection of customers or customer with the particular email
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Customer>> getCustomers(
      @RequestParam(value = "customerEmail", required = false) String customerEmail,
      @RequestParam(value = "active", required = false) String isActive, Pageable pageable) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    if (customerEmail != null) {
      Collection<Customer> customers = new ArrayList<>();
      Customer customer = customerService.findByEmail(customerEmail);
      if (customer != null && customer.getCounter().equals(counter)) {
        customers.add(customer);
      }
      Page<Customer> singleResult = new PageImpl<>(new ArrayList<>(customers));
      return new ResponseEntity<>(singleResult, HttpStatus.OK);
    } else if (isActive != null) {
      if (isActive.compareToIgnoreCase("true") == 0) {
        Page<Customer> activeCustomers = customerService.findByIsActiveAndCounter(true, counter, pageable);
        return new ResponseEntity<>(activeCustomers, HttpStatus.OK);
      } else if (isActive.compareToIgnoreCase("false") == 0) {
        Page<Customer> deactivatedCustomers = customerService.findByIsActiveAndCounter(false, counter, pageable);
        return new ResponseEntity<>(deactivatedCustomers, HttpStatus.OK);
      }
    } else {
      Page<Customer> allCustomers = customerService.findByCounter(counter, pageable);
      return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }
    return new ResponseEntity<>(Page.empty(), HttpStatus.OK);
  }

  /**
   * Get customer with given customer id.
   *
   * @param customerId
   * @return Customer object or 404 if customer is not found
   */
  @RequestMapping(
      value = "/{customerId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Customer customer = customerService.findById(customerId);
    if (customer == null || !customer.getCounter().equals(counter)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(customer, HttpStatus.OK);
  }

  /**
   * Create new customer.
   *
   * @param customer
   * @return Customer object (created object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    customer.setCounter(counter);

    customer = customerService.create(customer);
    if (customer == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(customer, HttpStatus.CREATED);
  }

  /**
   * Update customer.
   *
   * @param customer
   * @return Customer object (updated object)
   */
  @RequestMapping(
      value = "/{customerId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Customer existingCustomer = customerService.findById(customer.getId());
    if (existingCustomer == null || !existingCustomer.getCounter().equals(counter)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    customer.setCounter(counter);
    customer = customerService.update(customer);
    if (customer == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(customer, HttpStatus.OK);
  }

  /**
   * Deactivate a customer's account
   *
   * @param customerId
   * @return HTTP status, NON_CONTENT
   */
  @RequestMapping(
      value = "/{customerId}",
      method = RequestMethod.DELETE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Customer> deActivateCustomer(@PathVariable("customerId") Long customerId) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Customer customer = customerService.findById(customerId);
    if (customer == null || !customer.getCounter().equals(counter)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    customerService.deactivate(customerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
