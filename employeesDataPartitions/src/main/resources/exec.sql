DROP TABLE employees if exists;

CREATE TABLE employees (
	employee_id INT NOT NULL PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30),
	location VARCHAR(30)
);