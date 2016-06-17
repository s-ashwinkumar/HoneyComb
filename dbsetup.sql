CREATE TABLE fault
(
faultID int NOT NULL AUTO_INCREMENT,
name varchar(50) NOT NULL,
description TEXT,
location varchar(255) NOT NULL,
arguments varchar(500),
active BOOLEAN NOT NULL,
PRIMARY KEY (faultID)
);


ALTER TABLE fault AUTO_INCREMENT = 10000;

Insert into fault (name,description,location,arguments,active) values ("Test Fault","Description of test fault","/var/whatever","instancename;secondargument;thirdargument",true);
