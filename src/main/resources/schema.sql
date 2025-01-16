
CREATE TABLE IF NOT EXISTS tenpista (
    nm_tenpista_id   SERIAL PRIMARY KEY,
    va_name VARCHAR(100) NOT null
);

CREATE TABLE IF NOT EXISTS transaction (
    nm_transaction_id   SERIAL PRIMARY KEY,
    nm_tenpista_id      INT NOT NULL,
    nm_amount           INT NOT NULL CHECK (nm_amount >= 0), 
    va_giro             VARCHAR(100) NOT NULL,
    fe_transaction_date TIMESTAMP NOT NULL CHECK (fe_transaction_date <= NOW()),
    
    -- Clave foránea que apunta al Tenpista
    CONSTRAINT fk_tenpista
        FOREIGN KEY (nm_tenpista_id)
        REFERENCES tenpista(nm_tenpista_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

/*

INSERT INTO tenpista (va_name) VALUES ('Juan Pérez');

INSERT INTO transaction (nm_amount, va_giro, fe_transaction_date)
VALUES (5000, 'Supermercado XYZ', '2025-01-10 10:00:00');
*/