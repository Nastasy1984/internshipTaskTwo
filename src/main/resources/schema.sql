CREATE TABLE IF NOT EXISTS users(
   user_id serial PRIMARY KEY,
   first_name VARCHAR (50) NOT NULL,
   last_name VARCHAR (50) NOT NULL,
   email VARCHAR (50),
   created_on TIMESTAMP (0) NOT NULL default now()
);

CREATE TABLE IF NOT EXISTS phone_numbers(
   number_id serial PRIMARY KEY,
   phone_number VARCHAR (30) NOT NULL UNIQUE,
   user_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);