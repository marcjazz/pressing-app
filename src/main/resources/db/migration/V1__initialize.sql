CREATE TABLE IF NOT EXISTS category (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS cleaning_material (
  id BIGSERIAL PRIMARY KEY,
  cost DOUBLE PRECISION NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS customer (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS item (
  id BIGSERIAL PRIMARY KEY,
  cost DOUBLE PRECISION NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  category_id BIGINT NOT NULL,
  UNIQUE (name),
  FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS customer_item (
  id BIGSERIAL PRIMARY KEY,
  deposit_date TIMESTAMP NOT NULL,
  due_date TIMESTAMP NOT NULL,
  label VARCHAR(255) NOT NULL,
  quantity INT NOT NULL,
  status VARCHAR(255) NOT NULL,
  customer_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  UNIQUE (label),
  FOREIGN KEY (customer_id) REFERENCES customer (id),
  FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE IF NOT EXISTS expense (
  id BIGSERIAL PRIMARY KEY,
  depreciation_date TIMESTAMP NOT NULL,
  purchased_date TIMESTAMP NOT NULL,
  quantity INT NOT NULL,
  cleaning_material_id BIGINT NOT NULL,
  FOREIGN KEY (cleaning_material_id) REFERENCES cleaning_material (id)
);

CREATE TABLE IF NOT EXISTS payment_method (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  is_active BOOLEAN NOT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS payment (
  id BIGSERIAL PRIMARY KEY,
  amount DOUBLE PRECISION NOT NULL,
  "time" TIMESTAMP NOT NULL,
  customer_item_id BIGINT NOT NULL,
  payment_method_id BIGINT NOT NULL,
  FOREIGN KEY (customer_item_id) REFERENCES customer_item (id),
  FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
);

CREATE TABLE IF NOT EXISTS permission (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS role (
  id BIGSERIAL PRIMARY KEY,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS role_permission (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES role (id),
  FOREIGN KEY (permission_id) REFERENCES permission (id)
);

CREATE TABLE IF NOT EXISTS "user" (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  telephone VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (role_id) REFERENCES role (id)
);
