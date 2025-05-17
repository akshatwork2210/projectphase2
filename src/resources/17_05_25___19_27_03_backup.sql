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
  `ordertype` varchar(100) DEFAULT NULL,
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
  `OrderSlipNumber` bigint DEFAULT NULL,
  `itemid_bill` int NOT NULL AUTO_INCREMENT,
  `RawCost` decimal(10,2) NOT NULL DEFAULT '0.00',
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`itemid_bill`),
  KEY `DesignID` (`DesignID`),
  KEY `fk_orderslip` (`OrderSlipNumber`),
  KEY `fk_billdetails_bill` (`BillID`),
  CONSTRAINT `billdetails_ibfk_1` FOREIGN KEY (`BillID`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_billdetails_bill` FOREIGN KEY (`BillID`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=718 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billdetails`
--

LOCK TABLES `billdetails` WRITE;
/*!40000 ALTER TABLE `billdetails` DISABLE KEYS */;
INSERT INTO `billdetails` VALUES (253,1,'1','NoID','Kachhe Ka Baaki',65.00,65.00,65.00,65.00,65.00,65.00,'',22750.00,8550.00,2.00,17100.00,39850.00,0,704,65.00,50),(253,2,'2','NoID','Kachhe Ka Baaki',65.00,65.00,65.00,65.00,6.00,55.00,'',25090.00,8550.00,5.00,42750.00,67840.00,0,705,65.00,65),(253,3,'3','NoID','Kachhe Ka Baaki',54.00,0.00,0.00,0.00,0.00,0.00,'',270.00,8550.00,6.00,51300.00,51570.00,0,706,0.00,5),(253,4,'adf','NoID','Kachhe Ka Baaki',12.00,0.00,0.00,0.00,0.00,0.00,'',60.00,8550.00,2.00,17100.00,17160.00,0,707,0.00,5),(253,5,'adsf','NoID','Kachhe Ka Baaki',65.00,0.00,0.00,0.00,0.00,0.00,'',325.00,8550.00,4.00,34200.00,34525.00,0,708,0.00,5),(253,6,'ade','NoID','Kachhe Ka Baaki',87.00,0.00,0.00,0.00,0.00,0.00,'',435.00,8550.00,3.00,25650.00,26085.00,0,709,0.00,5),(253,7,'sdf','NoID','Kachhe Ka Baaki',32.00,0.00,0.00,0.00,0.00,0.00,'',160.00,8550.00,5.00,42750.00,42910.00,0,710,0.00,5),(256,1,'sf','NoID','Kachhe Ka Baaki',45.00,45.00,65.00,254.00,74.00,0.00,'',2640.00,8550.00,5.00,42750.00,45390.00,0,711,45.00,5),(256,2,'adsf','NoID','Kachhe Ka Baaki',65.00,65.00,456.00,65.00,85.00,0.00,'',3124.00,8550.00,2.00,17100.00,20224.00,0,712,45.00,4),(256,3,'asdf','NoID','Kachhe Ka Baaki',45.00,45.00,65.00,45.00,65.00,0.00,'',930.00,8550.00,3.00,25650.00,26580.00,0,713,45.00,3),(256,4,'adf','NoID','Kachhe Ka Baaki',65.00,65.00,45.00,65.00,45.00,0.00,'',660.00,8550.00,5.00,42750.00,43410.00,0,714,45.00,2),(259,1,'Midium Pandal','NoID','Kachhe Ka Baaki',45.00,65.00,32.00,850.00,0.00,0.00,'',5285.00,9850.00,5.00,49250.00,54535.00,0,715,65.00,5),(259,2,'Moti Chain','NoID','Kachhe Ka Baaki',65.00,45.00,45.00,0.00,0.00,0.00,'',2090.00,9850.00,5.00,49250.00,51340.00,0,716,54.00,10),(259,3,'mangalsutra','NoID','Kachhe Ka Baaki',12.00,65.00,12.00,0.00,0.00,0.00,'',2680.00,9850.00,5.00,49250.00,51930.00,0,717,45.00,20);
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
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `customer_name` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`BillID`),
  KEY `fk_bill` (`customer_name`),
  CONSTRAINT `fk_bill` FOREIGN KEY (`customer_name`) REFERENCES `customers` (`customer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (253,'2025-03-31 13:00:00','Golden Plaza'),(256,'2025-04-15 13:00:00','Golden Plaza'),(259,'2025-05-17 18:56:22','Kothari ji kota');
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
  `balance` double DEFAULT NULL,
  `openingaccount` double DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `customer_name` (`customer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (18,'Mohseen bhai',0,-126000),(19,'Prabir Bhukta',0,598630),(24,'Mohseen hai',0,36000),(25,'Kothari ji kota',157805,51790),(26,'Golden Plaza',299544,138525),(27,'????? ?? kn',0,65000),(28,'SUNIL JI KN',0,65000),(29,'Triptahi ji ',0,65000),(30,'Sachin Soni',0,65000);
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
INSERT INTO `inventory` VALUES ('D001',570,'mangal sutra',120),('D002',1945,'rings',35),('D003',150,'rani har',650),('D005',50,'Fancy Kandora chain',15);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item_bill_link`
--

DROP TABLE IF EXISTS `order_item_bill_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item_bill_link` (
  `link_id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `bill_id` int NOT NULL,
  `quantity` int NOT NULL,
  `amount_covered` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`link_id`),
  KEY `order_item_bill_link_ibfk_1` (`item_id`),
  KEY `order_item_bill_link_ibfk_2` (`bill_id`),
  CONSTRAINT `order_item_bill_link_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `order_slips` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_item_bill_link_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item_bill_link`
