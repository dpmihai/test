create database currencies;

DROP TABLE IF EXISTS RON_VALUES;
CREATE TABLE RON_VALUES ( 
	id INT NOT NULL AUTO_INCREMENT, 
	PRIMARY KEY(id),
    code varchar(3) NOT NULL, 
    value decimal(10,4),
    exchange_date datetime     
); 