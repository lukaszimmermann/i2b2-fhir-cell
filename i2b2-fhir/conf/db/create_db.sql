create database i2b2fhir;
CREATE USER 'ifcu'@'localhost' identified by 'ifcp';
GRANT ALL ON i2b2fhir.* TO 'ifcu'@'localhost';
