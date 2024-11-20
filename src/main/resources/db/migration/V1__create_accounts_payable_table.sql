CREATE TABLE accounts_payable (
    id SERIAL PRIMARY key,
    due_date DATE NOT NULL,
    payment_date DATE,
    value DECIMAL(15, 2) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(15) NOT NULL
);