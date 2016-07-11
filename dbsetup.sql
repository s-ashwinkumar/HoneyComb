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

Insert into fault (name,description,location,arguments,active) values ("TestFault","Description of test fault, this fault can be used to test termination","faults/TestFault.jar","nothing",true);

Insert into fault (name,description,location,arguments,active) values ("ChangeAmiInLcFault","Change ami for an ASG group","faults/ChangeAmiInLcFault.jar","asgName;faultyAmiId",true);

Insert into fault (name,description,location,arguments,active) values ("ElbUnavailableFault","Cannot create an asg when ELB is deleted","faults/ElbUnavailableFault.jar","asgName;elbName",true);

Insert into fault (name,description,location,arguments,active) values ("InstanceUnavailableFault","Delete an instance","faults/InstanceUnavailableFault.jar","instanceId",true);