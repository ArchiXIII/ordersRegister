CREATE TABLE IF NOT EXISTS customers(
 customer_id BIGINT GENERATED BY DEFAULT AS IDENTITY(start with 1) PRIMARY KEY,
name VARCHAR(30) NOT NULL,
 surname VARCHAR(30) NOT NULL,
 patronymic VARCHAR (30) NOT NULL,
 phone VARCHAR (20) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders(
 order_id BIGINT GENERATED BY DEFAULT AS IDENTITY(start with 1) PRIMARY KEY,
 description VARCHAR (250) NOT NULL,
 customer_id BIGINT NOT NULL,
 created_date TIMESTAMP (6) NOT NULL,
 end_works_date TIMESTAMP (6),
 price INT,
 state_order VARCHAR (30) NOT NULL,
FOREIGN KEY(customer_id) REFERENCES customers(customer_id)
);