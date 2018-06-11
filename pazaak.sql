-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: ID222177_g53.db.webhosting.be    Database: ID222177_g53
-- ------------------------------------------------------
-- Server version	5.7.14-8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Card`
--

DROP TABLE IF EXISTS `Card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Card` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) NOT NULL,
  `value` int(11) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Card`
--

LOCK TABLES `Card` WRITE;
/*!40000 ALTER TABLE `Card` DISABLE KEYS */;
INSERT INTO `Card` VALUES (1,'P',2,''),(2,'P',4,''),(3,'P',5,''),(4,'P',6,''),(5,'S',1,''),(6,'S',3,''),(7,'N',1,''),(8,'N',2,''),(9,'N',3,''),(10,'N',5,'');
/*!40000 ALTER TABLE `Card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Game`
--

DROP TABLE IF EXISTS `Game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Game` (
  `game_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_name` varchar(45) NOT NULL,
  PRIMARY KEY (`game_id`),
  UNIQUE KEY `game_id_UNIQUE` (`game_id`),
  UNIQUE KEY `game_name_UNIQUE` (`game_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Game`
--

LOCK TABLES `Game` WRITE;
/*!40000 ALTER TABLE `Game` DISABLE KEYS */;
INSERT INTO `Game` VALUES (1,'Game1'),(4,'Game10'),(2,'Game8'),(3,'Game9'),(5,'Leen-versus-Kodak');
/*!40000 ALTER TABLE `Game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Game_Player`
--

DROP TABLE IF EXISTS `Game_Player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Game_Player` (
  `game_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `sideDeckCard` varchar(255) NOT NULL COMMENT 'setScore: current score of the player in the game\n',
  `setScore` int(11) DEFAULT NULL,
  PRIMARY KEY (`game_id`,`player_id`),
  KEY `player__id_idx` (`player_id`),
  CONSTRAINT `game_id` FOREIGN KEY (`game_id`) REFERENCES `Game` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `player__id` FOREIGN KEY (`player_id`) REFERENCES `Player` (`player_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Game_Player`
--

LOCK TABLES `Game_Player` WRITE;
/*!40000 ALTER TABLE `Game_Player` DISABLE KEYS */;
INSERT INTO `Game_Player` VALUES (1,1,'+;5#±;3#-;1#+;2#',0),(1,10,'±;2#+;5#+;2#-;4#',0),(2,1,'±;1#±;5#±;2#±;3#',0),(2,10,'±;1#±;5#±;4#±;2#',0),(3,1,'±;2#±;3#±;5#±;1#',0),(3,10,'±;2#±;2#±;3#±;5#',0),(4,1,'±;4#±;1#±;5#±;3#',1),(4,10,'±;1#±;4#±;3#±;2#',0),(5,1,'±;3#±;2#±;4#±;2#',0),(5,10,'±;2#±;3#±;5#±;4#',0);
/*!40000 ALTER TABLE `Game_Player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Player`
--

DROP TABLE IF EXISTS `Player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Player` (
  `player_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `birthyear` int(4) NOT NULL,
  `credit` int(11) NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player`
--

LOCK TABLES `Player` WRITE;
/*!40000 ALTER TABLE `Player` DISABLE KEYS */;
INSERT INTO `Player` VALUES (1,'Senne',1995,30),(2,'Hilda',1996,5),(3,'Senne1',1995,5),(4,'Denis',1997,5),(5,'Senne26',1995,20),(6,'Jean-Luc',1954,70),(7,'Alain',1958,5),(8,'Luke1',1990,5),(9,'Luke2',1990,5),(10,'TestKerel',1995,10),(11,'Senne250',1995,5),(12,'Senne260',1995,5),(13,'aaaaaa',1995,5),(14,'kkekke',1995,5),(15,'kkekk',1995,5),(16,'kkek',1995,5),(17,'kke',1995,5),(18,'senne',1995,5),(19,'senne1',1995,5),(20,'Denis2',1997,5),(21,'RRR',1997,5),(22,'Frederik',1995,5),(23,'Stijn',1994,5),(24,'i1Talen',2000,5),(25,'PrebenLeroy',1997,5),(26,'FrederikCool2',1995,5),(27,'///',1992,5),(28,'leenVuyge',1996,5),(29,'kodak',1996,5),(30,'NederlandseLeen',1996,5),(31,'LeenEnFrancais',1996,5),(32,'Bart',2001,5),(33,'Leo10',1990,5);
/*!40000 ALTER TABLE `Player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Player_Card`
--

DROP TABLE IF EXISTS `Player_Card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Player_Card` (
  `Player_player_id` int(11) NOT NULL,
  `Card_card_id` int(11) NOT NULL,
  PRIMARY KEY (`Player_player_id`,`Card_card_id`),
  KEY `fk_Player_has_Card_Card1_idx` (`Card_card_id`),
  KEY `fk_Player_has_Card_Player_idx` (`Player_player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Player_Card`
--

LOCK TABLES `Player_Card` WRITE;
/*!40000 ALTER TABLE `Player_Card` DISABLE KEYS */;
INSERT INTO `Player_Card` VALUES (1,1),(-1,3),(1,3),(1,5),(2,6);
/*!40000 ALTER TABLE `Player_Card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'ID222177_g53'
--

--
-- Dumping routines for database 'ID222177_g53'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-24 20:10:55
