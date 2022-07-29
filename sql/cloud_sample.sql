CREATE DATABASE  IF NOT EXISTS `cloud_sample` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cloud_sample`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;



#  manage_device
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device`;

CREATE TABLE `manage_device` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `nickname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `device_type` int NOT NULL DEFAULT '-1',
  `sub_type` int NOT NULL DEFAULT '-1',
  `domain` int NOT NULL DEFAULT '-1',
  `firmware_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `device_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `child_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `bound_time` bigint DEFAULT NULL,
  `bound_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:bund; 1:not bound',
  `login_time` bigint DEFAULT NULL,
  `device_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `url_normal` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `url_select` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_sn_UNIQUE` (`device_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



#  manage_device_dictionary
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device_dictionary`;

CREATE TABLE `manage_device_dictionary` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `domain` int NOT NULL,
  `device_type` int NOT NULL,
  `sub_type` int NOT NULL,
  `device_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `device_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

LOCK TABLES `manage_device_dictionary` WRITE;
/*!40000 ALTER TABLE `manage_device_dictionary` DISABLE KEYS */;

INSERT INTO `manage_device_dictionary` (`id`, `domain`, `device_type`, `sub_type`, `device_name`, `device_desc`)
VALUES
	(1,0,60,0,'Matrice 300 RTK',NULL),
	(2,0,67,0,'Matrice 30',NULL),
	(3,0,67,1,'Matrice 30T',NULL),
	(4,1,20,0,'Z30',NULL),
	(5,1,26,0,'XT2',NULL),
	(6,1,39,0,'FPV',NULL),
	(7,1,41,0,'XTS',NULL),
	(8,1,42,0,'H20',NULL),
	(9,1,43,0,'H20T',NULL),
	(10,1,50,65535,'P1','include 24 and 35 and 50mm'),
	(11,1,52,0,'M30 Camera',NULL),
	(12,1,53,0,'M30T Camera',NULL),
	(13,1,61,0,'H20N',NULL),
	(14,1,165,0,'DJI Dock Camera',NULL),
	(15,1,90742,0,'L1',NULL),
	(16,2,56,0,'DJI Smart Controller','Remote control for M300'),
	(17,2,119,0,'DJI RC Plus','Remote control for M30'),
	(18,3,1,0,'DJI Dock','');

/*!40000 ALTER TABLE `manage_device_dictionary` ENABLE KEYS */;
UNLOCK TABLES;


# manage_device_hms
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device_hms`;

CREATE TABLE `manage_device_hms` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `hms_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `tid` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bid` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sn` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level` smallint NOT NULL,
  `module` tinyint NOT NULL,
  `hms_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `message_zh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message_en` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_hms_id` (`hms_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


#  manage_device_payload
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device_payload`;

CREATE TABLE `manage_device_payload` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `payload_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `payload_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `payload_type` smallint NOT NULL,
  `sub_type` smallint NOT NULL,
  `firmware_version` varchar(32) DEFAULT NULL,
  `payload_index` smallint NOT NULL,
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `payload_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `payload_sn_UNIQUE` (`payload_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



#  manage_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_user`;

CREATE TABLE `manage_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_id` varchar(64) NOT NULL DEFAULT '',
  `user_type` smallint NOT NULL,
  `mqtt_username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `mqtt_password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

LOCK TABLES `manage_user` WRITE;
/*!40000 ALTER TABLE `manage_user` DISABLE KEYS */;

INSERT INTO `manage_user` (`id`, `user_id`, `username`, `password`, `workspace_id`, `user_type`, `mqtt_username`, `mqtt_password`, `create_time`, `update_time`)
VALUES
	(1,'a1559e7c-8dd8-4780-b952-100cc4797da2','adminPC','adminPC','e3dea0f5-37f2-4d79-ae58-490af3228069',1,'admin','admin',1634898410751,1650880112310),
	(2,'be7c6c3d-afe9-4be4-b9eb-c55066c0914e','pilot','pilot123','e3dea0f5-37f2-4d79-ae58-490af3228069',2,'pilot','pilot123',1634898410751,1634898410751);

/*!40000 ALTER TABLE `manage_user` ENABLE KEYS */;
UNLOCK TABLES;


#  manage_workspace
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_workspace`;

CREATE TABLE `manage_workspace` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `platform_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `bind_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `workspace_id_UNIQUE` (`workspace_id`),
  UNIQUE KEY `bind_code_UNIQUE` (`bind_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

LOCK TABLES `manage_workspace` WRITE;
/*!40000 ALTER TABLE `manage_workspace` DISABLE KEYS */;

INSERT INTO `manage_workspace` (`id`, `workspace_id`, `workspace_name`, `workspace_desc`, `platform_name`, `create_time`, `update_time`, `bind_code`)
VALUES
	(1,'e3dea0f5-37f2-4d79-ae58-490af3228069','Test Group One','Cloud Sample Test Platform','Cloud Api Platform',1634898410751,1634898410751,'qwe');

/*!40000 ALTER TABLE `manage_workspace` ENABLE KEYS */;
UNLOCK TABLES;


#  map_element_coordinate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `map_element_coordinate`;

CREATE TABLE `map_element_coordinate` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `element_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `longitude` decimal(18,14) NOT NULL,
  `latitude` decimal(17,14) NOT NULL,
  `altitude` decimal(17,14) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



#  map_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `map_group`;

CREATE TABLE `map_group` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `group_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `group_type` int NOT NULL,
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `is_distributed` tinyint(1) NOT NULL DEFAULT '1',
  `is_lock` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id_UNIQUE` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

LOCK TABLES `map_group` WRITE;
/*!40000 ALTER TABLE `map_group` DISABLE KEYS */;

INSERT INTO `map_group` (`id`, `group_id`, `group_name`, `group_type`, `workspace_id`, `is_distributed`, `is_lock`, `create_time`, `update_time`)
VALUES
	(1,'e3dea0f5-37f2-4d79-ae58-490af3228060','Pilot Share Layer',2,'e3dea0f5-37f2-4d79-ae58-490af3228069',1,0,1638330077356,1638330077356),
	(2,'e3dea0f5-37f2-4d79-ae58-490af3228011','Default Layer',1,'e3dea0f5-37f2-4d79-ae58-490af3228069',1,0,1638330077356,1638330077356);

/*!40000 ALTER TABLE `map_group` ENABLE KEYS */;
UNLOCK TABLES;


#  map_group_element
# ------------------------------------------------------------

DROP TABLE IF EXISTS `map_group_element`;

CREATE TABLE `map_group_element` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `element_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `element_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `display` smallint NOT NULL DEFAULT '1',
  `group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `element_type` smallint NOT NULL,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `color` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `clamp_to_ground` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `element_id_UNIQUE` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;



#  media_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `media_file`;

CREATE TABLE `media_file` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `fingerprint` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `tinny_fingerprint` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `object_key` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `sub_file_type` int DEFAULT NULL,
  `is_original` tinyint(1) NOT NULL,
  `drone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `payload` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `job_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



#  wayline_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wayline_file`;

CREATE TABLE `wayline_file` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `wayline_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `drone_model_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `payload_model_keys` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `sign` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'MD5',
  `favorited` tinyint(1) NOT NULL DEFAULT '0',
  `template_types` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `object_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL COMMENT 'required, can''t modify.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `wayline_id_UNIQUE` (`wayline_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



#  wayline_job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wayline_job`;

CREATE TABLE `wayline_job` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `job_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `file_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `dock_sn` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `bid` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `job_id_UNIQUE` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

