CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 8.0.12, for macos10.13 (x86_64)
--
-- Host: mydb.cd4ztimoe6ek.us-east-1.rds.amazonaws.com    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
INSERT INTO `Group` VALUES (2,'testModerateGroup3','\"TestModerateGroup3\"'),(3,'testChatGroup1',NULL),(4,'Abcdef1234',NULL),(6,'testGroup1',NULL),(7,'testGroup2',NULL),(8,'testGroup3',NULL),(10,'testFollowGroup8375',NULL),(11,'testFollowGroup5731',NULL),(12,'testFollowGroup3413',NULL),(13,'testFollowGroup4097',NULL),(14,'testFollowGroup1828',NULL),(15,'qjjjj1',NULL),(16,'NewGRoup',NULL),(17,'tesgt123','\"Tesgt1234\"');
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Group_has_Group`
--

LOCK TABLES `Group_has_Group` WRITE;
/*!40000 ALTER TABLE `Group_has_Group` DISABLE KEYS */;
INSERT INTO `Group_has_Group` VALUES (6,7),(6,8);
/*!40000 ALTER TABLE `Group_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Invitation`
--

LOCK TABLES `Invitation` WRITE;
/*!40000 ALTER TABLE `Invitation` DISABLE KEYS */;
/*!40000 ALTER TABLE `Invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'Abcdef1234','Abc12345',NULL,'en',0,NULL,0),(3,'testModerator1','Password1',NULL,'en',1,NULL,0),(4,'testModerator3','Password3',NULL,'en',0,NULL,0),(5,'testModerator5996','Password5375',NULL,'en',0,NULL,0),(6,'testModerator2338','Password827',NULL,'en',0,NULL,0),(7,'testModerator1515','Password3036',NULL,'en',0,NULL,0),(8,'testModerator3019','Password6910',NULL,'en',0,NULL,0),(9,'testModerator8676','Password4600',NULL,'en',0,NULL,0),(10,'testModerator4','Password4',NULL,'en',0,NULL,0),(11,'testModerator8322','Password3084',NULL,'en',0,NULL,0),(12,'testName1','User1Password',NULL,'en',1,NULL,0),(13,'testName2','User2Password',NULL,'en',0,NULL,0),(14,'HarryPotter1','Harry12345','picture.png','en',1,NULL,0),(16,'testFollowUser4224','Password1',NULL,'en',0,NULL,0),(17,'testFollowUser3730','Password1',NULL,'en',0,NULL,0),(18,'testFollowUser1695','Password1',NULL,'en',0,NULL,0),(19,'testFollowUser9270','Password2',NULL,'en',0,NULL,0),(20,'testFollowUser390','Password1',NULL,'en',0,NULL,0),(21,'testFollowUser9987','Password1',NULL,'en',0,NULL,0),(22,'testFollowUser9474','Password2',NULL,'en',0,NULL,0),(23,'testFollowUser850','Password1',NULL,'en',0,NULL,0),(24,'testFollowUser9595','Password2',NULL,'en',0,NULL,0),(25,'testFollowUser1037','Password1',NULL,'en',0,NULL,0),(26,'testFollowUser6305','Password1',NULL,'en',0,NULL,0),(27,'testFollowUser3788','Password2',NULL,'en',0,NULL,0),(28,'testFollowUser7088','Password1',NULL,'en',0,NULL,0),(29,'testFollowUser9048','Password2',NULL,'en',0,NULL,0),(30,'testFollowUser6759','Password1',NULL,'en',0,NULL,0),(31,'testFollowUser9819','Password2',NULL,'en',0,NULL,0),(32,'Danie123','Daa123',NULL,'en',0,NULL,0),(33,'Testuser12345','Testuser12345',NULL,'en',0,NULL,0),(34,'Alice124','Alice124',NULL,'en',0,NULL,0),(35,'Alice123','Alice123',NULL,'en',0,NULL,0);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_follows_Group`
--

LOCK TABLES `User_follows_Group` WRITE;
/*!40000 ALTER TABLE `User_follows_Group` DISABLE KEYS */;
INSERT INTO `User_follows_Group` VALUES (1,2),(4,2),(3,3),(3,4),(3,8),(3,10),(3,11),(3,15),(3,16);
/*!40000 ALTER TABLE `User_follows_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_follows_User`
--

LOCK TABLES `User_follows_User` WRITE;
/*!40000 ALTER TABLE `User_follows_User` DISABLE KEYS */;
INSERT INTO `User_follows_User` VALUES (3,1),(1,3),(5,3),(3,4),(3,5);
/*!40000 ALTER TABLE `User_follows_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_has_Group`
--

LOCK TABLES `User_has_Group` WRITE;
/*!40000 ALTER TABLE `User_has_Group` DISABLE KEYS */;
INSERT INTO `User_has_Group` VALUES (3,2),(4,2),(13,3),(3,15),(3,16),(3,17);
/*!40000 ALTER TABLE `User_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_moderates_Group`
--

LOCK TABLES `User_moderates_Group` WRITE;
/*!40000 ALTER TABLE `User_moderates_Group` DISABLE KEYS */;
INSERT INTO `User_moderates_Group` VALUES (3,2),(12,3),(3,15),(3,16),(3,17);
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

-- Dump completed on 2020-06-19  0:18:48
