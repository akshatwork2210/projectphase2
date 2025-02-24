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
  `DesignID` varchar(50) NOT NULL,
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
INSERT INTO `inventory` VALUES ('D001',100,'Supplier A','helloooo',500),('D002',150,'Supplier B','helloooo',5000),('D003',200,'Supplier C','helloooo',700),('D004',50,'Supplier D','helloooo',5000),('D005',120,'Supplier E','helloooo',5000),('D006',300,'Supplier F','helloooo',5000),('D007',75,'Supplier G','helloooo',5000),('D008',180,'Supplier H','helloooo',5000),('D009',60,'Supplier I','helloooo',5000),('D010',250,'Supplier J','helloooo',5000),('hello',121,'okkkk','helloooo',5000),('mi remote',121,'okkkk','helloooo',5000),('thi',45,'ok','helloooo',5000);
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
  KEY `fk_design_id` (`design_id`),
  KEY `slip_type` (`slip_type`),
  CONSTRAINT `fk_design_id` FOREIGN KEY (`design_id`) REFERENCES `inventory` (`DesignID`),
  CONSTRAINT `order_slips_ibfk_1` FOREIGN KEY (`slip_type`) REFERENCES `ordertype` (`type_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips`
--

LOCK TABLES `order_slips` WRITE;
/*!40000 ALTER TABLE `order_slips` DISABLE KEYS */;
INSERT INTO `order_slips` VALUES ('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,1.00,'2025-02-22 07:58:20',1,'d','D002',1),('Kachhe Ka Baaki','Arjun Lal Ji R',700.00,'helloooo',5,2.00,'2025-02-22 07:58:20',2,'d','D003',1),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,3.00,'2025-02-22 07:58:20',3,'d','D004',1),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,1.00,'2025-02-22 07:58:24',1,'d','D002',2),('Kachhe Ka Baaki','Arjun Lal Ji R',700.00,'helloooo',5,2.00,'2025-02-22 07:58:24',2,'d','D003',2),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,3.00,'2025-02-22 07:58:24',3,'d','D004',2),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,1.00,'2025-02-22 07:58:25',1,'d','D002',3),('Kachhe Ka Baaki','Arjun Lal Ji R',700.00,'helloooo',5,2.00,'2025-02-22 07:58:25',2,'d','D003',3),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,3.00,'2025-02-22 07:58:25',3,'d','D004',3),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',5,1.00,'2025-02-22 07:59:03',1,'d','D002',4),('Kachhe Ka Baaki','Select Customer',700.00,'helloooo',5,2.00,'2025-02-22 07:59:03',2,'d','D003',4),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',5,3.00,'2025-02-22 07:59:03',3,'d','D004',4),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,1.00,'2025-02-22 07:59:41',1,'d','D002',5),('Kachhe Ka Baaki','Arjun Lal Ji R',700.00,'helloooo',5,2.00,'2025-02-22 07:59:41',2,'d','D003',5),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,3.00,'2025-02-22 07:59:41',3,'d','D004',5),('Kachhe Ka Baaki','Arjun Lal Ji R',500.00,'helloooo',2,0.25,'2025-02-22 08:06:10',1,'','D001',6),('Kachhe Ka Baaki','Arjun Lal Ji R',500.00,'helloooo',2,0.25,'2025-02-22 08:06:27',1,'c','D001',7),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',5,0.26,'2025-02-22 08:06:27',2,'c','D002',7),('Kachhe Ka Baaki','Arjun Lal Ji R',700.00,'helloooo',2,0.12,'2025-02-22 08:06:27',3,'c','D003',7),('Kachhe Ka Baaki','Arjun Lal Ji R',5000.00,'helloooo',1,0.00,'2025-02-22 08:06:27',4,'c','D004',7),('Kachhe Ka Baaki','Select Customer',500.00,'helloooo',1,1.00,'2025-02-22 08:09:40',1,'','D001',8),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',2,5.00,'2025-02-22 08:09:40',2,'','D002',8),('Kachhe Ka Baaki','Select Customer',700.00,'helloooo',3,8.00,'2025-02-22 08:09:40',3,'','D003',8),('Kachhe Ka Baaki','Select Customer',500.00,'helloooo',1,2.00,'2025-02-22 08:20:15',1,'','D001',9),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',2,1.00,'2025-02-22 08:20:15',2,'','D002',9),('Kachhe Ka Baaki','Select Customer',500.00,'helloooo',1,1.00,'2025-02-22 08:20:39',1,'','D001',10),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',2,2.00,'2025-02-22 08:20:39',2,'','D002',10),('Kachhe Ka Baaki','Select Customer',500.00,'helloooo',1,5.00,'2025-02-22 10:57:09',1,'','D001',11),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',2,6.00,'2025-02-22 10:57:09',2,'','D002',11),('Kachhe Ka Baaki','Select Customer',700.00,'helloooo',3,2.00,'2025-02-22 10:57:09',3,'','D003',11),('Kachhe Ka Baaki','Select Customer',5000.00,'helloooo',4,4.00,'2025-02-22 10:57:09',4,'','D005',11),('Kachhe Ka Baaki','HS Saab',500.00,'helloooo',5,0.30,'2025-02-24 06:43:05',1,'','D001',12),('Kachhe Ka Baaki','HS Saab',5000.00,'helloooo',6,0.40,'2025-02-24 06:43:05',2,'','D002',12),('Kachhe Ka Baaki','HS Saab',5000.00,'helloooo',2,0.50,'2025-02-24 06:43:05',3,'','D008',12);
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

-- Dump completed on 2025-02-24 20:19:53
