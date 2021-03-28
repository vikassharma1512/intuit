DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS bidder;
DROP TABLE IF EXISTS bid;
DROP TABLE IF EXISTS bid_winner;

--Create customer table
CREATE TABLE customer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(245) DEFAULT NULL
);

--Create project table
CREATE TABLE project (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(245) DEFAULT NULL,
  deadline DATE DEFAULT NULL,
  estimated_hours INT DEFAULT NULL,
  customer_id INT DEFAULT NULL
);

--Create bidder table
CREATE TABLE bidder (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(245) DEFAULT NULL
);

-- Create bid Table
CREATE TABLE bid (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fixed_price DOUBLE DEFAULT NULL,
  hourly_price DOUBLE DEFAULT NULL,
  project_id INT DEFAULT NULL,
  bidder_id INT DEFAULT NULL,
  create_date DATETIME DEFAULT NULL
);

--Create bid_winner table
CREATE TABLE bid_winner (
  project_id INT NOT NULL,
  bid_id INT NOT NULL,
  bidder_id INT NOT NULL,
  quote DOUBLE DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (project_id,bid_id,bidder_id)
);