--

LOCK TABLES `order_item_bill_link` WRITE;
/*!40000 ALTER TABLE `order_item_bill_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_item_bill_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_slips`
--

DROP TABLE IF EXISTS `order_slips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_slips` (
  `slip_type` varchar(50) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `raw_material_price` decimal(10,2) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `plating_grams` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `other_details` varchar(50) DEFAULT NULL,
  `design_id` varchar(20) DEFAULT NULL,
  `slip_id` int DEFAULT NULL,
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `sno` int DEFAULT NULL,
  `billed_quantity` int DEFAULT '0',
  PRIMARY KEY (`item_id`),
  KEY `idx_slip_id` (`slip_id`),
  KEY `idx_customer_name` (`customer_name`),
  KEY `idx_slip_type` (`slip_type`),
  KEY `fk_design_id` (`design_id`),
  CONSTRAINT `customer_fk` FOREIGN KEY (`customer_name`) REFERENCES `customers` (`customer_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_design_id` FOREIGN KEY (`design_id`) REFERENCES `inventory` (`DesignID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_slip_id` FOREIGN KEY (`slip_id`) REFERENCES `order_slips_main` (`slip_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_slips_ibfk_1` FOREIGN KEY (`slip_type`) REFERENCES `ordertype` (`type_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips`
--

LOCK TABLES `order_slips` WRITE;
/*!40000 ALTER TABLE `order_slips` DISABLE KEYS */;
INSERT INTO `order_slips` VALUES ('Kachhe Me Jama','Kothari ji kota',0.00,'MOTI CHAIN',8,0.10,'2025-05-07 14:17:34','',NULL,47,428,1,0),('Kachhe Me Jama','Kothari ji kota',0.00,'MIX HOLO CHAIN',12,0.20,'2025-05-07 14:17:34','',NULL,47,429,2,0),('Kachhe Me Jama','Kothari ji kota',0.00,'BRACELET NAG',3,0.20,'2025-05-07 14:17:34','',NULL,47,430,3,0),('Kachhe Me Jama','Kothari ji kota',0.00,'BOX CHAIN NAG',4,0.20,'2025-05-07 14:17:34','',NULL,47,431,4,0),('Kachhe Me Jama','Kothari ji kota',0.00,'RASSI CHAIN NAG',4,0.00,'2025-05-07 14:17:34','',NULL,47,432,5,0),('Kachhe Me Jama','Kothari ji kota',0.00,'VERTICAL DAANE MALA',5,0.20,'2025-05-07 14:17:34','',NULL,47,433,6,0),('Kachhe Me Jama','Golden Plaza',0.00,'HOLO CHAIN NAG',1,0.25,'2025-05-07 14:24:18','',NULL,49,434,1,0),('Kachhe Ka Baaki','SUNIL JI KN',0.00,'MINI HAR SET',3,0.00,'2025-05-07 14:32:15','',NULL,50,435,1,1),('Kachhe Ka Baaki','SUNIL JI KN',0.00,'SINGLE STEP JHUMKI JOD',2,0.00,'2025-05-07 14:32:15','',NULL,50,436,2,2),('Kachhe Ka Baaki','SUNIL JI KN',0.00,'LATKAN JOD',3,0.00,'2025-05-07 14:32:15','',NULL,50,437,3,3),('Kachhe Ka Baaki','Golden Plaza',0.00,'HEAVY HOLO CHAIN',6,0.40,'2025-05-07 15:17:50','',NULL,51,438,1,0),('Kachhe Ka Baaki','Golden Plaza',0.00,'NICE HOLO CHAIN',4,0.25,'2025-05-07 15:17:50','M/CM/AD NAHI LAGANA',NULL,51,439,2,0),('Kachhe Ka Baaki','Golden Plaza',0.00,'CHOURAS PANDAL',2,0.25,'2025-05-07 15:17:50','',NULL,51,440,3,0),('Kachhe Ka Baaki','Golden Plaza',0.00,'MIX DOKIYA NAG',5,0.15,'2025-05-07 15:17:50','',NULL,51,441,4,0),('Kachhe Ka Baaki','Golden Plaza',0.00,'GENTS BRACELET',1,0.25,'2025-05-07 15:17:50','',NULL,51,442,5,0),('Kachhe Me Jama','Triptahi ji ',0.00,'VERTICAL DAANE MALA BADE',1,0.30,'2025-05-07 15:23:28','30\" KARNA HAI',NULL,52,443,1,0),('Kachhe Me Jama','Triptahi ji ',0.00,'VERTICAL DAANE MALA',1,0.20,'2025-05-07 15:23:28','COPPER NAHI HOGA',NULL,52,444,2,0),('Kachhe Me Jama','Sachin Soni',0.00,'THOS BAALI NAG',14,0.00,'2025-05-07 15:25:30','',NULL,53,445,1,0),('Kachhe Me Jama','Sachin Soni',0.00,'3 LINE KANTHA NAG',1,0.00,'2025-05-07 15:25:30','',NULL,53,446,2,0),('Kachhe Me Jama','Sachin Soni',0.00,'5 LINE KANTHA NAG',1,0.00,'2025-05-07 15:25:30','',NULL,53,447,3,0);
/*!40000 ALTER TABLE `order_slips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_slips_main`
--

DROP TABLE IF EXISTS `order_slips_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_slips_main` (
  `slip_id` int NOT NULL AUTO_INCREMENT,
  `slip_type` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`slip_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips_main`
--

LOCK TABLES `order_slips_main` WRITE;
/*!40000 ALTER TABLE `order_slips_main` DISABLE KEYS */;
INSERT INTO `order_slips_main` VALUES (45,'Kachhe Me Jama','2025-05-07 14:16:40'),(46,'Kachhe Me Jama','2025-05-07 14:16:44'),(47,'Kachhe Me Jama','2025-05-07 14:17:34'),(48,'Kachhe Me Jama','2025-05-07 14:22:56'),(49,'Kachhe Me Jama','2025-05-07 14:24:18'),(50,'Kachhe Ka Baaki','2025-05-07 14:32:15'),(51,'Kachhe Ka Baaki','2025-05-07 15:17:50'),(52,'Kachhe Me Jama','2025-05-07 15:23:28'),(53,'Kachhe Me Jama','2025-05-07 15:25:30');
/*!40000 ALTER TABLE `order_slips_main` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordertype`
--

LOCK TABLES `ordertype` WRITE;
/*!40000 ALTER TABLE `ordertype` DISABLE KEYS */;
INSERT INTO `ordertype` VALUES (2,'Kachhe Ka Baaki'),(1,'Kachhe Me Jama'),(4,'purchase'),(3,'Repairing');
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

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `billid` int DEFAULT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `fk_customer_name_transaction` (`customer_name`),
  KEY `transactions_ibfk_1` (`billid`),
  CONSTRAINT `fk_customer_name_transaction` FOREIGN KEY (`customer_name`) REFERENCES `customers` (`customer_name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`billid`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (90,256,29000,'2025-05-17','Golden Plaza',''),(91,256,29000,'2025-05-17','Golden Plaza',''),(92,256,29000,'2025-05-17','Golden Plaza',''),(93,256,29000,'2025-05-17','Golden Plaza','');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-17 19:27:04
