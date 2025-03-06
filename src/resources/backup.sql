-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: sample
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `billdetails`
--

DROP TABLE IF EXISTS `billdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billdetails` (
  `BillID` int NOT NULL,
  `SNo` int NOT NULL,
  `ItemName` varchar(100) NOT NULL,
  `DesignID` varchar(50) DEFAULT NULL,
  `OrderType` enum('Kachhe Me Jama','Kachhe Ka Baaki','Repairing') NOT NULL,
  `LabourCost` decimal(10,2) NOT NULL,
  `DullChillaiCost` decimal(10,2) DEFAULT NULL,
  `MeenaColorMeenaCost` decimal(10,2) DEFAULT NULL,
  `RhodiumCost` decimal(10,2) DEFAULT NULL,
  `NagSettingCost` decimal(10,2) DEFAULT NULL,
  `OtherBaseCosts` decimal(10,2) DEFAULT NULL,
  `OtherBaseCostNotes` text,
  `TotalBaseCosting` decimal(10,2) NOT NULL,
  `GoldRate` decimal(10,2) NOT NULL,
  `GoldPlatingWeight` decimal(10,2) NOT NULL,
  `TotalGoldCost` decimal(10,2) NOT NULL,
  `TotalFinalCost` decimal(10,2) NOT NULL,
  `OrderSlipNumber` int NOT NULL,
  KEY `BillID` (`BillID`),
  KEY `DesignID` (`DesignID`),
  CONSTRAINT `billdetails_ibfk_1` FOREIGN KEY (`BillID`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE,
  CONSTRAINT `billdetails_ibfk_2` FOREIGN KEY (`DesignID`) REFERENCES `inventory` (`DesignID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billdetails`
--

LOCK TABLES `billdetails` WRITE;
/*!40000 ALTER TABLE `billdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `billdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `BillID` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  PRIMARY KEY (`BillID`),
  KEY `fk_customer_id` (`customer_id`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (1,1),(2,2),(3,3),(4,4),(5,5);
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) NOT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `customer_name` (`customer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (5,'Arjun Lal Ji R'),(1,'HS Saab'),(3,'Rajesh ji AJR'),(2,'Sunil ji KN'),(4,'Vishal ji R');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `DesignID` varchar(20) NOT NULL,
  `TotalQuantity` int NOT NULL,
  `SupplierName` varchar(50) NOT NULL,
  `itemname` varchar(50) DEFAULT NULL,
  `price` int DEFAULT NULL,
  PRIMARY KEY (`DesignID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES ('D001',100,'Supplier A','helloooo',500),('D002',150,'Supplier B','helloooo',5000),('D003',200,'Supplier C','helloooo',700),('D004',50,'Supplier D','helloooo',5000),('D005',120,'Supplier E','helloooo',5000),('D006',300,'Supplier F','helloooo',5000),('D007',75,'Supplier G','helloooo',5000),('D008',180,'Supplier H','helloooo',5000),('D009',60,'Supplier I','helloooo',5000),('D010',250,'Supplier J','helloooo',5000),('D011',100,'Supplier K','Ring',1500),('D012',120,'Supplier L','Bangle',1800),('D013',90,'Supplier M','Anklet',1300),('D014',110,'Supplier N','Earring',1600),('D015',75,'Supplier O','Chain',5000),('D016',85,'Supplier P','Bracelet',2500),('D017',95,'Supplier Q','Necklace',4500),('D018',100,'Supplier R','Pendant',2000),('D019',80,'Supplier S','Earring',1000),('D020',60,'Supplier T','Ring',750),('D021',110,'Supplier U','Pendant',1200),('D022',130,'Supplier V','Ring',700),('D023',90,'Supplier W','Earring',600),('D024',100,'Supplier X','Bracelet',4500),('D025',140,'Supplier Y','Anklet',900),('D026',110,'Supplier Z','Earring',2500),('D027',105,'Supplier AA','Bangle',1000),('D028',115,'Supplier BB','Ring',700),('D029',120,'Supplier CC','Pendant',500),('D030',125,'Supplier DD','Bracelet',600),('D031',50,'Supplier K','Necklace',3200),('D032',75,'Supplier L','Bracelet',2700),('D033',120,'Supplier M','Ring',1500),('D034',90,'Supplier N','Pendant',2200),('D035',60,'Supplier O','Earring',1800),('D036',40,'Supplier P','Ring',600),('D037',85,'Supplier Q','Bangle',1500),('D038',70,'Supplier R','Bracelet',1000),('D039',55,'Supplier S','Earring',700),('D040',65,'Supplier T','Anklet',800),('D041',45,'Supplier U','Necklace',4500),('D042',95,'Supplier V','Bracelet',3000),('D043',110,'Supplier W','Ring',2000),('D044',80,'Supplier X','Earring',1200),('D045',70,'Supplier Y','Pendant',1500),('D046',55,'Supplier Z','Chain',3200),('D047',60,'Supplier A1','Bracelet',2800),('D048',90,'Supplier B1','Ring',2200),('D049',75,'Supplier C1','Earring',2500),('D050',65,'Supplier D1','Pendant',2000),('D051',50,'Supplier E1','Ring',800),('D052',85,'Supplier F1','Bangle',1500),('D053',70,'Supplier G1','Bracelet',1000),('D054',55,'Supplier H1','Earring',700),('D055',65,'Supplier I1','Anklet',900),('hello',121,'okkkk','helloooo',5000),('mi remote',121,'okkkk','helloooo',5000),('RP121MO',50,'rani patta har set',NULL,NULL),('RPMO122',65,'rani patta har set',NULL,NULL),('thi',45,'ok','helloooo',5000);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_slips`
--

DROP TABLE IF EXISTS `order_slips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_slips` (
  `slip_type` varchar(50) DEFAULT NULL,
  `customer_name` varchar(255) NOT NULL,
  `raw_material_price` decimal(10,2) DEFAULT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `plating_grams` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `item_number` int NOT NULL,
  `other_details` varchar(50) DEFAULT NULL,
  `design_id` varchar(20) NOT NULL,
  `slip_id` int DEFAULT NULL,
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`item_id`),
  KEY `fk_design_id` (`design_id`),
  KEY `idx_slip_id` (`slip_id`),
  KEY `idx_customer_name` (`customer_name`),
  KEY `idx_slip_type` (`slip_type`),
  CONSTRAINT `fk_design_id` FOREIGN KEY (`design_id`) REFERENCES `inventory` (`DesignID`),
  CONSTRAINT `order_slips_ibfk_1` FOREIGN KEY (`slip_type`) REFERENCES `ordertype` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips`
--

LOCK TABLES `order_slips` WRITE;
/*!40000 ALTER TABLE `order_slips` DISABLE KEYS */;
INSERT INTO `order_slips` VALUES ('Kachhe Me Jama','HS Saab',1350.00,'Ring',6,2.50,'2025-03-02 16:12:11',151,'Exclusive Design','D012',11,1),('Kachhe Me Jama','HS Saab',1150.00,'Bangle',7,2.20,'2025-03-02 16:12:11',152,'Royal Finish','D024',11,2),('Kachhe Me Jama','HS Saab',950.00,'Bracelet',5,1.90,'2025-03-02 16:12:11',153,'Handcrafted','D031',11,3),('Kachhe Me Jama','HS Saab',1250.00,'Necklace',8,3.00,'2025-03-02 16:12:11',154,'Gold Layered','D045',11,4),('Kachhe Me Jama','HS Saab',900.00,'Pendant',5,2.30,'2025-03-02 16:12:11',155,'Silver Touch','D003',11,5),('Repairing','Rajesh ji AJR',1050.00,'Earring',6,1.80,'2025-03-02 16:12:11',156,'Diamond Studded','D019',12,6),('Repairing','Rajesh ji AJR',1400.00,'Ring',8,2.10,'2025-03-02 16:12:11',157,'Matte Finish','D028',12,7),('Repairing','Rajesh ji AJR',650.00,'Bracelet',5,2.00,'2025-03-02 16:12:11',158,'Rose Gold','D038',12,8),('Repairing','Rajesh ji AJR',1100.00,'Bangle',7,2.50,'2025-03-02 16:12:11',159,'Antique Polish','D052',12,9),('Repairing','Rajesh ji AJR',800.00,'Anklet',5,1.70,'2025-03-02 16:12:11',160,'Silver Coated','D055',12,10),('Kachhe Ka Baaki','Sunil ji KN',1300.00,'Necklace',7,3.20,'2025-03-02 16:12:11',161,'Royal Design','D041',13,11),('Kachhe Ka Baaki','Sunil ji KN',950.00,'Bracelet',5,2.10,'2025-03-02 16:12:11',162,'Classic Look','D030',13,12),('Kachhe Ka Baaki','Sunil ji KN',900.00,'Pendant',6,2.40,'2025-03-02 16:12:11',163,'Engraved','D050',13,13),('Kachhe Ka Baaki','Sunil ji KN',700.00,'Ring',5,1.50,'2025-03-02 16:12:11',164,'Simple Design','D022',13,14),('Kachhe Ka Baaki','Sunil ji KN',750.00,'Earring',6,2.00,'2025-03-02 16:12:11',165,'Pearl Embedded','D026',13,15),('Kachhe Me Jama','Vishal ji R',1550.00,'Bangle',8,2.50,'2025-03-02 16:12:11',166,'Handcrafted','D037',14,16),('Kachhe Me Jama','Vishal ji R',1350.00,'Necklace',6,3.40,'2025-03-02 16:12:11',167,'Grand Design','D017',14,17),('Kachhe Me Jama','Vishal ji R',1100.00,'Bracelet',7,2.00,'2025-03-02 16:12:11',168,'Gold Polish','D053',14,18),('Kachhe Me Jama','Vishal ji R',800.00,'Ring',5,1.80,'2025-03-02 16:12:11',169,'Custom Cut','D033',14,19),('Kachhe Me Jama','Vishal ji R',950.00,'Pendant',6,2.30,'2025-03-02 16:12:11',170,'Diamond Engraved','D029',14,20),('Repairing','Arjun Lal Ji R',850.00,'Earring',5,1.90,'2025-03-02 16:12:11',171,'Pearl Drop','D039',15,21),('Repairing','Arjun Lal Ji R',1000.00,'Ring',6,2.00,'2025-03-02 16:12:11',172,'Sleek Design','D016',15,22),('Repairing','Arjun Lal Ji R',750.00,'Bracelet',7,2.20,'2025-03-02 16:12:11',173,'Minimalist Look','D013',15,23),('Repairing','Arjun Lal Ji R',1150.00,'Bangle',5,2.40,'2025-03-02 16:12:11',174,'Handmade','D027',15,24),('Repairing','Arjun Lal Ji R',900.00,'Anklet',6,1.60,'2025-03-02 16:12:11',175,'Silver Polish','D040',15,25),('Kachhe Me Jama','HS Saab',1400.00,'Ring',6,2.50,'2025-03-02 16:12:11',176,'Exclusive Design','D011',16,26),('Kachhe Me Jama','HS Saab',1200.00,'Bangle',7,2.20,'2025-03-02 16:12:11',177,'Royal Finish','D025',16,27),('Kachhe Me Jama','HS Saab',1000.00,'Bracelet',5,1.90,'2025-03-02 16:12:11',178,'Handcrafted','D035',16,28),('Kachhe Me Jama','HS Saab',1300.00,'Necklace',8,3.00,'2025-03-02 16:12:11',179,'Gold Layered','D042',16,29),('Kachhe Me Jama','HS Saab',950.00,'Pendant',5,2.30,'2025-03-02 16:12:11',180,'Silver Touch','D048',16,30),('Repairing','Rajesh ji AJR',1100.00,'Earring',6,1.80,'2025-03-02 16:12:11',181,'Diamond Studded','D018',17,31),('Repairing','Rajesh ji AJR',1450.00,'Ring',8,2.10,'2025-03-02 16:12:11',182,'Matte Finish','D020',17,32),('Repairing','Rajesh ji AJR',700.00,'Bracelet',5,2.00,'2025-03-02 16:12:11',183,'Rose Gold','D021',17,33),('Repairing','Rajesh ji AJR',1150.00,'Bangle',7,2.50,'2025-03-02 16:12:11',184,'Antique Polish','D014',17,34),('Repairing','Rajesh ji AJR',850.00,'Anklet',5,1.70,'2025-03-02 16:12:11',185,'Silver Coated','D023',17,35),('Kachhe Me Jama','HS Saab',1250.00,'Ring',6,2.40,'2025-03-02 16:12:59',101,'Exclusive Design','D033',1,36),('Kachhe Me Jama','HS Saab',1100.00,'Bangle',5,2.00,'2025-03-02 16:12:59',102,'Traditional Pattern','D012',1,37),('Kachhe Me Jama','HS Saab',950.00,'Necklace',7,3.20,'2025-03-02 16:12:59',103,'Gold Polish','D041',1,38),('Kachhe Me Jama','HS Saab',1350.00,'Bracelet',6,2.50,'2025-03-02 16:12:59',104,'Handcrafted','D037',1,39),('Kachhe Me Jama','HS Saab',800.00,'Pendant',5,2.10,'2025-03-02 16:12:59',105,'Silver Engraved','D045',1,40),('Repairing','Rajesh ji AJR',1400.00,'Earring',6,1.80,'2025-03-02 16:12:59',106,'Pearl Embedded','D026',2,41),('Repairing','Rajesh ji AJR',1300.00,'Ring',7,2.10,'2025-03-02 16:12:59',107,'Matte Finish','D028',2,42),('Repairing','Rajesh ji AJR',900.00,'Bracelet',5,1.90,'2025-03-02 16:12:59',108,'Rose Gold Polish','D038',2,43),('Repairing','Rajesh ji AJR',1150.00,'Bangle',6,2.30,'2025-03-02 16:12:59',109,'Antique Style','D052',2,44),('Repairing','Rajesh ji AJR',1000.00,'Anklet',5,1.70,'2025-03-02 16:12:59',110,'Silver Plated','D055',2,45),('Repairing','Arjun Lal Ji R',850.00,'Earring',5,1.90,'2025-03-02 16:12:59',121,'Pearl Drop','D039',5,46),('Repairing','Arjun Lal Ji R',1000.00,'Ring',6,2.20,'2025-03-02 16:12:59',122,'Elegant Cut','D016',5,47),('Repairing','Arjun Lal Ji R',750.00,'Bracelet',7,2.00,'2025-03-02 16:12:59',123,'Classic Style','D013',5,48),('Repairing','Arjun Lal Ji R',1150.00,'Bangle',5,2.30,'2025-03-02 16:12:59',124,'Handmade','D027',5,49),('Repairing','Arjun Lal Ji R',900.00,'Anklet',6,1.60,'2025-03-02 16:12:59',125,'Silver Coated','D040',5,50);
/*!40000 ALTER TABLE `order_slips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordertype`
--

DROP TABLE IF EXISTS `ordertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordertype` (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordertype`
--

LOCK TABLES `ordertype` WRITE;
/*!40000 ALTER TABLE `ordertype` DISABLE KEYS */;
INSERT INTO `ordertype` VALUES (2,'Kachhe Ka Baaki'),(1,'Kachhe Me Jama'),(3,'Repairing');
/*!40000 ALTER TABLE `ordertype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otherdetail`
--

DROP TABLE IF EXISTS `otherdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `otherdetail` (
  `id` int NOT NULL,
  `dnumber` int NOT NULL,
  `details` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otherdetail`
--

LOCK TABLES `otherdetail` WRITE;
/*!40000 ALTER TABLE `otherdetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `otherdetail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-06 20:38:23
