DROP DATABASE IF EXISTS abernathy_clinic ;

CREATE DATABASE abernathy_clinic;
USE abernathy_clinic;

CREATE TABLE patients (
    patient_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    family VARCHAR(50) NOT NULL,
    given VARCHAR(50) NOT NULL,
    dob DATE NOT NULL,
    sex VARCHAR(1),
    address VARCHAR(100),
    phone VARCHAR(50)
);

INSERT INTO patients (family, given, dob, sex, address, phone) VALUES
('TestNone', 'Test', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
('TestBorderline', 'Test', '1945-06-24', 'M', '2 High St', '200-333-4444'),
('TestInDanger', 'Test', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
('TestEarlyOnset', 'Test', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666'),
('TestNone', 'Test2', '1996-12-31', 'F', '', '')
;
