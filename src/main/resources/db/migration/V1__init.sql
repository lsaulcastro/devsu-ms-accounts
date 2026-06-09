
CREATE TABLE customer_view (
                               customer_id     VARCHAR(30)  PRIMARY KEY,
                               name            VARCHAR(100) NOT NULL,
                               identification  VARCHAR(20),
                               active          BOOLEAN      NOT NULL,
                               synced_at       TIMESTAMP    NOT NULL
);

CREATE INDEX idx_customer_view_active ON customer_view (active);

CREATE TABLE accounts (
                          id                BIGSERIAL      PRIMARY KEY,
                          account_number    VARCHAR(30)    NOT NULL UNIQUE,
                          account_type      VARCHAR(20)    NOT NULL,
                          initial_balance   NUMERIC(19, 4) NOT NULL CHECK (initial_balance >= 0),
                          current_balance   NUMERIC(19, 4) NOT NULL CHECK (current_balance >= 0),
                          active            BOOLEAN        NOT NULL DEFAULT TRUE,
                          customer_id       VARCHAR(30)    NOT NULL,
                          version           BIGINT         NOT NULL DEFAULT 0
);

CREATE INDEX idx_accounts_account_number ON accounts (account_number);
CREATE INDEX idx_accounts_customer_id    ON accounts (customer_id);
CREATE INDEX idx_accounts_active         ON accounts (active);

CREATE TABLE movements (
                           id              BIGSERIAL      PRIMARY KEY,
                           account_id      BIGINT         NOT NULL REFERENCES accounts (id) ON DELETE RESTRICT,
                           movement_type   VARCHAR(20)    NOT NULL,
                           amount          NUMERIC(19, 4) NOT NULL CHECK (amount > 0),
                           balance         NUMERIC(19, 4) NOT NULL CHECK (balance >= 0),
                           date            TIMESTAMP      NOT NULL
);

CREATE INDEX idx_movements_account_id      ON movements (account_id);
CREATE INDEX idx_movements_date            ON movements (date);
CREATE INDEX idx_movements_account_date    ON movements (account_id, date);