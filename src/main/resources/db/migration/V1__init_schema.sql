-- ==========================================
-- V1__init_schema.sql
-- ==========================================

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles
(
    role_id     BIGSERIAL PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- =========================
-- BRANCHES
-- =========================
CREATE TABLE branches
(
    branch_id  BIGSERIAL PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    address    VARCHAR(255),
    phone      VARCHAR(30),
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- =========================
-- USERS (email + password + branch)
-- =========================
CREATE TABLE users
(
    user_id            BIGSERIAL PRIMARY KEY,
    full_name          VARCHAR(150) NOT NULL,
    email              VARCHAR(150) NOT NULL UNIQUE,
    phone              VARCHAR(30),
    password_hash      VARCHAR(255) NOT NULL,
    is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
    two_factor_enabled BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,

    role_id            BIGINT       NOT NULL,
    branch_id          BIGINT,

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles (role_id),

    CONSTRAINT fk_users_branch
        FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

-- =========================
-- CUSTOMERS
-- =========================
CREATE TABLE customers
(
    customer_id BIGSERIAL PRIMARY KEY,
    full_name   VARCHAR(150) NOT NULL,
    phone       VARCHAR(30),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- =========================
-- PRODUCTS
-- Enums as INTEGER:
-- - product_type (enum)
-- - status (enum)
-- Other attributes optional (nullable)
-- =========================
CREATE TABLE products
(
    product_id     BIGSERIAL PRIMARY KEY,
    product_code   VARCHAR(100)   NOT NULL UNIQUE, -- CUP
    name           VARCHAR(150)   NOT NULL,

    product_type   INTEGER        NOT NULL,        -- enum in Java (ordinal)
    size           VARCHAR(50),

    color          VARCHAR(50),
    brand          VARCHAR(100),
    location       VARCHAR(50),

    purchase_price NUMERIC(10, 2) NOT NULL CHECK (purchase_price >= 0),
    sale_price     NUMERIC(10, 2) NOT NULL CHECK (sale_price >= 0),

    width          NUMERIC(10, 2),
    height         NUMERIC(10, 2),
    depth          NUMERIC(10, 2),

    status         INTEGER        NOT NULL,        -- enum in Java (ordinal)

    created_at     TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP
);

-- =========================
-- BRANCH_INVENTORY (stock per branch)
-- Composite PK: (branch_id, product_id)
-- =========================
CREATE TABLE branch_inventory
(
    branch_id      BIGINT  NOT NULL,
    product_id     BIGINT  NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    updated_at     TIMESTAMP,

    PRIMARY KEY (branch_id, product_id),

    CONSTRAINT fk_branch_inventory_branch
        FOREIGN KEY (branch_id) REFERENCES branches (branch_id),

    CONSTRAINT fk_branch_inventory_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- =========================
-- SALES
-- Enums as INTEGER:
-- - status (enum)
-- =========================
CREATE TABLE sales
(
    sale_id     BIGSERIAL PRIMARY KEY,
    branch_id   BIGINT         NOT NULL,
    customer_id BIGINT,
    created_by  BIGINT         NOT NULL,

    total       NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    paid_total  NUMERIC(10, 2) NOT NULL DEFAULT 0 CHECK (paid_total >= 0),

    status      INTEGER        NOT NULL, -- enum in Java (ordinal)
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_sales_branch
        FOREIGN KEY (branch_id) REFERENCES branches (branch_id),

    CONSTRAINT fk_sales_customer
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id),

    CONSTRAINT fk_sales_user
        FOREIGN KEY (created_by) REFERENCES users (user_id),

    CONSTRAINT chk_sales_paid_not_over_total CHECK (paid_total <= total)
);

-- =========================
-- SALE_ITEMS (line items)
-- Composite PK: (sale_id, product_id)
-- Includes snapshot prices:
-- - unit_price: selling price at time of sale
-- - unit_cost: purchase cost at time of sale
-- =========================
CREATE TABLE sale_items
(
    sale_id    BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,

    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    unit_cost  NUMERIC(10, 2) NOT NULL CHECK (unit_cost >= 0),

    PRIMARY KEY (sale_id, product_id),

    CONSTRAINT fk_sale_items_sale
        FOREIGN KEY (sale_id) REFERENCES sales (sale_id),

    CONSTRAINT fk_sale_items_product
        FOREIGN KEY (product_id) REFERENCES products (product_id)
);

-- =========================
-- PAYMENTS
-- Enums as INTEGER:
-- - method (enum)
-- =========================
CREATE TABLE payments
(
    payment_id  BIGSERIAL PRIMARY KEY,
    sale_id     BIGINT         NOT NULL,
    branch_id   BIGINT         NOT NULL,
    customer_id BIGINT,
    created_by  BIGINT         NOT NULL,

    amount      NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    method      INTEGER        NOT NULL, -- enum in Java (ordinal)
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    notes       VARCHAR(255),

    CONSTRAINT fk_payments_sale
        FOREIGN KEY (sale_id) REFERENCES sales (sale_id),

    CONSTRAINT fk_payments_branch
        FOREIGN KEY (branch_id) REFERENCES branches (branch_id),

    CONSTRAINT fk_payments_customer
        FOREIGN KEY (customer_id) REFERENCES customers (customer_id),

    CONSTRAINT fk_payments_user
        FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- =========================
-- INVENTORY_MOVEMENTS
-- Enums as INTEGER:
-- - movement_type (IN, OUT, ADJUST)
-- reference_type (SALE, MANUAL, RETURN, INITIAL...)
-- =========================
CREATE TABLE inventory_movements
(
    movement_id    BIGSERIAL PRIMARY KEY,
    branch_id      BIGINT    NOT NULL,
    product_id     BIGINT    NOT NULL,
    created_by     BIGINT    NOT NULL,

    movement_type  INTEGER   NOT NULL, -- enum: IN, OUT, ADJUST
    quantity       INTEGER   NOT NULL CHECK (quantity > 0),

    reference_type INTEGER   NOT NULL, -- enum: SALE, RETURN, MANUAL, INITIAL
    reference_id   BIGINT,             -- nullable depending on reference_type

    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    notes          VARCHAR(255),

    CONSTRAINT fk_inventory_movements_branch
        FOREIGN KEY (branch_id) REFERENCES branches (branch_id),

    CONSTRAINT fk_inventory_movements_product
        FOREIGN KEY (product_id) REFERENCES products (product_id),

    CONSTRAINT fk_inventory_movements_user
        FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- =========================
-- OTP_CODES (2FA)
-- purpose stored as INTEGER enum (LOGIN, VERIFY, etc.)
-- code_hash stores hashed OTP (never store plain OTP)
-- =========================
CREATE TABLE otp_codes
(
    otp_id     BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,

    code_hash  VARCHAR(255) NOT NULL,
    purpose    INTEGER      NOT NULL, -- enum in Java (ordinal)
    attempts   INTEGER      NOT NULL DEFAULT 0 CHECK (attempts >= 0),

    expires_at TIMESTAMP    NOT NULL,
    used_at    TIMESTAMP,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_otp_codes_user
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);