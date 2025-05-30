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
  KEY `bills_customer_fk` (`customer_id`),
  CONSTRAINT `bills_customer_fk` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (6,NULL),(7,NULL),(8,NULL),(9,NULL),(10,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (6,'Amit Sharma'),(14,'Arjun Nair'),(13,'Kavita Das'),(11,'Meera Iyer'),(9,'Neha Joshi'),(7,'Priya Verma'),(8,'Rohan Gupta'),(15,'Sneha Kapoor'),(10,'Suresh Patel'),(12,'Vikram Singh');
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
INSERT INTO `inventory` VALUES ('D001',411,'Supplier8','Chain',1395),('D002',427,'Supplier4','Anklet',1379),('D003',202,'Supplier2','Anklet',1641),('D004',65,'Supplier1','Bracelet',829),('D005',470,'Supplier5','Bangles',1378),('D006',449,'Supplier5','Necklace',964),('D007',232,'Supplier10','Bracelet',957),('D008',916,'Supplier6','Ring',1989),('D009',896,'Supplier10','Nose Ring',2095),('D010',706,'Supplier8','Bangles',968),('D011',529,'Supplier6','Ring',2022),('D012',100,'Supplier7','Bracelet',696),('D013',282,'Supplier7','Earrings',694),('D014',746,'Supplier1','Necklace',1365),('D015',259,'Supplier6','Chain',1779),('D016',193,'Supplier6','Bracelet',1322),('D017',892,'Supplier9','Chain',1097),('D018',567,'Supplier6','Nose Ring',1026),('D019',863,'Supplier2','Earrings',880),('D020',373,'Supplier9','Necklace',544),('D021',284,'Supplier9','Pendant',1046),('D022',127,'Supplier3','Necklace',598),('D023',178,'Supplier3','Bangles',2263),('D024',723,'Supplier6','Chain',1284),('D025',897,'Supplier10','Bracelet',766),('D026',485,'Supplier6','Pendant',572),('D027',938,'Supplier3','Anklet',1867),('D028',404,'Supplier6','Earrings',1635),('D029',66,'Supplier1','Earrings',1392),('D030',672,'Supplier7','Necklace',550),('D031',60,'Supplier7','Earrings',1800),('D032',696,'Supplier2','Bangles',525),('D033',452,'Supplier8','Pendant',1941),('D034',243,'Supplier6','Necklace',735),('D035',576,'Supplier2','Nose Ring',743),('D036',400,'Supplier2','Chain',1034),('D037',521,'Supplier4','Earrings',2058),('D038',228,'Supplier4','Nose Ring',1891),('D039',201,'Supplier5','Pendant',1563),('D040',468,'Supplier4','Necklace',555),('D041',59,'Supplier7','Bracelet',1467),('D042',963,'Supplier1','Earrings',1840),('D043',705,'Supplier2','Pendant',1755),('D044',762,'Supplier6','Pendant',618),('D045',132,'Supplier10','Anklet',1778),('D046',172,'Supplier5','Bangles',1205),('D047',72,'Supplier8','Bangles',984),('D048',562,'Supplier7','Bangles',1588),('D049',76,'Supplier3','Nose Ring',665),('D050',75,'Supplier6','Chain',1320),('D051',989,'Supplier5','Ring',708),('D052',771,'Supplier2','Pendant',1552),('D053',265,'Supplier3','Pendant',792),('D054',450,'Supplier4','Pendant',1814),('D055',116,'Supplier1','Necklace',1640),('D056',817,'Supplier1','Bangles',1251),('D057',283,'Supplier8','Bracelet',2128),('D058',793,'Supplier2','Anklet',2126),('D059',163,'Supplier9','Nose Ring',537),('D060',787,'Supplier6','Bracelet',1911),('D061',247,'Supplier7','Anklet',1506),('D062',553,'Supplier9','Pendant',1318),('D063',745,'Supplier2','Anklet',1985),('D064',883,'Supplier9','Pendant',1654),('D065',467,'Supplier2','Anklet',304),('D066',541,'Supplier6','Earrings',2199),('D067',751,'Supplier9','Nose Ring',1163),('D068',225,'Supplier7','Pendant',494),('D069',709,'Supplier2','Chain',1178),('D070',828,'Supplier8','Earrings',1779),('D071',488,'Supplier1','Ring',325),('D072',904,'Supplier5','Pendant',1383),('D073',964,'Supplier2','Ring',1623),('D074',216,'Supplier9','Nose Ring',2241),('D075',111,'Supplier5','Chain',612),('D076',183,'Supplier3','Bangles',336),('D077',868,'Supplier3','Pendant',1362),('D078',707,'Supplier9','Bracelet',1506),('D079',335,'Supplier7','Pendant',1843),('D080',187,'Supplier5','Pendant',1910),('D081',247,'Supplier7','Anklet',1541),('D082',626,'Supplier2','Ring',1554),('D083',102,'Supplier4','Chain',1980),('D084',797,'Supplier5','Bangles',837),('D085',278,'Supplier4','Bracelet',520),('D086',784,'Supplier6','Earrings',501),('D087',512,'Supplier2','Necklace',1453),('D088',336,'Supplier8','Nose Ring',1433),('D089',900,'Supplier8','Necklace',1483),('D090',440,'Supplier3','Necklace',2228),('D091',373,'Supplier9','Ring',1712),('D092',476,'Supplier2','Bracelet',2286),('D093',189,'Supplier8','Bracelet',1097),('D094',997,'Supplier8','Nose Ring',1010),('D095',951,'Supplier7','Anklet',1415),('D096',282,'Supplier6','Ring',1197),('D097',223,'Supplier6','Bracelet',1820),('D098',926,'Supplier4','Chain',1004),('D099',204,'Supplier8','Bracelet',705),('D100',172,'Supplier1','Chain',2140),('D101',218,'Supplier2','Ring',606),('D102',471,'Supplier8','Anklet',378),('D103',817,'Supplier10','Necklace',371),('D104',724,'Supplier5','Ring',2292),('D105',804,'Supplier10','Anklet',1687),('D106',890,'Supplier4','Ring',625),('D107',723,'Supplier1','Necklace',1403),('D108',364,'Supplier10','Nose Ring',2267),('D109',936,'Supplier8','Bangles',1577),('D110',934,'Supplier8','Nose Ring',823),('D111',643,'Supplier4','Chain',363),('D112',748,'Supplier6','Bangles',1653),('D113',372,'Supplier7','Bracelet',1341),('D114',711,'Supplier10','Anklet',1826),('D115',339,'Supplier3','Bracelet',1455),('D116',164,'Supplier9','Nose Ring',920),('D117',621,'Supplier1','Pendant',1584),('D118',515,'Supplier6','Necklace',550),('D119',251,'Supplier7','Chain',2135),('D120',240,'Supplier3','Pendant',1262),('D121',494,'Supplier9','Ring',1587),('D122',72,'Supplier2','Chain',1912),('D123',435,'Supplier7','Chain',945),('D124',166,'Supplier7','Chain',964),('D125',146,'Supplier6','Bracelet',1803),('D126',984,'Supplier7','Earrings',1920),('D127',975,'Supplier5','Bracelet',2255),('D128',161,'Supplier7','Nose Ring',1566),('D129',436,'Supplier2','Anklet',2073),('D130',102,'Supplier7','Nose Ring',1810),('D131',58,'Supplier8','Chain',417),('D132',669,'Supplier1','Anklet',2281),('D133',633,'Supplier1','Pendant',2087),('D134',581,'Supplier2','Nose Ring',605),('D135',111,'Supplier9','Necklace',413),('D136',899,'Supplier3','Chain',573),('D137',302,'Supplier10','Chain',727),('D138',704,'Supplier9','Nose Ring',879),('D139',652,'Supplier3','Pendant',421),('D140',554,'Supplier5','Bangles',988),('D141',498,'Supplier4','Necklace',443),('D142',746,'Supplier5','Ring',1893),('D143',910,'Supplier2','Nose Ring',1120),('D144',202,'Supplier6','Earrings',664),('D145',783,'Supplier4','Bracelet',877),('D146',723,'Supplier7','Bracelet',799),('D147',507,'Supplier7','Chain',763),('D148',655,'Supplier5','Anklet',727),('D149',486,'Supplier7','Nose Ring',1395),('D150',75,'Supplier5','Earrings',1089),('D151',861,'Supplier1','Chain',392),('D152',670,'Supplier2','Pendant',2143),('D153',650,'Supplier4','Ring',652),('D154',685,'Supplier9','Ring',1975),('D155',71,'Supplier6','Nose Ring',2000),('D156',488,'Supplier8','Earrings',1682),('D157',317,'Supplier4','Chain',516),('D158',122,'Supplier1','Ring',558),('D159',490,'Supplier10','Bracelet',1325),('D160',791,'Supplier4','Anklet',798),('D161',853,'Supplier5','Chain',2074),('D162',845,'Supplier6','Necklace',2280),('D163',630,'Supplier1','Pendant',1697),('D164',735,'Supplier6','Earrings',1016),('D165',678,'Supplier3','Necklace',636),('D166',358,'Supplier2','Pendant',1705),('D167',697,'Supplier3','Anklet',1022),('D168',477,'Supplier2','Anklet',2133),('D169',175,'Supplier10','Necklace',485),('D170',967,'Supplier6','Chain',1443),('D171',363,'Supplier10','Bangles',1428),('D172',814,'Supplier4','Bracelet',589),('D173',95,'Supplier9','Chain',2251),('D174',282,'Supplier3','Bangles',1971),('D175',946,'Supplier3','Necklace',1177),('D176',577,'Supplier5','Pendant',1857),('D177',998,'Supplier7','Bracelet',1059),('D178',142,'Supplier4','Anklet',720),('D179',712,'Supplier9','Necklace',926),('D180',90,'Supplier3','Bracelet',1079),('D181',259,'Supplier10','Ring',759),('D182',173,'Supplier10','Earrings',726),('D183',817,'Supplier4','Anklet',1442),('D184',241,'Supplier3','Chain',1279),('D185',830,'Supplier7','Bangles',1673),('D186',311,'Supplier4','Bangles',1953),('D187',875,'Supplier9','Bangles',2096),('D188',432,'Supplier4','Earrings',2159),('D189',544,'Supplier9','Anklet',398),('D190',769,'Supplier7','Nose Ring',1588),('D191',515,'Supplier6','Ring',2209),('D192',500,'Supplier6','Necklace',456),('D193',77,'Supplier10','Anklet',1393),('D194',402,'Supplier3','Nose Ring',602),('D195',886,'Supplier10','Ring',1513),('D196',778,'Supplier1','Bangles',1767),('D197',439,'Supplier9','Ring',1240),('D198',379,'Supplier4','Pendant',2051),('D199',690,'Supplier8','Bangles',678),('D200',905,'Supplier10','Nose Ring',2270);
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
  KEY `item_id` (`item_id`),
  KEY `bill_id` (`bill_id`),
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
  `sno` int NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `idx_slip_id` (`slip_id`),
  KEY `idx_customer_name` (`customer_name`),
  KEY `idx_slip_type` (`slip_type`),
  KEY `fk_design_id` (`design_id`),
  CONSTRAINT `customer_fk` FOREIGN KEY (`customer_name`) REFERENCES `customers` (`customer_name`) ON DELETE CASCADE,
  CONSTRAINT `fk_design_id` FOREIGN KEY (`design_id`) REFERENCES `inventory` (`DesignID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_slip_id` FOREIGN KEY (`slip_id`) REFERENCES `order_slips_main` (`slip_id`) ON DELETE CASCADE,
  CONSTRAINT `order_slips_ibfk_1` FOREIGN KEY (`slip_type`) REFERENCES `ordertype` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips`
--

LOCK TABLES `order_slips` WRITE;
/*!40000 ALTER TABLE `order_slips` DISABLE KEYS */;
INSERT INTO `order_slips` VALUES ('Kachhe Me Jama','Amit Sharma',1395.00,'Chain',10,5.50,'2022-01-17 21:46:48',101,'Gold Plated','D001',1,97,1),('Kachhe Me Jama','Amit Sharma',1379.00,'Anklet',5,10.20,'2022-01-17 21:46:48',102,'Silver Base','D002',1,98,2),('Kachhe Me Jama','Amit Sharma',1641.00,'Anklet',7,6.30,'2022-01-17 21:46:48',103,'Handmade','D003',1,99,3),('Kachhe Me Jama','Amit Sharma',829.00,'Bracelet',15,2.00,'2022-01-17 21:46:48',104,'Repolishing','D004',1,100,4),('Kachhe Me Jama','Amit Sharma',1378.00,'Bangles',3,7.50,'2022-01-17 21:46:48',105,'Gold Chain','D005',1,101,5),('Kachhe Ka Baaki','Arjun Nair',964.00,'Necklace',12,6.10,'2022-04-10 11:02:38',106,'Rose Gold','D006',2,102,1),('Kachhe Ka Baaki','Arjun Nair',957.00,'Bracelet',4,9.80,'2022-04-10 11:02:38',107,'Engraved','D007',2,103,2),('Kachhe Ka Baaki','Arjun Nair',1989.00,'Ring',9,5.00,'2022-04-10 11:02:38',108,'Classic Design','D008',2,104,3),('Kachhe Ka Baaki','Arjun Nair',2095.00,'Nose Ring',20,2.80,'2022-04-10 11:02:38',109,'Diamond Studded','D009',2,105,4),('Kachhe Ka Baaki','Arjun Nair',968.00,'Bangles',5,8.40,'2022-04-10 11:02:38',110,'Silver Chain','D010',2,106,5),('Repairing','Kavita Das',2022.00,'Ring',8,4.20,'2022-05-24 19:22:49',111,'Antique','D011',3,107,1),('Repairing','Kavita Das',696.00,'Bracelet',6,11.00,'2022-05-24 19:22:49',112,'Vintage','D012',3,108,2),('Repairing','Kavita Das',694.00,'Earrings',10,5.50,'2022-05-24 19:22:49',113,'Pearl Embedded','D013',3,109,3),('Repairing','Kavita Das',1365.00,'Necklace',18,3.10,'2022-05-24 19:22:49',114,'Polished','D014',3,110,4),('Repairing','Kavita Das',1779.00,'Chain',7,7.20,'2022-05-24 19:22:49',115,'Oxidized Silver','D015',3,111,5),('Kachhe Me Jama','Meera Iyer',1322.00,'Bracelet',11,4.70,'2022-04-28 21:16:12',116,'Thin Band','D016',4,112,1),('Kachhe Me Jama','Meera Iyer',1097.00,'Chain',5,12.30,'2022-04-28 21:16:12',117,'Floral Design','D017',4,113,2),('Kachhe Me Jama','Meera Iyer',1026.00,'Nose Ring',6,6.70,'2022-04-28 21:16:12',118,'Gold Base','D018',4,114,3),('Kachhe Me Jama','Meera Iyer',880.00,'Earrings',14,2.90,'2022-04-28 21:16:12',119,'Stone Studded','D019',4,115,4),('Kachhe Me Jama','Meera Iyer',544.00,'Necklace',4,9.50,'2022-04-28 21:16:12',120,'Pure Silver','D020',4,116,5),('Kachhe Ka Baaki','Neha Joshi',1046.00,'Pendant',9,5.30,'2022-06-08 05:42:36',121,'Handcrafted','D021',5,117,1),('Kachhe Ka Baaki','Neha Joshi',598.00,'Necklace',7,10.70,'2022-06-08 05:42:36',122,'Gold Leaf','D022',5,118,2),('Kachhe Ka Baaki','Neha Joshi',2263.00,'Bangles',11,7.10,'2022-06-08 05:42:36',123,'Adjustable','D023',5,119,3),('Kachhe Ka Baaki','Neha Joshi',1284.00,'Chain',22,3.50,'2022-06-08 05:42:36',124,'Oxidized Finish','D024',5,120,4),('Kachhe Ka Baaki','Neha Joshi',766.00,'Bracelet',6,8.90,'2022-06-08 05:42:36',125,'Traditional Design','D025',5,121,5),('Kachhe Me Jama','Amit Sharma',1395.00,'Chain',2,5.25,'2022-05-15 18:14:26',101,'Gold Plated','D001',6,122,1),('Kachhe Me Jama','Amit Sharma',1379.00,'Anklet',1,10.50,'2022-05-15 18:14:26',102,'Silver Plated','D002',6,123,2),('Kachhe Me Jama','Amit Sharma',1641.00,'Anklet',3,3.75,'2022-05-15 18:14:26',103,'Copper Finish','D003',6,124,3),('Kachhe Me Jama','Amit Sharma',829.00,'Bracelet',4,4.25,'2022-05-15 18:14:26',104,'Gold Polish','D004',6,125,4),('Kachhe Me Jama','Amit Sharma',1378.00,'Bangles',2,6.00,'2022-05-15 18:14:26',105,'Matte Finish','D005',6,126,5),('Kachhe Ka Baaki','Priya Verma',964.00,'Necklace',3,4.75,'2022-07-19 07:34:22',106,'Gold Plated','D006',7,127,1),('Kachhe Ka Baaki','Priya Verma',957.00,'Bracelet',1,9.25,'2022-07-19 07:34:22',107,'Silver Plated','D007',7,128,2),('Kachhe Ka Baaki','Priya Verma',1989.00,'Ring',2,4.00,'2022-07-19 07:34:22',108,'Copper Finish','D008',7,129,3),('Kachhe Ka Baaki','Priya Verma',2095.00,'Nose Ring',5,5.75,'2022-07-19 07:34:22',109,'Gold Polish','D009',7,130,4),('Kachhe Ka Baaki','Priya Verma',968.00,'Bangles',3,6.50,'2022-07-19 07:34:22',110,'Matte Finish','D010',7,131,5),('Repairing','Rohan Gupta',2022.00,'Ring',2,5.00,'2022-10-17 12:38:35',111,'Gold Plated','D011',8,132,1),('Repairing','Rohan Gupta',696.00,'Bracelet',1,10.00,'2022-10-17 12:38:35',112,'Silver Plated','D012',8,133,2),('Repairing','Rohan Gupta',694.00,'Earrings',3,3.50,'2022-10-17 12:38:35',113,'Copper Finish','D013',8,134,3),('Repairing','Rohan Gupta',1365.00,'Necklace',4,4.50,'2022-10-17 12:38:35',114,'Gold Polish','D014',8,135,4),('Repairing','Rohan Gupta',1779.00,'Chain',2,6.25,'2022-10-17 12:38:35',115,'Matte Finish','D015',8,136,5),('Kachhe Me Jama','Sneha Kapoor',1322.00,'Bracelet',3,4.00,'2022-09-01 21:59:51',116,'Gold Plated','D016',9,137,1),('Kachhe Me Jama','Sneha Kapoor',1097.00,'Chain',1,8.75,'2022-09-01 21:59:51',117,'Silver Plated','D017',9,138,2),('Kachhe Me Jama','Sneha Kapoor',1026.00,'Nose Ring',2,3.25,'2022-09-01 21:59:51',118,'Copper Finish','D018',9,139,3),('Kachhe Me Jama','Sneha Kapoor',880.00,'Earrings',5,5.50,'2022-09-01 21:59:51',119,'Gold Polish','D019',9,140,4),('Kachhe Me Jama','Sneha Kapoor',544.00,'Necklace',3,6.75,'2022-09-01 21:59:51',120,'Matte Finish','D020',9,141,5),('Kachhe Ka Baaki','Meera Iyer',1046.00,'Pendant',2,4.50,'2022-02-18 05:33:32',121,'Gold Plated','D021',10,142,1),('Kachhe Ka Baaki','Meera Iyer',598.00,'Necklace',1,9.00,'2022-02-18 05:33:32',122,'Silver Plated','D022',10,143,2),('Kachhe Ka Baaki','Meera Iyer',2263.00,'Bangles',3,3.75,'2022-02-18 05:33:32',123,'Copper Finish','D023',10,144,3),('Kachhe Ka Baaki','Meera Iyer',1284.00,'Chain',4,5.25,'2022-02-18 05:33:32',124,'Gold Polish','D024',10,145,4),('Kachhe Ka Baaki','Meera Iyer',766.00,'Bracelet',2,6.50,'2022-02-18 05:33:32',125,'Matte Finish','D025',10,146,5),('Kachhe Me Jama','Arjun Nair',572.00,'Pendant',3,4.25,'2022-04-26 15:18:05',126,'Gold Plated','D026',11,147,1),('Kachhe Me Jama','Arjun Nair',1867.00,'Anklet',1,9.50,'2022-04-26 15:18:05',127,'Silver Plated','D027',11,148,2),('Kachhe Me Jama','Arjun Nair',1635.00,'Earrings',2,3.50,'2022-04-26 15:18:05',128,'Copper Finish','D028',11,149,3),('Kachhe Me Jama','Arjun Nair',1392.00,'Earrings',4,5.50,'2022-04-26 15:18:05',129,'Gold Polish','D029',11,150,4),('Kachhe Me Jama','Arjun Nair',550.00,'Necklace',3,6.75,'2022-04-26 15:18:05',130,'Matte Finish','D030',11,151,5),('Kachhe Ka Baaki','Neha Joshi',1800.00,'Earrings',2,4.75,'2022-05-11 17:19:53',131,'Gold Plated','D031',12,152,1),('Kachhe Ka Baaki','Neha Joshi',525.00,'Bangles',1,10.25,'2022-05-11 17:19:53',132,'Silver Plated','D032',12,153,2),('Kachhe Ka Baaki','Neha Joshi',1941.00,'Pendant',3,3.75,'2022-05-11 17:19:53',133,'Copper Finish','D033',12,154,3),('Kachhe Ka Baaki','Neha Joshi',735.00,'Necklace',3,6.00,'2022-05-11 17:19:53',134,'Gold Polish','D034',12,155,4),('Kachhe Ka Baaki','Neha Joshi',743.00,'Nose Ring',2,7.25,'2022-05-11 17:19:53',135,'Matte Finish','D035',12,156,5),('Kachhe Me Jama','Vikram Singh',1034.00,'Chain',3,4.00,'2022-01-04 22:15:14',136,'Gold Plated','D036',13,157,1),('Kachhe Me Jama','Vikram Singh',2058.00,'Earrings',1,8.50,'2022-01-04 22:15:14',137,'Silver Plated','D037',13,158,2),('Kachhe Me Jama','Vikram Singh',1891.00,'Nose Ring',2,3.25,'2022-01-04 22:15:14',138,'Copper Finish','D038',13,159,3),('Kachhe Me Jama','Vikram Singh',1563.00,'Pendant',4,5.25,'2022-01-04 22:15:14',139,'Gold Polish','D039',13,160,4),('Kachhe Me Jama','Vikram Singh',555.00,'Necklace',3,6.50,'2022-01-04 22:15:14',140,'Matte Finish','D040',13,161,5),('Kachhe Ka Baaki','Meera Iyer',1467.00,'Bracelet',2,4.50,'2022-08-22 16:46:27',141,'Gold Plated','D041',14,162,1),('Kachhe Ka Baaki','Meera Iyer',1840.00,'Earrings',1,9.00,'2022-08-22 16:46:27',142,'Silver Plated','D042',14,163,2),('Kachhe Ka Baaki','Meera Iyer',1755.00,'Pendant',3,3.75,'2022-08-22 16:46:27',143,'Copper Finish','D043',14,164,3),('Kachhe Ka Baaki','Meera Iyer',618.00,'Pendant',4,5.25,'2022-08-22 16:46:27',144,'Gold Polish','D044',14,165,4),('Kachhe Ka Baaki','Meera Iyer',1778.00,'Anklet',2,6.50,'2022-08-22 16:46:27',145,'Matte Finish','D045',14,166,5),('Kachhe Me Jama','Kavita Das',1205.00,'Bangles',3,4.75,'2022-09-05 22:36:38',146,'Gold Plated','D046',15,167,1),('Kachhe Me Jama','Kavita Das',984.00,'Bangles',1,10.00,'2022-09-05 22:36:38',147,'Silver Plated','D047',15,168,2),('Kachhe Me Jama','Kavita Das',1588.00,'Bangles',2,4.00,'2022-09-05 22:36:38',148,'Copper Finish','D048',15,169,3),('Kachhe Me Jama','Kavita Das',665.00,'Nose Ring',5,5.75,'2022-09-05 22:36:38',149,'Gold Polish','D049',15,170,4),('Kachhe Me Jama','Kavita Das',1320.00,'Chain',3,6.25,'2022-09-05 22:36:38',150,'Matte Finish','D050',15,171,5),('Kachhe Me Jama','Amit Sharma',708.00,'Ring',2,5.00,'2022-08-24 20:13:53',151,'Gold Plated','D051',16,172,1),('Kachhe Me Jama','Amit Sharma',1552.00,'Pendant',1,9.75,'2022-08-24 20:13:53',152,'Silver Plated','D052',16,173,2),('Kachhe Me Jama','Amit Sharma',792.00,'Pendant',3,4.00,'2022-08-24 20:13:53',153,'Copper Finish','D053',16,174,3),('Kachhe Me Jama','Amit Sharma',1814.00,'Pendant',4,5.75,'2022-08-24 20:13:53',154,'Gold Polish','D054',16,175,4),('Kachhe Me Jama','Amit Sharma',1640.00,'Necklace',3,6.50,'2022-08-24 20:13:53',155,'Matte Finish','D055',16,176,5),('Kachhe Ka Baaki','Sneha Kapoor',1251.00,'Bangles',2,4.25,'2022-05-13 14:49:33',156,'Gold Plated','D056',17,177,1),('Kachhe Ka Baaki','Sneha Kapoor',2128.00,'Bracelet',1,8.75,'2022-05-13 14:49:33',157,'Silver Plated','D057',17,178,2),('Kachhe Ka Baaki','Sneha Kapoor',2126.00,'Anklet',2,3.50,'2022-05-13 14:49:33',158,'Copper Finish','D058',17,179,3),('Kachhe Ka Baaki','Sneha Kapoor',537.00,'Nose Ring',5,6.00,'2022-05-13 14:49:33',159,'Gold Polish','D059',17,180,4),('Kachhe Ka Baaki','Sneha Kapoor',1911.00,'Bracelet',3,7.00,'2022-05-13 14:49:33',160,'Matte Finish','D060',17,181,5),('Kachhe Ka Baaki','Sneha Kapoor',1506.00,'Anklet',2,5.50,'2022-05-13 14:49:33',161,'Glossy Finish','D061',17,182,6),('Kachhe Me Jama','Priya Verma',1318.00,'Pendant',2,4.50,'2022-09-16 18:56:07',162,'Gold Plated','D062',18,183,1),('Kachhe Me Jama','Priya Verma',1985.00,'Anklet',1,9.25,'2022-09-16 18:56:07',163,'Silver Plated','D063',18,184,2),('Kachhe Me Jama','Priya Verma',1654.00,'Pendant',3,4.25,'2022-09-16 18:56:07',164,'Copper Finish','D064',18,185,3),('Kachhe Me Jama','Priya Verma',304.00,'Anklet',5,5.50,'2022-09-16 18:56:07',165,'Gold Polish','D065',18,186,4),('Kachhe Me Jama','Priya Verma',2199.00,'Earrings',3,6.75,'2022-09-16 18:56:07',166,'Matte Finish','D066',18,187,5),('Kachhe Me Jama','Priya Verma',1163.00,'Nose Ring',2,5.25,'2022-09-16 18:56:07',167,'Glossy Finish','D067',18,188,6),('Kachhe Me Jama','Priya Verma',494.00,'Pendant',4,3.75,'2022-09-16 18:56:07',168,'Antique Look','D068',18,189,7),('Kachhe Ka Baaki','Rohan Gupta',1178.00,'Chain',3,4.75,'2022-10-18 07:41:59',169,'Gold Plated','D069',19,190,1),('Kachhe Ka Baaki','Rohan Gupta',1779.00,'Earrings',1,9.50,'2022-10-18 07:41:59',170,'Silver Plated','D070',19,191,2),('Kachhe Ka Baaki','Rohan Gupta',325.00,'Ring',3,4.00,'2022-10-18 07:41:59',171,'Copper Finish','D071',19,192,3),('Kachhe Ka Baaki','Rohan Gupta',1383.00,'Pendant',4,5.50,'2022-10-18 07:41:59',172,'Gold Polish','D072',19,193,4),('Kachhe Ka Baaki','Rohan Gupta',1623.00,'Ring',2,6.25,'2022-10-18 07:41:59',173,'Matte Finish','D073',19,194,5),('Kachhe Ka Baaki','Rohan Gupta',2241.00,'Nose Ring',3,5.00,'2022-10-18 07:41:59',174,'Glossy Finish','D074',19,195,6),('Kachhe Ka Baaki','Rohan Gupta',612.00,'Chain',5,3.50,'2022-10-18 07:41:59',175,'Antique Look','D075',19,196,7),('Kachhe Ka Baaki','Rohan Gupta',336.00,'Bangles',4,7.00,'2022-10-18 07:41:59',176,'Traditional','D076',19,197,8),('Kachhe Me Jama','Meera Iyer',1362.00,'Pendant',2,5.25,'2022-03-11 11:11:36',177,'Gold Plated','D077',20,198,1),('Kachhe Me Jama','Meera Iyer',1506.00,'Bracelet',1,9.00,'2022-03-11 11:11:36',178,'Silver Plated','D078',20,199,2),('Kachhe Me Jama','Meera Iyer',1843.00,'Pendant',3,4.50,'2022-03-11 11:11:36',179,'Copper Finish','D079',20,200,3),('Kachhe Me Jama','Meera Iyer',1910.00,'Pendant',5,6.00,'2022-03-11 11:11:36',180,'Gold Polish','D080',20,201,4),('Kachhe Me Jama','Meera Iyer',1541.00,'Anklet',2,6.75,'2022-03-11 11:11:36',181,'Matte Finish','D081',20,202,5),('Kachhe Me Jama','Meera Iyer',1554.00,'Ring',3,5.50,'2022-03-11 11:11:36',182,'Glossy Finish','D082',20,203,6),('Kachhe Me Jama','Amit Sharma',1400.00,'Ring',3,5.50,'2022-03-24 14:22:03',201,'Gold Plated','D101',21,348,1),('Kachhe Me Jama','Amit Sharma',1100.00,'Necklace',1,10.00,'2022-03-24 14:22:03',202,'Silver Plated','D102',21,349,2),('Kachhe Me Jama','Amit Sharma',890.00,'Bracelet',2,4.75,'2022-03-24 14:22:03',203,'Copper Finish','D103',21,350,3),('Kachhe Me Jama','Amit Sharma',1200.00,'Earrings',4,6.00,'2022-03-24 14:22:03',204,'Gold Polish','D104',21,351,4),('Kachhe Me Jama','Amit Sharma',1025.00,'Anklet',3,7.25,'2022-03-24 14:22:03',205,'Matte Finish','D105',21,352,5),('Kachhe Ka Baaki','Sneha Kapoor',1650.00,'Ring',2,4.25,'2022-07-24 19:45:27',206,'Gold Plated','D106',22,353,1),('Kachhe Ka Baaki','Sneha Kapoor',1300.00,'Necklace',1,9.50,'2022-07-24 19:45:27',207,'Silver Plated','D107',22,354,2),('Kachhe Ka Baaki','Sneha Kapoor',1750.00,'Bracelet',3,6.00,'2022-07-24 19:45:27',208,'Copper Finish','D108',22,355,3),('Kachhe Ka Baaki','Sneha Kapoor',1900.00,'Earrings',5,4.50,'2022-07-24 19:45:27',209,'Gold Polish','D109',22,356,4),('Repairing','Vikram Singh',500.00,'Ring',2,4.00,'2022-06-20 13:11:11',210,'Gold Plated','D110',23,357,1),('Repairing','Vikram Singh',600.00,'Necklace',1,5.50,'2022-06-20 13:11:11',211,'Silver Plated','D111',23,358,2),('Repairing','Vikram Singh',700.00,'Bracelet',2,3.75,'2022-06-20 13:11:11',212,'Copper Finish','D112',23,359,3),('Repairing','Vikram Singh',800.00,'Earrings',3,4.25,'2022-06-20 13:11:11',213,'Gold Polish','D113',23,360,4),('Kachhe Me Jama','Priya Verma',1750.00,'Ring',3,5.00,'2022-08-27 12:09:35',214,'Gold Plated','D114',24,361,1),('Kachhe Me Jama','Priya Verma',1600.00,'Necklace',2,7.25,'2022-08-27 12:09:35',215,'Silver Plated','D115',24,362,2),('Kachhe Me Jama','Priya Verma',1550.00,'Bracelet',3,6.50,'2022-08-27 12:09:35',216,'Copper Finish','D116',24,363,3),('Kachhe Ka Baaki','Neha Joshi',1100.00,'Ring',2,3.50,'2022-03-17 02:44:26',217,'Gold Plated','D117',25,364,1),('Kachhe Ka Baaki','Neha Joshi',1250.00,'Necklace',1,6.75,'2022-03-17 02:44:26',218,'Silver Plated','D118',25,365,2),('Kachhe Ka Baaki','Neha Joshi',1300.00,'Bracelet',3,5.50,'2022-03-17 02:44:26',219,'Copper Finish','D119',25,366,3),('Repairing','Amit Sharma',550.00,'Earrings',4,3.75,'2022-09-24 06:43:23',220,'Gold Polish','D120',26,367,1),('Repairing','Amit Sharma',750.00,'Toe Ring',4,4.75,'2022-09-24 06:43:23',221,'Rose Gold','D121',26,368,2),('Kachhe Me Jama','Sneha Kapoor',1600.00,'Bangle',2,7.75,'2022-07-17 06:53:40',222,'Silver Shine','D122',27,369,1),('Kachhe Ka Baaki','Vikram Singh',1400.00,'Toe Ring',3,4.75,'2022-07-07 19:48:15',223,'Rose Gold','D123',28,370,1),('Repairing','Neha Joshi',700.00,'Bracelet',2,3.75,'2022-02-14 11:50:17',224,'Copper Finish','D124',29,371,1),('Kachhe Me Jama','Amit Sharma',1450.00,'Bangle',2,7.00,'2022-09-23 05:16:38',225,'Silver Shine','D125',30,372,1),('Kachhe Me Jama','Amit Sharma',1350.00,'Ring',2,4.50,'2022-10-11 20:22:25',226,'Gold Plated','D126',31,373,1),('Kachhe Me Jama','Amit Sharma',1800.00,'Necklace',1,9.75,'2022-10-11 20:22:25',227,'Silver Plated','D127',31,374,2),('Kachhe Me Jama','Amit Sharma',950.00,'Bracelet',3,5.00,'2022-10-11 20:22:25',228,'Copper Finish','D128',31,375,3),('Kachhe Me Jama','Amit Sharma',1150.00,'Earrings',4,6.25,'2022-10-11 20:22:25',229,'Gold Polish','D129',31,376,4),('Kachhe Me Jama','Amit Sharma',1250.00,'Anklet',3,7.50,'2022-10-11 20:22:25',230,'Matte Finish','D130',31,377,5),('Kachhe Ka Baaki','Sneha Kapoor',1600.00,'Ring',2,4.00,'2022-01-18 19:32:15',231,'Gold Plated','D131',32,378,1),('Kachhe Ka Baaki','Sneha Kapoor',1450.00,'Necklace',1,8.50,'2022-01-18 19:32:15',232,'Silver Plated','D132',32,379,2),('Kachhe Ka Baaki','Sneha Kapoor',1700.00,'Bracelet',3,6.25,'2022-01-18 19:32:15',233,'Copper Finish','D133',32,380,3),('Kachhe Ka Baaki','Sneha Kapoor',1900.00,'Earrings',5,5.00,'2022-01-18 19:32:15',234,'Gold Polish','D134',32,381,4),('Kachhe Ka Baaki','Sneha Kapoor',1750.00,'Toe Ring',2,3.50,'2022-01-18 19:32:15',235,'Rose Gold','D135',32,382,5),('Repairing','Vikram Singh',600.00,'Ring',2,3.75,'2022-05-27 18:03:58',236,'Gold Plated','D136',33,383,1),('Repairing','Vikram Singh',750.00,'Necklace',1,5.25,'2022-05-27 18:03:58',237,'Silver Plated','D137',33,384,2),('Repairing','Vikram Singh',800.00,'Bracelet',2,4.50,'2022-05-27 18:03:58',238,'Copper Finish','D138',33,385,3),('Repairing','Vikram Singh',950.00,'Earrings',3,4.75,'2022-05-27 18:03:58',239,'Gold Polish','D139',33,386,4),('Repairing','Vikram Singh',1050.00,'Bangle',2,6.50,'2022-05-27 18:03:58',240,'Silver Shine','D140',33,387,5),('Kachhe Me Jama','Priya Verma',1850.00,'Ring',3,5.50,'2022-03-16 13:13:08',241,'Gold Plated','D141',34,388,1),('Kachhe Me Jama','Priya Verma',1600.00,'Necklace',2,7.00,'2022-03-16 13:13:08',242,'Silver Plated','D142',34,389,2),('Kachhe Me Jama','Priya Verma',1550.00,'Bracelet',3,6.50,'2022-03-16 13:13:08',243,'Copper Finish','D143',34,390,3),('Kachhe Me Jama','Priya Verma',1400.00,'Earrings',4,5.75,'2022-03-16 13:13:08',244,'Gold Polish','D144',34,391,4),('Kachhe Me Jama','Priya Verma',1950.00,'Anklet',3,8.25,'2022-03-16 13:13:08',245,'Matte Finish','D145',34,392,5),('Kachhe Ka Baaki','Neha Joshi',1200.00,'Ring',2,4.00,'2022-08-24 17:23:45',246,'Gold Plated','D146',35,393,1),('Kachhe Ka Baaki','Neha Joshi',1350.00,'Necklace',1,7.50,'2022-08-24 17:23:45',247,'Silver Plated','D147',35,394,2),('Kachhe Ka Baaki','Neha Joshi',1450.00,'Bracelet',3,5.75,'2022-08-24 17:23:45',248,'Copper Finish','D148',35,395,3),('Kachhe Ka Baaki','Neha Joshi',1100.00,'Earrings',4,4.50,'2022-08-24 17:23:45',249,'Gold Polish','D149',35,396,4),('Kachhe Ka Baaki','Neha Joshi',1600.00,'Toe Ring',2,3.75,'2022-08-24 17:23:45',250,'Rose Gold','D150',35,397,5),('Kachhe Ka Baaki','Arjun Nair',1779.00,'Chain',5,1.00,'2025-03-20 17:36:59',1,'','D015',36,398,1),('Kachhe Ka Baaki','Arjun Nair',743.00,'Nose Ring',2,5.00,'2025-03-20 17:36:59',2,'','D035',36,399,2),('Kachhe Ka Baaki','Arjun Nair',598.00,'Necklace',33,2.00,'2025-03-20 17:36:59',3,'','D022',36,400,3),('Kachhe Ka Baaki','Arjun Nair',1089.00,'Earrings',8,3.20,'2025-03-20 17:36:59',4,'','D150',36,401,4);
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_slips_main`
--

LOCK TABLES `order_slips_main` WRITE;
/*!40000 ALTER TABLE `order_slips_main` DISABLE KEYS */;
INSERT INTO `order_slips_main` VALUES (1,'Kachhe Me Jama','2022-01-17 21:46:48'),(2,'Kachhe Ka Baaki','2022-04-10 11:02:38'),(3,'Repairing','2022-05-24 19:22:49'),(4,'Kachhe Me Jama','2022-04-28 21:16:12'),(5,'Kachhe Ka Baaki','2022-06-08 05:42:36'),(6,'Kachhe Me Jama','2022-05-15 18:14:26'),(7,'Kachhe Ka Baaki','2022-07-19 07:34:22'),(8,'Repairing','2022-10-17 12:38:35'),(9,'Kachhe Me Jama','2022-09-01 21:59:51'),(10,'Kachhe Ka Baaki','2022-02-18 05:33:32'),(11,'Kachhe Me Jama','2022-04-26 15:18:05'),(12,'Kachhe Ka Baaki','2022-05-11 17:19:53'),(13,'Kachhe Me Jama','2022-01-04 22:15:14'),(14,'Kachhe Ka Baaki','2022-08-22 16:46:27'),(15,'Kachhe Me Jama','2022-09-05 22:36:38'),(16,'Kachhe Me Jama','2022-08-24 20:13:53'),(17,'Kachhe Ka Baaki','2022-05-13 14:49:33'),(18,'Kachhe Me Jama','2022-09-16 18:56:07'),(19,'Kachhe Ka Baaki','2022-10-18 07:41:59'),(20,'Kachhe Me Jama','2022-03-11 11:11:36'),(21,'Kachhe Me Jama','2022-03-24 14:22:03'),(22,'Kachhe Ka Baaki','2022-07-24 19:45:27'),(23,'Repairing','2022-06-20 13:11:11'),(24,'Kachhe Me Jama','2022-08-27 12:09:35'),(25,'Kachhe Ka Baaki','2022-03-17 02:44:26'),(26,'Repairing','2022-09-24 06:43:23'),(27,'Kachhe Me Jama','2022-07-17 06:53:40'),(28,'Kachhe Ka Baaki','2022-07-07 19:48:15'),(29,'Repairing','2022-02-14 11:50:17'),(30,'Kachhe Me Jama','2022-09-23 05:16:38'),(31,'Kachhe Me Jama','2022-10-11 20:22:25'),(32,'Kachhe Ka Baaki','2022-01-18 19:32:15'),(33,'Repairing','2022-05-27 18:03:58'),(34,'Kachhe Me Jama','2022-03-16 13:13:08'),(35,'Kachhe Ka Baaki','2022-08-24 17:23:45'),(36,'Kachhe Ka Baaki','2025-03-20 17:36:59');
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

--
-- Table structure for table `transaction_bill_link`
--

DROP TABLE IF EXISTS `transaction_bill_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_bill_link` (
  `link_id` bigint NOT NULL AUTO_INCREMENT,
  `transaction_id` bigint NOT NULL,
  `bill_id` int NOT NULL,
  `amount_linked` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`link_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `transaction_bill_link_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`transaction_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `transaction_bill_link_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`BillID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_bill_link`
--

LOCK TABLES `transaction_bill_link` WRITE;
/*!40000 ALTER TABLE `transaction_bill_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_bill_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `transaction_date` date NOT NULL,
  `payment_mode` varchar(50) NOT NULL,
  `notes` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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

-- Dump completed on 2025-03-20 23:53:20
