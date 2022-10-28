CREATE TABLE users (
	user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL
);

CREATE TABLE credentials (
	user_name VARCHAR(20) PRIMARY KEY,
	user_password VARCHAR(20) NOT NULL,
	user_pin INT NOT NULL CHECK(user_pin >= 1000 AND user_pin < 10000),
	user_maiden VARCHAR(20) NOT NULL,
	user_id INT NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE cascade
);

CREATE TABLE inactive_users (
	inactive_user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	user_name VARCHAR(20) NOT NULL,
	user_password VARCHAR(20) NOT NULL,
	user_pin INT NOT NULL,
	user_maiden VARCHAR(20) NOT NULL
);

CREATE TABLE sessions (
	session_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	user_name VARCHAR(20) NOT NULL,
	login_time VARCHAR(50) NOT NULL,
	logout_time VARCHAR(50) NOT NULL
);

CREATE TABLE accounts (
	account_no INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	account_type VARCHAR(20) NOT NULL,
	account_balance DOUBLE PRECISION CHECK(account_balance >= 0),
	access_code INT CHECK(access_code >=1000 AND access_code < 10000) UNIQUE,
	pending DOUBLE PRECISION
);

CREATE TABLE accountUsers (
	account_no INT NOT NULL,
	user_id INT NOT NULL,
	PRIMARY KEY(account_no, user_id)
);

CREATE TABLE transactions (
	transaction_number INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	account_number INT NOT NULL,
	time VARCHAR(50) NOT NULL,
	transaction_type VARCHAR(20) NOT NULL,
	transaction_amount DOUBLE PRECISION NOT NULL,
	updated_balance DOUBLE PRECISION NOT NULL,
	user_id INT NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY(account_number) REFERENCES accounts(account_no) ON DELETE cascade
);

INSERT INTO users(first_name, last_name) VALUES('Robert', 'Smith');
INSERT INTO credentials(user_name, user_password, user_pin, user_maiden, user_id) VALUES('bobby', 'pass000', 1111, 'Jones', 1);
