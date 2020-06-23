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
INSERT INTO `Group` VALUES (1,'CS5500Team4',NULL),(2,'CS5700Team1','Password1'),(3,'Library3rdFloor',NULL),(4,'STEMstudent',NULL),(5,'Align',NULL),(6,'WomenInStem',NULL),(7,'Running',NULL),(8,'Debug',NULL),(9,'Brunch','password2');
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Group_has_Group`
--

LOCK TABLES `Group_has_Group` WRITE;
/*!40000 ALTER TABLE `Group_has_Group` DISABLE KEYS */;
INSERT INTO `Group_has_Group` VALUES (1,2),(2,3),(2,4),(3,4),(6,5),(2,7),(6,7),(4,8),(5,8),(6,8),(7,8),(9,8);
/*!40000 ALTER TABLE `Group_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Invitation`
--

LOCK TABLES `Invitation` WRITE;
/*!40000 ALTER TABLE `Invitation` DISABLE KEYS */;
INSERT INTO `Invitation` VALUES (1,3,1),(1,6,1),(2,3,1),(2,4,1),(2,8,1),(3,4,1),(3,5,1),(3,8,1),(4,1,1),(5,5,1),(5,9,1),(7,2,1),(7,7,1),(8,1,1),(8,2,1),(9,2,1);
/*!40000 ALTER TABLE `Invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'Rouni111','Password111',NULL,'en',1,NULL,0),(2,'Rob111','Password111',NULL,'en',0,NULL,0),(3,'Qingjing111','Password111',NULL,'en',1,NULL,0),(4,'Peiran111','Password111',NULL,'en',1,NULL,0),(5,'Harshil111','Password111',NULL,'en',0,NULL,0),(6,'Yijia111','Password111',NULL,'en',1,NULL,0),(7,'Zoe111','Password111',NULL,'en',0,NULL,0),(8,'Mike111','Password111',NULL,'en',0,NULL,1),(9,'Harry111','Password111',NULL,'en',0,NULL,1);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_follows_Group`
--

LOCK TABLES `User_follows_Group` WRITE;
/*!40000 ALTER TABLE `User_follows_Group` DISABLE KEYS */;
INSERT INTO `User_follows_Group` VALUES (4,1),(4,2),(2,3),(3,3),(3,4),(5,4),(3,5),(1,6),(1,7),(4,7),(6,7),(7,7),(2,8),(8,8),(9,8),(1,9);
/*!40000 ALTER TABLE `User_follows_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_follows_User`
--

LOCK TABLES `User_follows_User` WRITE;
/*!40000 ALTER TABLE `User_follows_User` DISABLE KEYS */;
INSERT INTO `User_follows_User` VALUES (3,1),(4,1),(8,1),(1,2),(8,2),(9,2),(1,3),(2,3),(4,3),(5,3),(6,3),(7,3),(1,4),(3,4),(5,4),(1,5),(3,5),(4,5),(3,6),(3,7),(6,7),(8,7),(9,7),(2,8),(3,8),(4,8),(7,8),(2,9),(7,9);
/*!40000 ALTER TABLE `User_follows_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_has_Group`
--

LOCK TABLES `User_has_Group` WRITE;
/*!40000 ALTER TABLE `User_has_Group` DISABLE KEYS */;
INSERT INTO `User_has_Group` VALUES (1,1),(2,1),(3,1),(5,1),(6,1),(7,1),(1,2),(2,2),(3,2),(4,2),(5,2),(3,3),(5,3),(6,3),(7,3),(8,3),(1,4),(4,4),(8,4),(1,5),(2,5),(4,5),(3,6),(4,6),(3,7),(8,7),(9,7),(4,8),(5,8),(6,8),(2,9),(4,9),(6,9),(7,9),(8,9),(9,9);
/*!40000 ALTER TABLE `User_has_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User_moderates_Group`
--

LOCK TABLES `User_moderates_Group` WRITE;
/*!40000 ALTER TABLE `User_moderates_Group` DISABLE KEYS */;
INSERT INTO `User_moderates_Group` VALUES (1,1),(3,1),(6,1),(1,2),(3,3),(1,4),(4,5),(3,6),(4,6),(3,7),(4,8),(4,9),(6,9);
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

-- Dump completed on 2020-06-23  0:46:02

-- -----------------------------------------------------
-- Table `mydb`.`Message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Message` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Message` (
  `Message_id` INT NOT NULL auto_increment,
  `fromName` VARCHAR(25) NOT NULL,
  `toName` VARCHAR(25) NOT NULL,
  `message` VARCHAR(50) NOT NULL,
  `messageDate` VARCHAR(15) NOT NULL,
  `messageTimeStamp` VARCHAR(35) NOT NULL,
  `sendToGroup` TINYINT NOT NULL DEFAULT 0,
  `groupId` INT NOT NULL DEFAULT -1,
  PRIMARY KEY (`Message_id`))
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;