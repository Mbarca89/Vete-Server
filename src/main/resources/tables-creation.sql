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
    bar_code LONG UNIQUE,
    cost DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    image BLOB
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
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Pets (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    race VARCHAR(50),
    weight DOUBLE,
    born DATE,
    photo BLOB
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
    sale_date DATE NOT NULL,
    client_id LONG,
    sale_amount DECIMAL(10, 2) NOT NULL,
    seller_id LONG NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(id),
    FOREIGN KEY (seller_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS SalesProducts (
    sale_id LONG NOT NULL,
    product_id LONG NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES Sales(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE IF NOT EXISTS medical_history (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(50) NOT NULL,
    notes TEXT,
    description TEXT,
    medicine VARCHAR(100),
    pet_id LONG UNIQUE,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);