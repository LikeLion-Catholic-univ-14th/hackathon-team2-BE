-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: hackathon_2
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `archive_product_tags`
--

DROP TABLE IF EXISTS `archive_product_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archive_product_tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archive_product_id` bigint NOT NULL,
  `tag_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `archive_product_id` (`archive_product_id`),
  CONSTRAINT `archive_product_tags_ibfk_1` FOREIGN KEY (`archive_product_id`) REFERENCES `archive_products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archive_product_tags`
--

LOCK TABLES `archive_product_tags` WRITE;
/*!40000 ALTER TABLE `archive_product_tags` DISABLE KEYS */;
INSERT INTO `archive_product_tags` VALUES (1,1,'VISETOS'),(2,1,'COGNAC_Color'),(3,1,'MOBILITY'),(4,1,'GeoMetric_Structure'),(5,2,'Visetos'),(6,2,'Mobility'),(7,2,'Visible_Identity'),(8,2,'Metal Studs'),(9,3,'Visetos'),(10,3,'Miami_Blue'),(11,3,'Adaptive_Styling'),(12,3,'Cultural_Collaboration');
/*!40000 ALTER TABLE `archive_product_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `archive_products`
--

DROP TABLE IF EXISTS `archive_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archive_products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `short_description` text,
  `image_url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archive_products`
--

LOCK TABLES `archive_products` WRITE;
/*!40000 ALTER TABLE `archive_products` DISABLE KEYS */;
INSERT INTO `archive_products` VALUES (1,'Ottomar 비세토스 위켄더','MCM의 여행용 캐리어 헤리티지와 시그니처 비세토스를 담은 대표 웨컨더백',NULL),(2,'Stark 사이드 비세토스 백팩','블랙 비세토스와 피라미드 스터드로 도시적 이동성과 대담한 자기표현을 담은 MCM의 대표 백팩',NULL),(3,'SMCM X We The Best 비세토스 크로스바디 파우치','코냑 비세토스에 선명한 마이애미 블루와 음악 문화를 결합해 MCM 헤리티지를 자유롭게 재해석한 협업 파우치',NULL);
/*!40000 ALTER TABLE `archive_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `future_contexts`
--

DROP TABLE IF EXISTS `future_contexts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `future_contexts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `future_contexts`
--

LOCK TABLES `future_contexts` WRITE;
/*!40000 ALTER TABLE `future_contexts` DISABLE KEYS */;
INSERT INTO `future_contexts` VALUES (1,'Space Travel','무중력 이동과 행성 간 여행을 위한 미래 환경'),(2,'Hyper City','초고밀도 도시의 빠른 이동과 스마트 보안 환경'),(4,'Virtual Dimension','현실과 디지털 정체성이 연결된 융합 공간');
/*!40000 ALTER TABLE `future_contexts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `generation_locked_dna`
--

DROP TABLE IF EXISTS `generation_locked_dna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generation_locked_dna` (
  `generation_id` bigint NOT NULL,
  `heritage_dna_id` bigint NOT NULL,
  PRIMARY KEY (`generation_id`,`heritage_dna_id`),
  KEY `heritage_dna_id` (`heritage_dna_id`),
  CONSTRAINT `generation_locked_dna_ibfk_1` FOREIGN KEY (`generation_id`) REFERENCES `generations` (`id`),
  CONSTRAINT `generation_locked_dna_ibfk_2` FOREIGN KEY (`heritage_dna_id`) REFERENCES `heritage_dna` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `generation_locked_dna`
--

LOCK TABLES `generation_locked_dna` WRITE;
/*!40000 ALTER TABLE `generation_locked_dna` DISABLE KEYS */;
INSERT INTO `generation_locked_dna` VALUES (3,12),(3,13);
/*!40000 ALTER TABLE `generation_locked_dna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `generations`
--

DROP TABLE IF EXISTS `generations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archive_product_id` bigint NOT NULL,
  `future_context_id` bigint NOT NULL,
  `status` varchar(20) NOT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `description` text,
  `saved_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `archive_product_id` (`archive_product_id`),
  KEY `future_context_id` (`future_context_id`),
  CONSTRAINT `generations_ibfk_1` FOREIGN KEY (`archive_product_id`) REFERENCES `archive_products` (`id`),
  CONSTRAINT `generations_ibfk_2` FOREIGN KEY (`future_context_id`) REFERENCES `future_contexts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `generations`
--

LOCK TABLES `generations` WRITE;
/*!40000 ALTER TABLE `generations` DISABLE KEYS */;
INSERT INTO `generations` VALUES (3,1,2,'GENERATING',NULL,NULL,NULL,NULL,NULL,'2026-08-18 02:24:00');
/*!40000 ALTER TABLE `generations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `heritage_dna`
--

DROP TABLE IF EXISTS `heritage_dna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `heritage_dna` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archive_product_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `ratio` int DEFAULT NULL,
  `description` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtbrlvtx4ix6vgb591ane1brh4` (`archive_product_id`),
  CONSTRAINT `heritage_dna_ibfk_1` FOREIGN KEY (`archive_product_id`) REFERENCES `archive_products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heritage_dna`
--

LOCK TABLES `heritage_dna` WRITE;
/*!40000 ALTER TABLE `heritage_dna` DISABLE KEYS */;
INSERT INTO `heritage_dna` VALUES (12,1,'VISETOS',37,'한눈에 MCM임을 인식하게 하는 시그니처 모노그램'),(13,1,'MOBILITY',29,'여행용 러기지에서 출발한 제품의 본질적인 이동성'),(14,1,'COGNAC COLOR',22,'MCM의 클래식한 인상을 형성하는 대표적인 실루엣'),(15,1,'GEOMETRIC STRUCTURE',12,'안정적인 보스턴백 형태를 만드는 구조적 실루엣'),(16,2,'VISETOS',34,'블랙 톤으로 재해석된 시그니처 모노그램을 통해 MCM의 정체성을 직접적으로 보여준다.'),(17,2,'VISIBLE IDENTITY',28,'반복되는 로고 패턴과 강한 실루엣으로 대담한 자기표현의 가치를 드러낸다.'),(18,2,'METAL STUDS',23,'측면의 피라미드 스터드가 제품에 강렬하고 반항적인 이미지를 더한다.'),(19,2,'MOBILITY',15,'양손을 자유롭게 사용할 수 있는 백팩 구조로 도시 생활과 일상 이동에 적합하다.'),(20,3,'VISETOS',35,'코냑 비세토스 모노그램을 통해 MCM의 클래식한 브랜드 정체성을 가장 직접적으로 보여준다.'),(21,3,'CULTURAL COLLABORATION',30,'음악과 스트리트 문화의 감성을 결합해 전통적인 MCM 디자인을 새로운 문화적 언어로 재해석한다.'),(22,3,'MIAMI BLUE',20,'선명한 블루 컬러 포인트가 제품에 밝고 실험적인 에너지를 더한다.'),(23,3,'ADAPTIVE STYLING',15,'탈착식 스트랩을 통해 크로스바디와 클러치 두 가지 방식으로 활용할 수 있다.');
/*!40000 ALTER TABLE `heritage_dna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preset_scenarios`
--

DROP TABLE IF EXISTS `preset_scenarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preset_scenarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archive_product_id` bigint NOT NULL,
  `archive_product_name` varchar(255) NOT NULL,
  `future_context_id` bigint NOT NULL,
  `future_context_name` varchar(255) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `description` text,
  `image_url` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preset_scenarios`
--

LOCK TABLES `preset_scenarios` WRITE;
/*!40000 ALTER TABLE `preset_scenarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `preset_scenarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-19 22:26:44
