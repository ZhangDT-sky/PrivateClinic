-- 导出 pcms 的数据库结构
CREATE DATABASE IF NOT EXISTS `pcms` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pcms`;

-- 导出  表 pcms.drug 结构
CREATE TABLE IF NOT EXISTS `drug` (
  `drug_id` bigint NOT NULL AUTO_INCREMENT,
  `drug_name` varchar(100) NOT NULL,
  `specification` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int NOT NULL,
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`drug_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 pcms.medical_case 结构
CREATE TABLE IF NOT EXISTS `medical_case` (
  `case_id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `symptom` text,
  `diagnosis` text,
  `case_status` varchar(20) NOT NULL,
  `visit_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`case_id`),
  KEY `fk_case_patient` (`patient_id`),
  KEY `fk_case_doctor` (`doctor_id`),
  CONSTRAINT `fk_case_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_case_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 pcms.patient 结构
CREATE TABLE IF NOT EXISTS `patient` (
  `patient_id` bigint NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(50) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`patient_id`),
  KEY `fk_patient_doctor` (`doctor_id`),
  CONSTRAINT `fk_patient_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 pcms.prescription 结构
CREATE TABLE IF NOT EXISTS `prescription` (
  `prescription_id` bigint NOT NULL AUTO_INCREMENT,
  `case_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`prescription_id`),
  KEY `fk_prescription_case` (`case_id`),
  KEY `fk_prescription_doctor` (`doctor_id`),
  CONSTRAINT `fk_prescription_case` FOREIGN KEY (`case_id`) REFERENCES `medical_case` (`case_id`),
  CONSTRAINT `fk_prescription_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 pcms.prescription_item 结构
CREATE TABLE IF NOT EXISTS `prescription_item` (
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `prescription_id` bigint NOT NULL,
  `drug_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `usage_method` varchar(200) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `fk_item_prescription` (`prescription_id`),
  KEY `fk_item_drug` (`drug_id`),
  CONSTRAINT `fk_item_drug` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `fk_item_prescription` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 pcms.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `role` varchar(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

