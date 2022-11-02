BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, user_transactions CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transaction_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
   user_id int NOT NULL DEFAULT nextval('seq_user_id'),
   username varchar(50) NOT NULL,
   password_hash varchar(200) NOT NULL,
   CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
   CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
   account_id int NOT NULL DEFAULT nextval('seq_account_id'),
   user_id int NOT NULL,
   balance numeric(13, 2) NOT NULL,
   CONSTRAINT PK_account PRIMARY KEY (account_id),
   CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transaction_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE user_transactions (
transaction_id int NOT NULL DEFAULT nextval('seq_transaction_id'),
requesting_account_id int NOT NULL,
end_account_id int NOT NULL,
transfer_amount numeric(13, 2) NOT NULL,
end_user_approval boolean DEFAULT(FALSE),
CONSTRAINT PK_user_transactions PRIMARY KEY (transaction_id),
CONSTRAINT FK_user_transactions_account_r FOREIGN KEY (requesting_account_id) REFERENCES account (account_id),
CONSTRAINT FK_user_transactions_account_e FOREIGN KEY (end_account_id) REFERENCES account (account_id),
CONSTRAINT CK_transfer_amount CHECK(transfer_amount > 0)

);
--SELECT * FROM user_transactions;
--SELECT * FROM tenmo_user;
--SELECT * FROM account;
INSERT INTO tenmo_user(username,password_hash) VALUES ('ap66', 'password'),
INSERT INTO tenmo_user(username,password_hash) VALUES ('venty20', 'password'),
INSERT INTO tenmo_user(username,password_hash) VALUES ('hoyboi', 'password')
COMMIT;