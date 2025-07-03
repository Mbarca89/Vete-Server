CREATE TABLE IF NOT EXISTS Users (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Products (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    category_name VARCHAR(50) NOT NULL,
    provider_name VARCHAR(50) NOT NULL,
    bar_code LONG UNIQUE,
    cost DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    stock_alert BOOLEAN DEFAULT FALSE,
    published BOOLEAN DEFAULT FALSE,
    image BLOB,
    thumbnail BLOB
);

CREATE TABLE IF NOT EXISTS Category (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Providers (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    contact_name VARCHAR(50),
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ProductCategories (
    product_id LONG NOT NULL,
    category_id LONG NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE IF NOT EXISTS ProductProviders (
    product_id LONG NOT NULL,
    provider_id LONG NOT NULL,
    PRIMARY KEY (product_id, provider_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (provider_id) REFERENCES Providers(id)
);

CREATE TABLE IF NOT EXISTS Clients (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50),
    phone VARCHAR(50),
    email VARCHAR(50),
    social VARCHAR(50),
    user_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Pets (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
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
    client_id LONG NOT NULL,
    pet_id LONG NOT NULL,
    PRIMARY KEY (client_id, pet_id),
    FOREIGN KEY (client_id) REFERENCES Clients(id),
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Sales (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    sale_date DATETIME NOT NULL,
    sale_amount DECIMAL(10, 2) NOT NULL,
    sale_cost DECIMAL(10, 2) NOT NULL,
    seller VARCHAR(50) NOT NULL,
    discount BOOLEAN DEFAULT false,
    discount_amount DECIMAL(10, 2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS SalesProducts (
    sale_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES Sales(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE IF NOT EXISTS Orders (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    order_date DATETIME NOT NULL,
    order_amount DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS OrdersProducts (
    order_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE IF NOT EXISTS medical_history (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    notes TEXT,
    description TEXT,
    medicine VARCHAR(100),
    pet_id LONG,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Vaccines (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(50) NOT NULL,
    notes TEXT,
    pet_id LONG,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Reminders (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    name VARCHAR(50) NOT NULL,
    notes TEXT,
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Messages (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE DEFAULT CURRENT_DATE NOT NULL,
    client_name VARCHAR(50) NOT NULL,
    client_phone VARCHAR(50) NOT NULL,
    pet_name VARCHAR(50) NOT NULL,
    vaccine VARCHAR(50) NOT NULL,
    sent BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS Payments (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE,
    bill_number VARCHAR(50),
    amount DECIMAL(10, 2),
    provider VARCHAR(50),
    payed BOOLEAN DEFAULT FALSE,
    payment_method VARCHAR(50),
    payment_date DATE
);

CREATE TABLE IF NOT EXISTS Bills (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    fecha DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    numero LONG NOT NULL,
    tipo_documento INT NOT NULL,
    documento LONG NOT NULL,
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
    condicion_iva_descripcion VARCHAR(255) -- 👈 nuevo campo
);


CREATE TABLE IF NOT EXISTS BillsProducts (
    bill_id LONG NOT NULL,
    product_id LONG NOT NULL,
    bar_code LONG NOT NULL,
    description VARCHAR(255),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    net_price DECIMAL(10,2) NOT NULL,
    iva DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (bill_id, product_id),
    FOREIGN KEY (bill_id) REFERENCES Bills(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);