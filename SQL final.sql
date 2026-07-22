DROP DATABASE IF EXISTS camera_rental_system;
CREATE DATABASE camera_rental_system;
USE camera_rental_system;

CREATE TABLE users (
    userId       VARCHAR(20)  PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    contactNo    VARCHAR(20),
    password     VARCHAR(255) NOT NULL,
    role         ENUM('ADMIN', 'STAFF') NOT NULL,
    createdAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
    userId       VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);

CREATE TABLE staff (
    userId       VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);

CREATE TABLE customer (
    customerId     VARCHAR(20) PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    nicNo          VARCHAR(20)  NOT NULL UNIQUE,
    address        VARCHAR(255),
    contactNo      VARCHAR(20),
    registeredDate DATE NOT NULL
);

CREATE TABLE equipment (
    equipmentId     VARCHAR(20) PRIMARY KEY,
    equipmentName   VARCHAR(100) NOT NULL,
    brand           VARCHAR(50),
    purchasePrice   DECIMAL(10,2) NOT NULL,
    rentalPerDay    DECIMAL(10,2) NOT NULL,
    equipmentStatus ENUM('AVAILABLE','RENTED','MAINTENANCE','RETIRED') DEFAULT 'AVAILABLE',
    equipmentType   ENUM('CAMERA_BODY','CAMERA_LENS','ACCESSORY') NOT NULL
);

CREATE TABLE camera_body (
    equipmentId  VARCHAR(20) PRIMARY KEY,
    model        VARCHAR(100),
    FOREIGN KEY (equipmentId) REFERENCES equipment(equipmentId) ON DELETE CASCADE
);

CREATE TABLE camera_lens (
    equipmentId  VARCHAR(20) PRIMARY KEY,
    model        VARCHAR(100),
    zoomRange    VARCHAR(50),
    aperture     VARCHAR(20),
    mountType    VARCHAR(50),
    FOREIGN KEY (equipmentId) REFERENCES equipment(equipmentId) ON DELETE CASCADE
);

-- Updated ENUM to support Tripods and other accessories from the inventory
CREATE TABLE accessories (
    equipmentId    VARCHAR(20) PRIMARY KEY,
    accessoryType  ENUM('FLASHLIGHT','GIMBAL_STABILIZER','MIC', 'TRIPOD', 'OTHER') NOT NULL,
    FOREIGN KEY (equipmentId) REFERENCES equipment(equipmentId) ON DELETE CASCADE
);

CREATE TABLE flashlight (
    equipmentId  VARCHAR(20) PRIMARY KEY,
    model        VARCHAR(100),
    flashType    VARCHAR(50),
    FOREIGN KEY (equipmentId) REFERENCES accessories(equipmentId) ON DELETE CASCADE
);

CREATE TABLE gimbal_stabilizer (
    equipmentId  VARCHAR(20) PRIMARY KEY,
    model        VARCHAR(100),
    gimbalType   VARCHAR(50),
    FOREIGN KEY (equipmentId) REFERENCES accessories(equipmentId) ON DELETE CASCADE
);

CREATE TABLE mic (
    equipmentId  VARCHAR(20) PRIMARY KEY,
    model        VARCHAR(100),
    micType      VARCHAR(50),
    FOREIGN KEY (equipmentId) REFERENCES accessories(equipmentId) ON DELETE CASCADE
);

CREATE TABLE maintenance_record (
    recordId         VARCHAR(20) PRIMARY KEY,
    equipmentId      VARCHAR(20) NOT NULL,
    serviceDate      DATE NOT NULL,
    cost             DECIMAL(10,2),
    issueDescription VARCHAR(255),
    FOREIGN KEY (equipmentId) REFERENCES equipment(equipmentId) ON DELETE CASCADE
);

CREATE TABLE rental_transaction (
    transactionId  VARCHAR(20) PRIMARY KEY,
    staffId        VARCHAR(20) NOT NULL,
    customerId     VARCHAR(20) NOT NULL,
    checkoutDate   DATE NOT NULL,
    dueDate        DATE NOT NULL,
    returnDate     DATE,
    lateFee        DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (staffId)    REFERENCES staff(userId),
    FOREIGN KEY (customerId) REFERENCES customer(customerId)
);

CREATE TABLE rental_transaction_equipment (
    transactionId  VARCHAR(20) NOT NULL,
    equipmentId    VARCHAR(20) NOT NULL,
    PRIMARY KEY (transactionId, equipmentId),
    FOREIGN KEY (transactionId) REFERENCES rental_transaction(transactionId) ON DELETE CASCADE,
    FOREIGN KEY (equipmentId)   REFERENCES equipment(equipmentId)
);

