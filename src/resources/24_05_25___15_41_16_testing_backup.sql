-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: testing
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
) ENGINE=InnoDB AUTO_INCREMENT=11887 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billdetails`
--

LOCK TABLES `billdetails` WRITE;
/*!40000 ALTER TABLE `billdetails` DISABLE KEYS */;
INSERT INTO `billdetails` VALUES (2683,1,'2LINE MS LADI','2LMS4B','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-23920.00,NULL,11881,230.00,-104),(2683,2,'3LINE MS LADI','3LMS4B','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-9100.00,NULL,11882,260.00,-35),(2683,3,'KANCHAIN','KANCHAIN','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-7280.00,NULL,11883,35.00,-208),(2686,1,'BADE PANDAL','BP','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-7650.00,NULL,11884,85.00,-90),(2686,2,'MIDIUM PANDAL','MIDP','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-12375.00,NULL,11885,75.00,-165),(2686,3,'GHANTI GHUNGRU HAR','MINHGGS','purchase',0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,0.00,0.00,0.00,-6900.00,NULL,11886,230.00,-30);
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
  CONSTRAINT `fk_bill` FOREIGN KEY (`customer_name`) REFERENCES `customers` (`customer_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2691 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (2683,'2025-05-18 00:00:00','Basheer Bhai'),(2686,'2025-05-18 00:00:00','MOHSEEN BHAI');
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
  `balance` decimal(15,2) DEFAULT NULL,
  `openingaccount` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `customer_name` (`customer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (37,'Basheer Bhai',-40300.00,0.00),(38,'MOHSEEN BHAI',-26925.00,0.00),(39,'PAPPU JI KTN',0.00,0.00),(40,'GOLDEN PLAZA',0.00,0.00),(41,'OPG',0.00,0.00),(42,'TRIPATHI JI',0.00,0.00),(43,'RAJESH JI AJR',0.00,0.00),(44,'SANVI JI RAHUL',0.00,0.00),(45,'SHREE NITESH JI PRATAPGAD',0.00,0.00),(46,'PURUSHOTTUM JI',0.00,0.00),(47,'GJN SA',0.00,0.00),(48,'SHREE ARJUN LAL JI',0.00,0.00);
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
  `OPENINGSTOCK` mediumtext,
  PRIMARY KEY (`DesignID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES ('2LMS4B',104,'2LINE MS LADI',230,NULL),('3LMS4B',33,'3LINE MS LADI',260,NULL),('BP',90,'BADE PANDAL',85,NULL),('KANCHAIN',208,'KANCHAIN',35,NULL),('MIDP',139,'MIDIUM PANDAL',75,NULL),('MINHGGS',30,'GHANTI GHUNGRU HAR',230,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips`
--

LOCK TABLES `order_slips` WRITE;
/*!40000 ALTER TABLE `order_slips` DISABLE KEYS */;
INSERT INTO `order_slips` VALUES ('Kachhe Ka Baaki','TRIPATHI JI',185.00,'AD DOKIYA NAG',4,0.20,'2025-05-20 15:15:24','',NULL,54,448,1,0),('Kachhe Ka Baaki','TRIPATHI JI',60.00,'3 LINE BOX CHAIN',1,0.30,'2025-05-20 15:15:24','',NULL,54,449,2,0),('Kachhe Ka Baaki','TRIPATHI JI',0.00,'AD TOPS JOD',5,0.05,'2025-05-20 15:15:24','ROD LAGANA',NULL,54,450,3,0),('Kachhe Ka Baaki','TRIPATHI JI',320.00,'HR CHUDI SET',2,0.60,'2025-05-20 15:15:24','CHOUDI CHUDI',NULL,54,451,4,0),('Kachhe Ka Baaki','GOLDEN PLAZA',90.00,'MIDIUM PANDAL',20,0.00,'2025-05-20 15:16:17','','MIDP',55,452,1,0),('Kachhe Ka Baaki','GOLDEN PLAZA',0.00,'RANI PATTA HAR SET',1,0.00,'2025-05-20 15:17:01','',NULL,56,453,1,0),('Kachhe Ka Baaki','OPG',0.00,'HATHFUL JODI',2,0.00,'2025-05-20 15:17:38','',NULL,57,454,1,0),('Kachhe Ka Baaki','RAJESH JI AJR',90.00,'MIDIUM PANDAL',6,0.00,'2025-05-20 15:19:02','','MIDP',58,455,1,0),('Kachhe Ka Baaki','TRIPATHI JI',0.00,'PLASTER PATLI SET',1,1.00,'2025-05-20 15:20:32','',NULL,59,456,1,0),('Kachhe Ka Baaki','TRIPATHI JI',250.00,'RAJWADI RING',476,0.00,'2025-05-20 15:21:08','',NULL,60,457,1,0),('Kachhe Ka Baaki','SHREE NITESH JI PRATAPGAD',260.00,'3LINE MS LADI',2,0.20,'2025-05-20 15:24:02','','3LMS4B',61,458,1,0),('Kachhe Ka Baaki','SHREE NITESH JI PRATAPGAD',80.00,'TIKA NAG',1,0.07,'2025-05-20 15:24:02','',NULL,61,459,2,0),('Kachhe Ka Baaki','SHREE NITESH JI PRATAPGAD',0.00,'TOPDI JODI',7,0.05,'2025-05-20 15:24:02','MEENA 7',NULL,61,460,3,0),('Kachhe Ka Baaki','GJN SA',200.00,'DOKIYA NAG',1,0.40,'2025-05-20 15:25:35','',NULL,62,461,1,0),('Kachhe Ka Baaki','SHREE ARJUN LAL JI',500.00,'MINI RANI HAR NAG',1,0.60,'2025-05-20 15:27:04','URGENT',NULL,63,462,1,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips_main`
--

LOCK TABLES `order_slips_main` WRITE;
/*!40000 ALTER TABLE `order_slips_main` DISABLE KEYS */;
INSERT INTO `order_slips_main` VALUES (54,'Kachhe Ka Baaki','2025-05-20 15:15:24'),(55,'Kachhe Ka Baaki','2025-05-20 15:16:17'),(56,'Kachhe Ka Baaki','2025-05-20 15:17:01'),(57,'Kachhe Ka Baaki','2025-05-20 15:17:38'),(58,'Kachhe Ka Baaki','2025-05-20 15:19:02'),(59,'Kachhe Ka Baaki','2025-05-20 15:20:32'),(60,'Kachhe Ka Baaki','2025-05-20 15:21:08'),(61,'Kachhe Ka Baaki','2025-05-20 15:24:02'),(62,'Kachhe Ka Baaki','2025-05-20 15:25:35'),(63,'Kachhe Ka Baaki','2025-05-20 15:27:04');
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
) ENGINE=InnoDB AUTO_INCREMENT=1196 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
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

-- Dump completed on 2025-05-24 15:41:16
