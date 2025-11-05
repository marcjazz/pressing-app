CREATE TABLE IF NOT EXISTS categories (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS cleaning_materials (
  id BIGSERIAL PRIMARY KEY,
  cost DOUBLE PRECISION NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS customers (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGSERIAL PRIMARY KEY,
  cost DOUBLE PRECISION NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  category_id BIGINT NOT NULL,
  UNIQUE (name),
  FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS customer_items (
  id BIGSERIAL PRIMARY KEY,
  deposit_date TIMESTAMP NOT NULL,
  due_date TIMESTAMP NOT NULL,
  label VARCHAR(255) NOT NULL,
  quantity INT NOT NULL,
  status VARCHAR(255) NOT NULL,
  customer_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  UNIQUE (label),
  FOREIGN KEY (customer_id) REFERENCES customers (id),
  FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE IF NOT EXISTS expenses (
  id BIGSERIAL PRIMARY KEY,
  depreciation_date TIMESTAMP NOT NULL,
  purchased_date TIMESTAMP NOT NULL,
  quantity INT NOT NULL,
  cleaning_material_id BIGINT NOT NULL,
  FOREIGN KEY (cleaning_material_id) REFERENCES cleaning_materials (id)
);

CREATE TABLE IF NOT EXISTS payment_methods (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  is_active BOOLEAN NOT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS payments (
  id BIGSERIAL PRIMARY KEY,
  amount DOUBLE PRECISION NOT NULL,
  "time" TIMESTAMP NOT NULL,
  customer_item_id BIGINT NOT NULL,
  payment_method_id BIGINT NOT NULL,
  FOREIGN KEY (customer_item_id) REFERENCES customer_items (id),
  FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id)
);

CREATE TABLE IF NOT EXISTS permissions (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS roles (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS role_permissions (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles (id),
  FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE TABLE IF NOT EXISTS "users" (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES "users" (id),
  FOREIGN KEY (role_id) REFERENCES roles (id)
);
