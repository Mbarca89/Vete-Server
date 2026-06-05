-- =========================
-- FULL SCHEMA (H2)
-- =========================

CREATE TABLE IF NOT EXISTS Users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    category_name VARCHAR(50) NOT NULL,
    provider_name VARCHAR(50) NOT NULL,
    bar_code BIGINT UNIQUE,
    cost DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    stock_alert BOOLEAN DEFAULT FALSE,
    published BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    image BLOB,
    thumbnail BLOB
);

CREATE TABLE IF NOT EXISTS Category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Providers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    contact_name VARCHAR(50),
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ProductCategories (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE IF NOT EXISTS ProductProviders (
    product_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, provider_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (provider_id) REFERENCES Providers(id)
);

CREATE TABLE IF NOT EXISTS Clients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50),
    phone VARCHAR(50),
    email VARCHAR(50),
    social VARCHAR(50),
    user_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Pets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    public_id UUID DEFAULT RANDOM_UUID() NOT NULL,
    name VARCHAR(50) NOT NULL,
    race VARCHAR(50),
    gender VARCHAR(50),
    species VARCHAR(50),
    weight DOUBLE,
    born DATE,
    photo BLOB,
    thumbnail BLOB
);

CREATE TABLE IF NOT EXISTS ClientPets (
    client_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    PRIMARY KEY (client_id, pet_id),
    FOREIGN KEY (client_id) REFERENCES Clients(id),
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    sale_date TIMESTAMP NOT NULL,
    sale_amount DECIMAL(10, 2) NOT NULL,
    sale_cost DECIMAL(10, 2) NOT NULL,
    seller VARCHAR(50) NOT NULL,
    discount BOOLEAN DEFAULT FALSE,
    discount_amount DECIMAL(10, 2) DEFAULT 0
);

-- SalesProducts: snapshot + (opcional) product_id sin FK a Products
CREATE TABLE IF NOT EXISTS SalesProducts (
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    product_name VARCHAR(50),
    product_bar_code BIGINT,
    product_description VARCHAR(255),
    product_price DECIMAL(10,2),
    product_cost DECIMAL(10,2),

    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES Sales(id)
);

CREATE TABLE IF NOT EXISTS Orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    order_amount DECIMAL(10, 2) NOT NULL
);

-- OrdersProducts: snapshot + (opcional) product_id sin FK a Products
CREATE TABLE IF NOT EXISTS OrdersProducts (
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,

    product_name VARCHAR(50),
    product_bar_code BIGINT,
    product_description VARCHAR(255),
    product_price DECIMAL(10,2),
    product_cost DECIMAL(10,2),

    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(id)
);

CREATE TABLE IF NOT EXISTS medical_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    notes TEXT,
    description TEXT,
    medicine VARCHAR(100),
    pet_id BIGINT,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Vaccines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(50) NOT NULL,
    notes TEXT,
    sent BOOLEAN DEFAULT FALSE,
    failure_reason TEXT,
    pet_id BIGINT,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Reminders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(50) NOT NULL,
    notes TEXT,
    phone VARCHAR(50),
    sent BOOLEAN DEFAULT FALSE,
    failure_reason TEXT
);

CREATE TABLE IF NOT EXISTS Messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE DEFAULT CURRENT_DATE NOT NULL,
    client_name VARCHAR(50) NOT NULL,
    client_phone VARCHAR(50) NOT NULL,
    pet_name VARCHAR(50) NOT NULL,
    vaccine VARCHAR(50) NOT NULL,
    sent BOOLEAN DEFAULT FALSE,
    failure_reason TEXT
);

ALTER TABLE Vaccines ADD COLUMN IF NOT EXISTS sent BOOLEAN DEFAULT FALSE;
ALTER TABLE Vaccines ADD COLUMN IF NOT EXISTS failure_reason TEXT;
ALTER TABLE Reminders ADD COLUMN IF NOT EXISTS sent BOOLEAN DEFAULT FALSE;
ALTER TABLE Reminders ADD COLUMN IF NOT EXISTS failure_reason TEXT;
ALTER TABLE Messages ADD COLUMN IF NOT EXISTS failure_reason TEXT;

CREATE TABLE IF NOT EXISTS Payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE,
    bill_number VARCHAR(50),
    amount DECIMAL(10, 2),
    provider VARCHAR(50),
    payed BOOLEAN DEFAULT FALSE,
    payment_method VARCHAR(50),
    payment_date DATE
);

CREATE TABLE IF NOT EXISTS Bills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    fecha DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    numero BIGINT NOT NULL,
    tipo_documento INT NOT NULL,
    documento BIGINT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    importe_total DECIMAL(10, 2) NOT NULL,
    importe_no_gravado DECIMAL(10, 2) NOT NULL,
    importe_gravado DECIMAL(10, 2) NOT NULL,
    importe_iva DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cae VARCHAR(50) NOT NULL,
    cae_fch_vto VARCHAR(50) NOT NULL,
    errors VARCHAR(255) ARRAY,
    observations VARCHAR(255) ARRAY,
    condicion_iva_descripcion VARCHAR(255)
);

-- BillsProducts: snapshot, NO FK a Products (solo FK a Bills)
CREATE TABLE IF NOT EXISTS BillsProducts (
    bill_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    bar_code BIGINT NOT NULL,
    description VARCHAR(255),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    net_price DECIMAL(10,2) NOT NULL,
    iva DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (bill_id, product_id),
    FOREIGN KEY (bill_id) REFERENCES Bills(id)
);

CREATE TABLE IF NOT EXISTS web_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(100) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, APPROVED, REJECTED
    preference_id VARCHAR(255),
    payment_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shipped BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS web_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    web_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_web_order_items_order
        FOREIGN KEY (web_order_id) REFERENCES web_orders(id)
        ON DELETE CASCADE
);