CREATE TABLE payment (
    paymentId      VARCHAR(20) PRIMARY KEY,
    transactionId  VARCHAR(20) NOT NULL UNIQUE,
    amount         DECIMAL(10,2) NOT NULL,
    paymentDate    DATE NOT NULL,
    paymentMethod  VARCHAR(50),
    FOREIGN KEY (transactionId) REFERENCES rental_transaction(transactionId) ON DELETE CASCADE
);

CREATE INDEX idx_equipment_status ON equipment(equipmentStatus);
CREATE INDEX idx_rental_due_date  ON rental_transaction(dueDate);
CREATE INDEX idx_rental_customer  ON rental_transaction(customerId);

-- =================================================================
-- SAMPLE USERS & CUSTOMERS
-- =================================================================
INSERT INTO users (userId, name, email, contactNo, password, role) VALUES
('U001', 'Nadeesha Perera',  'nadeesha.admin@camrent.lk', '0771234567', 'hashed_pw_1', 'ADMIN'),
('U002', 'Kasun Silva',      'kasun.staff@camrent.lk',    '0772345678', 'hashed_pw_2', 'STAFF');

INSERT INTO admin (userId) VALUES ('U001');
INSERT INTO staff (userId) VALUES ('U002');

INSERT INTO customer (customerId, name, nicNo, address, contactNo, registeredDate) VALUES
('C001', 'Ruwan Jayasuriya', '199012345678', 'No 12, Galle Road', '0711112222', '2026-01-15');

-- =================================================================
-- EQUIPMENT DATA (From Inventory List)
-- =================================================================

-- 1. CAMERA BODIES
INSERT INTO equipment (equipmentId, equipmentName, brand, purchasePrice, rentalPerDay, equipmentType) VALUES
('CAM001', 'Sony A7 iii', 'Sony', 450000.00, 4500.00, 'CAMERA_BODY'),
('CAM002', 'Sony A7 iv', 'Sony', 600000.00, 6000.00, 'CAMERA_BODY'),
('CAM003', 'Sony A7 v', 'Sony', 800000.00, 8000.00, 'CAMERA_BODY'),
('CAM004', 'Canon 6D', 'Canon', 200000.00, 2000.00, 'CAMERA_BODY'),
('CAM005', 'Canon 6D mark II', 'Canon', 350000.00, 3500.00, 'CAMERA_BODY'),
('CAM006', 'Canon R', 'Canon', 400000.00, 4000.00, 'CAMERA_BODY'),
('CAM007', 'Nikon D850', 'Nikon', 400000.00, 4000.00, 'CAMERA_BODY'),
('CAM008', 'Nikon D7500', 'Nikon', 350000.00, 3500.00, 'CAMERA_BODY'),
('CAM009', 'Nikon Z6', 'Nikon', 600000.00, 6000.00, 'CAMERA_BODY');

INSERT INTO camera_body (equipmentId, model) VALUES
('CAM001', 'A7 III'), ('CAM002', 'A7 IV'), ('CAM003', 'A7 V'),
('CAM004', '6D'), ('CAM005', '6D Mark II'), ('CAM006', 'EOS R'),
('CAM007', 'D850'), ('CAM008', 'D7500'), ('CAM009', 'Z6');

-- 2. LENSES
INSERT INTO equipment (equipmentId, equipmentName, brand, purchasePrice, rentalPerDay, equipmentType) VALUES
('LEN001', 'Sony 28-70 mm F3.5', 'Sony', 150000.00, 1500.00, 'CAMERA_LENS'),
('LEN002', 'Sony 70-200 mm F2.8', 'Sony', 500000.00, 5000.00, 'CAMERA_LENS'),
('LEN003', 'Sony 50 mm F1.8', 'Sony', 150000.00, 1500.00, 'CAMERA_LENS'),
('LEN004', 'Canon Sigma 35 mm F1.4', 'Sigma', 250000.00, 2500.00, 'CAMERA_LENS'),
('LEN005', 'Canon 24-105 mm F4', 'Canon', 150000.00, 1500.00, 'CAMERA_LENS'),
('LEN006', 'Canon 50 mm F1.8', 'Canon', 75000.00, 750.00, 'CAMERA_LENS'),
('LEN007', 'Nikon 50 mm F1.8', 'Nikon', 100000.00, 1000.00, 'CAMERA_LENS'),
('LEN008', 'Nikon 70-200 mm F2.8', 'Nikon', 300000.00, 3000.00, 'CAMERA_LENS'),
('LEN009', 'Nikon Z 50 mm F1.4', 'Nikon', 250000.00, 2500.00, 'CAMERA_LENS');

