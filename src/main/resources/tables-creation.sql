CREATE TABLE IF NOT EXISTS Users (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Category (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Products (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    bar_code LONG UNIQUE,
    cost DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    category_id LONG NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    seller VARCHAR(50) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id LONG NOT NULL,
    image BLOB,
    FOREIGN KEY (category_id) REFERENCES Category(id),
    FOREIGN KEY (provider_id) REFERENCES Providers(id)
);

CREATE TABLE IF NOT EXISTS Pets (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    image BLOB
);

CREATE TABLE IF NOT EXISTS Clients (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    pet_id LONG NOT NULL,
    pet_name VARCHAR(50) NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES Pets(id)
);

CREATE TABLE IF NOT EXISTS Sales (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    date VARCHAR(20) NOT NULL,
    client_id LONG,
    seller VARCHAR(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);


CREATE TABLE IF NOT EXISTS SalesProducts (
    sale_id LONG NOT NULL,
    product_id LONG NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES Sales(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE IF NOT EXISTS Providers (
    id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    contact_name VARCHAR(50),
    phone VARCHAR(50)
)
