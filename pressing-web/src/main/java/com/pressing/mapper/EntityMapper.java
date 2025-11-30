package com.pressing.mapper;

import com.pressing.model.Agency;
import com.pressing.model.AgencyDTO;
import com.pressing.model.Category;
import com.pressing.model.CategoryDTO;
import com.pressing.model.CleaningMaterial;
import com.pressing.model.CleaningMaterialDTO;
import com.pressing.model.Counter;
import com.pressing.model.CounterDTO;
import com.pressing.model.Customer;
import com.pressing.model.CustomerDTO;
import com.pressing.model.Item;
import com.pressing.model.ItemDTO;
import com.pressing.model.MaterialPurchase;
import com.pressing.model.MaterialPurchaseDTO;
import com.pressing.model.Merchant;
import com.pressing.model.MerchantDTO;
import com.pressing.model.PaymentMethod;
import com.pressing.model.PaymentMethodDTO;
import com.pressing.model.Permission;
import com.pressing.model.PermissionDTO;
import com.pressing.model.Plan;
import com.pressing.model.PlanDTO;
import com.pressing.model.RoleDTO;
import com.pressing.model.Transaction;
import com.pressing.model.TransactionDTO;
import com.pressing.service.AgencyService;
import com.pressing.service.CategoryService;
import com.pressing.service.CleaningMaterialService;
import com.pressing.service.CustomerService;
import com.pressing.service.ItemService;
import com.pressing.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

  @Autowired private CategoryService categoryService;
  @Autowired private AgencyService agencyService;
  @Autowired private CleaningMaterialService cleaningMaterialService;
  @Autowired private CustomerService customerService;
  @Autowired private ItemService itemService;
  @Autowired private PlanService planService;

  public CategoryDTO toDTO(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
  }

  public Category toEntity(CategoryDTO categoryDTO) {
    if (categoryDTO == null) {
      return null;
    }
    Category category = new Category();
    category.setId(categoryDTO.getId());
    category.setName(categoryDTO.getName());
    category.setDescription(categoryDTO.getDescription());
    return category;
  }

  public RoleDTO toDTO(com.pressing.model.Role role) {
    if (role == null) {
      return null;
    }
    java.util.ArrayList<Long> permissionIds = new java.util.ArrayList<>();
    if (role.getPermissions() != null) {
      for (Permission permission : role.getPermissions()) {
        permissionIds.add(permission.getId());
      }
    }
    return new RoleDTO(role.getId(), role.getName(), role.getDescription(), permissionIds);
  }

  public com.pressing.model.Role toEntity(RoleDTO roleDTO) {
    if (roleDTO == null) {
      return null;
    }
    com.pressing.model.Role role = new com.pressing.model.Role();
    role.setId(roleDTO.getId());
    role.setName(roleDTO.getName());
    role.setDescription(roleDTO.getDescription());
    return role;
  }

  public com.pressing.model.PaymentDTO toDTO(com.pressing.model.Payment payment) {
    if (payment == null) {
      return null;
    }
    Long customerItemId =
        payment.getCustomerItem() != null ? payment.getCustomerItem().getId() : null;
    Long paymentMethodId =
        payment.getPaymentMethod() != null ? payment.getPaymentMethod().getId() : null;
    return new com.pressing.model.PaymentDTO(
        customerItemId, paymentMethodId, payment.getTime(), payment.getAmount());
  }

  public com.pressing.model.Payment toEntity(com.pressing.model.PaymentDTO paymentDTO) {
    if (paymentDTO == null) {
      return null;
    }
    com.pressing.model.Payment payment = new com.pressing.model.Payment();
    payment.setAmount(paymentDTO.getAmount());
    payment.setTime(paymentDTO.getPaymentDate());
    return payment;
  }

  public com.pressing.model.UserDTO toDTO(com.pressing.model.CustomUser user) {
    if (user == null) {
      return null;
    }
    java.util.ArrayList<Long> roleIds = new java.util.ArrayList<>();
    if (user.getRoles() != null) {
      for (com.pressing.model.Role role : user.getRoles()) {
        roleIds.add(role.getId());
      }
    }
    return new com.pressing.model.UserDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getUsername(),
        user.getPassword(),
        user.isActive(),
        roleIds,
        user.getTelephone());
  }

  public com.pressing.model.CustomUser toEntity(com.pressing.model.UserDTO userDTO) {
    if (userDTO == null) {
      return null;
    }
    com.pressing.model.CustomUser user = new com.pressing.model.CustomUser();
    user.setId(userDTO.getId());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setUsername(userDTO.getUsername());
    user.setPassword(userDTO.getPassword());
    user.setTelephone(userDTO.getTelephone());
    user.setActive(userDTO.isActive());
    return user;
  }

  public ItemDTO toDTO(Item item) {
    if (item == null) {
      return null;
    }
    Long categoryId = item.getCategory() != null ? item.getCategory().getId() : null;
    return new ItemDTO(
        item.getId(), item.getName(), item.getDescription(), item.getCost(), categoryId);
  }

  public Item toEntity(ItemDTO itemDTO) {
    if (itemDTO == null) {
      return null;
    }
    Item item = new Item();
    item.setId(itemDTO.getId());
    item.setName(itemDTO.getName());
    item.setDescription(itemDTO.getDescription());
    item.setCost(itemDTO.getCost());
    if (itemDTO.getCategoryId() != null) {
      Category category = categoryService.findById(itemDTO.getCategoryId());
      item.setCategory(category);
    }
    return item;
  }

  public AgencyDTO toDTO(Agency agency) {
    if (agency == null) {
      return null;
    }
    return new AgencyDTO(
        agency.getId(),
        agency.getName(),
        agency.getAddress(),
        agency.getTelephone(),
        null); // Email is not present in Agency entity
  }

  public Agency toEntity(AgencyDTO agencyDTO) {
    if (agencyDTO == null) {
      return null;
    }
    Agency agency = new Agency();
    agency.setId(agencyDTO.getId());
    agency.setName(agencyDTO.getName());
    agency.setAddress(agencyDTO.getAddress());
    agency.setTelephone(agencyDTO.getTelephone());
    // agency.setEmail(agencyDTO.getEmail()); // Email is not present in Agency entity
    return agency;
  }

  public CleaningMaterialDTO toDTO(CleaningMaterial material) {
    if (material == null) {
      return null;
    }
    return new CleaningMaterialDTO(
        material.getId(),
        material.getName(),
        material.getDescription(),
        material.getCost(),
        0.0, // Quantity is not present in CleaningMaterial entity
        ""); // Unit is not present in CleaningMaterial entity
  }

  public CleaningMaterial toEntity(CleaningMaterialDTO materialDTO) {
    if (materialDTO == null) {
      return null;
    }
    CleaningMaterial material = new CleaningMaterial();
    material.setId(materialDTO.getId());
    material.setName(materialDTO.getName());
    material.setDescription(materialDTO.getDescription());
    material.setCost(materialDTO.getCost());
    // material.setQuantity(materialDTO.getQuantity()); // Quantity is not present in CleaningMaterial entity
    // material.setUnit(materialDTO.getUnit()); // Unit is not present in CleaningMaterial entity
    return material;
  }

  public CounterDTO toDTO(Counter counter) {
    if (counter == null) {
      return null;
    }
    Long agencyId = counter.getAgency() != null ? counter.getAgency().getId() : null;
    return new CounterDTO(
        counter.getId(), counter.getName(), "", agencyId); // Location is not present in Counter entity
  }

  public Counter toEntity(CounterDTO counterDTO) {
    if (counterDTO == null) {
      return null;
    }
    Counter counter = new Counter();
    counter.setId(counterDTO.getId());
    counter.setName(counterDTO.getName());
    // counter.setLocation(counterDTO.getLocation()); // Location is not present in Counter entity
    if (counterDTO.getAgencyId() != null) {
      Agency agency = agencyService.findById(counterDTO.getAgencyId());
      counter.setAgency(agency);
    }
    return counter;
  }

  public CustomerDTO toDTO(Customer customer) {
    if (customer == null) {
      return null;
    }
    return new CustomerDTO(
        customer.getId(),
        customer.getFirstName(),
        customer.getLastName(),
        customer.getEmail(),
        customer.getTelephone(),
        customer.isActive());
  }

  public Customer toEntity(CustomerDTO customerDTO) {
    if (customerDTO == null) {
      return null;
    }
    Customer customer = new Customer();
    customer.setId(customerDTO.getId());
    customer.setFirstName(customerDTO.getFirstName());
    customer.setLastName(customerDTO.getLastName());
    customer.setEmail(customerDTO.getEmail());
    customer.setTelephone(customerDTO.getTelephone());
    customer.setActive(customerDTO.isActive());
    return customer;
  }

  public MaterialPurchaseDTO toDTO(MaterialPurchase purchase) {
    if (purchase == null) {
      return null;
    }
    Long materialId =
        purchase.getCleaningMaterial() != null ? purchase.getCleaningMaterial().getId() : null;
    return new MaterialPurchaseDTO(
        purchase.getId(),
        materialId,
        (double) purchase.getQuantity(),
        0.0, // Cost is not present in MaterialPurchase entity
        purchase.getPurchasedDate());
  }

  public MaterialPurchase toEntity(MaterialPurchaseDTO purchaseDTO) {
    if (purchaseDTO == null) {
      return null;
    }
    MaterialPurchase purchase = new MaterialPurchase();
    purchase.setId(purchaseDTO.getId());
    purchase.setQuantity(purchaseDTO.getQuantity().intValue());
    // purchase.setCost(purchaseDTO.getCost()); // Cost is not present in MaterialPurchase entity
    purchase.setPurchasedDate(purchaseDTO.getPurchaseDate());
    if (purchaseDTO.getCleaningMaterialId() != null) {
      CleaningMaterial material =
          cleaningMaterialService.findById(purchaseDTO.getCleaningMaterialId());
      purchase.setCleaningMaterial(material);
    }
    return purchase;
  }

  public MerchantDTO toDTO(Merchant merchant) {
    if (merchant == null) {
      return null;
    }
    Integer planId = merchant.getPlan() != null ? merchant.getPlan().getId().intValue() : null;
    return new MerchantDTO(
        merchant.getId(),
        merchant.getName(),
        "", // Address is not present in Merchant entity
        merchant.getTelephone(),
        merchant.getEmail(),
        planId);
  }

  public Merchant toEntity(MerchantDTO merchantDTO) {
    if (merchantDTO == null) {
      return null;
    }
    Merchant merchant = new Merchant();
    merchant.setId(merchantDTO.getId());
    merchant.setName(merchantDTO.getName());
    // merchant.setAddress(merchantDTO.getAddress()); // Address is not present in Merchant entity
    merchant.setTelephone(merchantDTO.getTelephone());
    merchant.setEmail(merchantDTO.getEmail());
    if (merchantDTO.getPlanId() != null) {
      Plan plan = planService.findById(merchantDTO.getPlanId());
      merchant.setPlan(plan);
    }
    return merchant;
  }

  public PaymentMethodDTO toDTO(PaymentMethod method) {
    if (method == null) {
      return null;
    }
    return new PaymentMethodDTO(
        method.getId(), method.getName(), method.getDescription(), method.isActive());
  }

  public PaymentMethod toEntity(PaymentMethodDTO methodDTO) {
    if (methodDTO == null) {
      return null;
    }
    PaymentMethod method = new PaymentMethod();
    method.setId(methodDTO.getId());
    method.setName(methodDTO.getName());
    method.setDescription(methodDTO.getDescription());
    method.setActive(methodDTO.isActive());
    return method;
  }

  public PermissionDTO toDTO(Permission permission) {
    if (permission == null) {
      return null;
    }
    return new PermissionDTO(
        permission.getId(), permission.getName(), permission.getDescription());
  }

  public Permission toEntity(PermissionDTO permissionDTO) {
    if (permissionDTO == null) {
      return null;
    }
    Permission permission = new Permission();
    permission.setId(permissionDTO.getId());
    permission.setName(permissionDTO.getName());
    permission.setDescription(permissionDTO.getDescription());
    return permission;
  }

  public PlanDTO toDTO(Plan plan) {
    if (plan == null) {
      return null;
    }
    return new PlanDTO(
        plan.getId().intValue(),
        plan.getName(),
        "", // Description is not present in Plan entity
        0.0, // Price is not present in Plan entity
        plan.getMaxAgencies(),
        plan.getMaxCounters(),
        0); // MaxUsers is not present in Plan entity
  }

  public Plan toEntity(PlanDTO planDTO) {
    if (planDTO == null) {
      return null;
    }
    Plan plan = new Plan();
    plan.setId(planDTO.getId().longValue());
    plan.setName(planDTO.getName());
    // plan.setDescription(planDTO.getDescription()); // Description is not present in Plan entity
    // plan.setPrice(planDTO.getPrice()); // Price is not present in Plan entity
    plan.setMaxAgencies(planDTO.getMaxAgencies());
    plan.setMaxCounters(planDTO.getMaxCounters());
    // plan.setMaxUsers(planDTO.getMaxUsers()); // MaxUsers is not present in Plan entity
    return plan;
  }

  public TransactionDTO toDTO(Transaction transaction) {
    if (transaction == null) {
      return null;
    }
    Long customerId =
        transaction.getCustomer() != null ? transaction.getCustomer().getId() : null;
    Long itemId = transaction.getItem() != null ? transaction.getItem().getId() : null;
    return new TransactionDTO(
        transaction.getId(),
        customerId,
        itemId,
        transaction.getQuantity(),
        transaction.getStatus(),
        transaction.getLabel(),
        transaction.getDepositDate(),
        transaction.getDueDate());
  }

  public Transaction toEntity(TransactionDTO transactionDTO) {
    if (transactionDTO == null) {
      return null;
    }
    Transaction transaction = new Transaction();
    transaction.setId(transactionDTO.getId());
    transaction.setQuantity(transactionDTO.getQuantity());
    transaction.setStatus(transactionDTO.getStatus());
    transaction.setLabel(transactionDTO.getLabel());
    transaction.setDepositDate(transactionDTO.getDepositDate());
    transaction.setDueDate(transactionDTO.getDueDate());
    if (transactionDTO.getCustomerId() != null) {
      Customer customer = customerService.findById(transactionDTO.getCustomerId());
      transaction.setCustomer(customer);
    }
    if (transactionDTO.getItemId() != null) {
      Item item = itemService.findById(transactionDTO.getItemId());
      transaction.setItem(item);
    }
    return transaction;
  }
}