INSERT INTO camera_lens (equipmentId, model, zoomRange, aperture, mountType) VALUES
('LEN001', '28-70mm', '28-70mm', 'f/3.5', 'Sony E'),
('LEN002', '70-200mm', '70-200mm', 'f/2.8', 'Sony E'),
('LEN003', '50mm', '50mm', 'f/1.8', 'Sony E'),
('LEN004', 'Sigma 35mm Art', '35mm', 'f/1.4', 'Canon EF'),
('LEN005', '24-105mm', '24-105mm', 'f/4', 'Canon EF'),
('LEN006', '50mm', '50mm', 'f/1.8', 'Canon EF'),
('LEN007', '50mm', '50mm', 'f/1.8', 'Nikon F'),
('LEN008', '70-200mm', '70-200mm', 'f/2.8', 'Nikon F'),
('LEN009', 'Z 50mm', '50mm', 'f/1.4', 'Nikon Z');

-- 3. ACCESSORIES
INSERT INTO equipment (equipmentId, equipmentName, brand, purchasePrice, rentalPerDay, equipmentType) VALUES
-- Flash/Lights
('ACC001', 'Godox V1', 'Godox', 100000.00, 1000.00, 'ACCESSORY'),
('ACC002', 'AD600 full set', 'Godox', 350000.00, 3500.00, 'ACCESSORY'),
('ACC003', 'Video light 500K', 'Generic', 150000.00, 1500.00, 'ACCESSORY'),
('ACC004', 'Godox LC 500R RGB', 'Godox', 200000.00, 2000.00, 'ACCESSORY'),
('ACC005', 'Nantube light', 'Nanlite', 200000.00, 2000.00, 'ACCESSORY'),
('ACC006', 'Canon Trigger X2', 'Godox', 50000.00, 500.00, 'ACCESSORY'),
('ACC007', 'Nikon Trigger X2', 'Godox', 50000.00, 500.00, 'ACCESSORY'),
('ACC008', 'Snoot', 'Generic', 50000.00, 500.00, 'ACCESSORY'),
-- Gimbals & Tripods
('ACC009', 'Osmo Mobile 7', 'DJI', 200000.00, 2000.00, 'ACCESSORY'),
('ACC010', 'Ronin RS4 Gimbal', 'DJI', 400000.00, 4000.00, 'ACCESSORY'),
('ACC011', 'DJI Osmo Pocket 3', 'DJI', 250000.00, 2500.00, 'ACCESSORY'),
('ACC012', 'Photo Tripod', 'Generic', 100000.00, 1000.00, 'ACCESSORY'),
('ACC013', 'Video Tripod', 'Generic', 100000.00, 1000.00, 'ACCESSORY'),
('ACC014', 'Monopod', 'Generic', 50000.00, 500.00, 'ACCESSORY'),
-- Mics
('ACC015', 'RODE GO wireless Mic', 'Rode', 250000.00, 2500.00, 'ACCESSORY'),
('ACC016', 'RODE GO II Pro', 'Rode', 300000.00, 3000.00, 'ACCESSORY'),
('ACC017', 'DJI MIC', 'DJI', 250000.00, 2500.00, 'ACCESSORY'),
('ACC018', 'BOOM MIC', 'Generic', 300000.00, 3000.00, 'ACCESSORY'),
('ACC019', 'PODCAST MIC with Arm', 'Generic', 300000.00, 3000.00, 'ACCESSORY'),
('ACC020', 'MIXER', 'Generic', 300000.00, 3000.00, 'ACCESSORY');

INSERT INTO accessories (equipmentId, accessoryType) VALUES
('ACC001', 'FLASHLIGHT'), ('ACC002', 'FLASHLIGHT'), ('ACC003', 'FLASHLIGHT'), 
('ACC004', 'FLASHLIGHT'), ('ACC005', 'FLASHLIGHT'), ('ACC006', 'OTHER'), 
('ACC007', 'OTHER'), ('ACC008', 'OTHER'),
('ACC009', 'GIMBAL_STABILIZER'), ('ACC010', 'GIMBAL_STABILIZER'), ('ACC011', 'GIMBAL_STABILIZER'), 
('ACC012', 'TRIPOD'), ('ACC013', 'TRIPOD'), ('ACC014', 'TRIPOD'),
('ACC015', 'MIC'), ('ACC016', 'MIC'), ('ACC017', 'MIC'), 
('ACC018', 'MIC'), ('ACC019', 'MIC'), ('ACC020', 'OTHER');

-- Add details for specific sub-tables where applicable
INSERT INTO flashlight (equipmentId, model, flashType) VALUES
('ACC001', 'V1', 'Round Head Speedlite'),
('ACC002', 'AD600', 'Studio Strobe');

INSERT INTO gimbal_stabilizer (equipmentId, model, gimbalType) VALUES
('ACC009', 'Mobile 7', 'Smartphone Gimbal'),
('ACC010', 'RS4', 'DSLR Gimbal');

INSERT INTO mic (equipmentId, model, micType) VALUES
('ACC015', 'Wireless GO', 'Wireless Clip-on'),
('ACC016', 'Wireless GO II', 'Wireless Dual'),
('ACC017', 'DJI Mic', 'Wireless Clip-on');