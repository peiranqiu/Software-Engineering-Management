-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: mydb.cd4ztimoe6ek.us-east-1.rds.amazonaws.com    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Group` (
  `Group_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Group_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
INSERT INTO `Group` VALUES (1,'testChatGroup1',NULL),(2,'Abcdef1234','test1234'),(5,'testModerateGroup3',NULL),(7,'testGroup1',NULL),(8,'testGroup2',NULL),(9,'testGroup3',NULL),(11,'testModerateGroup11',NULL),(12,'testFollowGroup5702',NULL),(13,'testFollowGroup8278',NULL),(14,'testFollowGroup9231',NULL),(15,'testFollowGroup6963',NULL),(16,'testFollowGroup9767',NULL),(17,'testFollowGroup2255',NULL),(18,'testFollowGroup6463',NULL),(19,'testFollowGroup4674',NULL),(20,'testFollowGroup3388',NULL),(21,'testFollowGroup8805',NULL),(22,'testQJ123',NULL),(24,'testQJ234',NULL),(25,'group1',NULL),(26,'group2',NULL),(27,'group3',NULL),(28,'group4',NULL),(34,'group5',NULL),(35,'group101',NULL),(36,'group102',NULL),(37,'group103',NULL),(38,'test103',NULL),(39,'test202',NULL),(40,'test303',NULL);
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Group_has_Group`
--

DROP TABLE IF EXISTS `Group_has_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Group_has_Group` (
  `super_Group_id` int(11) NOT NULL,
  `sub_Group_id` int(11) NOT NULL,
  PRIMARY KEY (`super_Group_id`,`sub_Group_id`),
  KEY `fk_Group_has_Group_Group2_idx` (`sub_Group_id`),
  KEY `fk_Group_has_Group_Group1_idx` (`super_Group_id`),
  CONSTRAINT `fk_Group_has_Group_Group1` FOREIGN KEY (`super_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Group_has_Group_Group2` FOREIGN KEY (`sub_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group_has_Group`
--

LOCK TABLES `Group_has_Group` WRITE;
/*!40000 ALTER TABLE `Group_has_Group` DISABLE KEYS */;
INSERT INTO `Group_has_Group` VALUES (7,1),(9,2),(7,8),(7,9);
/*!40000 ALTER TABLE `Group_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invitation`
--

DROP TABLE IF EXISTS `Invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Invitation` (
  `User_User_id` int(11) NOT NULL,
  `Group_Group_id` int(11) NOT NULL,
  `isAdd` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`User_User_id`,`Group_Group_id`),
  KEY `fk_Invitation_Group1_idx` (`Group_Group_id`),
  KEY `fk_Invitation_User_idx` (`User_User_id`),
  CONSTRAINT `fk_Invitation_Group1` FOREIGN KEY (`Group_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Invitation_User` FOREIGN KEY (`User_User_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invitation`
--

LOCK TABLES `Invitation` WRITE;
/*!40000 ALTER TABLE `Invitation` DISABLE KEYS */;
/*!40000 ALTER TABLE `Invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `User_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `language` varchar(2) NOT NULL DEFAULT 'en',
  `isModerator` tinyint(4) NOT NULL DEFAULT '0',
  `logins` varchar(45) DEFAULT NULL,
  `watched` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`User_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'testName1','User1Password',NULL,'en',1,NULL,0),(2,'testName2','User2Password',NULL,'en',0,NULL,0),(3,'User123Test112','User123Test111',NULL,'en',1,NULL,0),(4,'qingjingQJ123','test123Test',NULL,'en',1,NULL,0),(5,'Abcdef1234','Abc12345',NULL,'en',0,NULL,0),(7,'HarryPotter1','Harry12345','picture.png','en',1,NULL,0),(9,'testModerator1','Password1',NULL,'en',1,NULL,0),(10,'testModerator3','Password3',NULL,'en',0,NULL,0),(11,'testModerator1993','Password3936',NULL,'en',0,NULL,0),(12,'testModerator7469','Password1837',NULL,'en',0,NULL,0),(13,'testModerator7405','Password3276',NULL,'en',0,NULL,0),(14,'testModerator5727','Password1202',NULL,'en',0,NULL,0),(15,'testModerator9396','Password6700',NULL,'en',0,NULL,0),(16,'testModerator4','Password4',NULL,'en',0,NULL,0),(17,'testModerator8285','Password9251',NULL,'en',0,NULL,0),(18,'testModerator11','Password1',NULL,'en',1,NULL,0),(19,'testModerator33','Password3',NULL,'en',0,NULL,0),(20,'testModerator6035','Password3683',NULL,'en',0,NULL,0),(21,'testModerator5027','Password6456',NULL,'en',0,NULL,0),(22,'testModerator492','Password2348',NULL,'en',0,NULL,0),(23,'testModerator9374','Password1237',NULL,'en',0,NULL,0),(24,'testModerator44','Password4',NULL,'en',0,NULL,0),(25,'testModerator1955','Password2424',NULL,'en',0,NULL,0),(26,'testModerator321','Password8756',NULL,'en',0,NULL,0),(27,'testFollowUser7018','Password1',NULL,'en',0,NULL,0),(28,'testFollowUser7160','Password1',NULL,'en',0,NULL,0),(29,'testFollowUser7743','Password1',NULL,'en',0,NULL,0),(30,'testFollowUser8062','Password1',NULL,'en',0,NULL,0),(31,'testFollowUser3941','Password2',NULL,'en',0,NULL,0),(32,'testFollowUser3130','Password1',NULL,'en',0,NULL,0),(33,'testFollowUser9989','Password1',NULL,'en',0,NULL,0),(34,'testFollowUser2425','Password2',NULL,'en',0,NULL,0),(35,'testFollowUser3261','Password1',NULL,'en',0,NULL,0),(36,'testFollowUser8868','Password2',NULL,'en',0,NULL,0),(37,'testFollowUser9178','Password1',NULL,'en',0,NULL,0),(38,'testFollowUser7038','Password1',NULL,'en',0,NULL,0),(39,'testFollowUser1696','Password1',NULL,'en',0,NULL,0),(40,'testFollowUser4730','Password2',NULL,'en',0,NULL,0),(41,'testFollowUser249','Password1',NULL,'en',0,NULL,0),(42,'testFollowUser2169','Password2',NULL,'en',0,NULL,0),(43,'testFollowUser4930','Password1',NULL,'en',0,NULL,0),(44,'testFollowUser9640','Password2',NULL,'en',0,NULL,0),(45,'testFollowUser1867','Password1',NULL,'en',0,NULL,0),(46,'testFollowUser6871','Password1',NULL,'en',0,NULL,0),(47,'testFollowUser1723','Password1',NULL,'en',0,NULL,0),(48,'testFollowUser6152','Password2',NULL,'en',0,NULL,0),(49,'testFollowUser2739','Password1',NULL,'en',0,NULL,0),(50,'testFollowUser444','Password1',NULL,'en',0,NULL,0),(51,'testFollowUser1474','Password2',NULL,'en',0,NULL,0),(52,'testFollowUser7834','Password1',NULL,'en',0,NULL,0),(53,'testFollowUser8988','Password2',NULL,'en',0,NULL,0),(54,'testFollowUser5037','Password1',NULL,'en',0,NULL,0),(55,'testFollowUser4675','Password1',NULL,'en',0,NULL,0),(56,'testFollowUser1827','Password2',NULL,'en',0,NULL,0),(57,'testFollowUser407','Password1',NULL,'en',0,NULL,0),(58,'testFollowUser2305','Password2',NULL,'en',0,NULL,0),(59,'testFollowUser5106','Password1',NULL,'en',0,NULL,0),(60,'testFollowUser8263','Password2',NULL,'en',0,NULL,0);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_follows_Group`
--

DROP TABLE IF EXISTS `User_follows_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_follows_Group` (
  `User_User_id` int(11) NOT NULL,
  `Group_Group_id` int(11) NOT NULL,
  PRIMARY KEY (`User_User_id`,`Group_Group_id`),
  KEY `fk_User_has_Group1_Group2_idx` (`Group_Group_id`),
  KEY `fk_User_has_Group1_User2_idx` (`User_User_id`),
  CONSTRAINT `fk_User_has_Group1_Group2` FOREIGN KEY (`Group_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Group1_User2` FOREIGN KEY (`User_User_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_follows_Group`
--

LOCK TABLES `User_follows_Group` WRITE;
/*!40000 ALTER TABLE `User_follows_Group` DISABLE KEYS */;
INSERT INTO `User_follows_Group` VALUES (1,13),(29,13),(1,14),(32,14),(1,15),(35,15),(36,15);
/*!40000 ALTER TABLE `User_follows_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_follows_User`
--

DROP TABLE IF EXISTS `User_follows_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_follows_User` (
  `follower_id` int(11) NOT NULL,
  `followee_id` int(11) NOT NULL,
  PRIMARY KEY (`follower_id`,`followee_id`),
  KEY `fk_User_has_User_User2_idx` (`followee_id`),
  KEY `fk_User_has_User_User1_idx` (`follower_id`),
  CONSTRAINT `fk_User_has_User_User1` FOREIGN KEY (`follower_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_User_User2` FOREIGN KEY (`followee_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_follows_User`
--

LOCK TABLES `User_follows_User` WRITE;
/*!40000 ALTER TABLE `User_follows_User` DISABLE KEYS */;
INSERT INTO `User_follows_User` VALUES (2,1),(3,1),(5,1),(41,1),(42,1),(1,2),(1,3),(1,4),(30,31),(33,34),(1,43),(1,44),(43,44);
/*!40000 ALTER TABLE `User_follows_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_has_Group`
--

DROP TABLE IF EXISTS `User_has_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_has_Group` (
  `User_User_id` int(11) NOT NULL,
  `Group_Group_id` int(11) NOT NULL,
  PRIMARY KEY (`User_User_id`,`Group_Group_id`),
  KEY `fk_User_has_Group_Group1_idx` (`Group_Group_id`),
  KEY `fk_User_has_Group_User_idx` (`User_User_id`),
  CONSTRAINT `fk_User_has_Group_Group1` FOREIGN KEY (`Group_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Group_User` FOREIGN KEY (`User_User_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_has_Group`
--

LOCK TABLES `User_has_Group` WRITE;
/*!40000 ALTER TABLE `User_has_Group` DISABLE KEYS */;
INSERT INTO `User_has_Group` VALUES (1,1),(2,1),(3,1),(1,2),(1,5),(9,5),(10,5),(1,7),(3,8),(4,40);
/*!40000 ALTER TABLE `User_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_moderates_Group`
--

DROP TABLE IF EXISTS `User_moderates_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_moderates_Group` (
  `User_User_id` int(11) NOT NULL,
  `Group_Group_id` int(11) NOT NULL,
  PRIMARY KEY (`User_User_id`,`Group_Group_id`),
  KEY `fk_User_has_Group1_Group1_idx` (`Group_Group_id`),
  KEY `fk_User_has_Group1_User1_idx` (`User_User_id`),
  CONSTRAINT `fk_User_has_Group1_Group1` FOREIGN KEY (`Group_Group_id`) REFERENCES `Group` (`Group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Group1_User1` FOREIGN KEY (`User_User_id`) REFERENCES `User` (`User_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_moderates_Group`
--

LOCK TABLES `User_moderates_Group` WRITE;
/*!40000 ALTER TABLE `User_moderates_Group` DISABLE KEYS */;
INSERT INTO `User_moderates_Group` VALUES (1,1),(1,2),(1,5),(9,5),(3,7),(3,8),(4,40);
/*!40000 ALTER TABLE `User_moderates_Group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-16 19:59:05
