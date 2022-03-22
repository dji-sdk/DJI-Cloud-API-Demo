CREATE DATABASE  IF NOT EXISTS `cloud_sample` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cloud_sample`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# manage_camera_video
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_camera_video`;

CREATE TABLE `manage_camera_video` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `camera_id` int NOT NULL,
  `video_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `video_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



# manage_capacity_camera
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_capacity_camera`;

CREATE TABLE `manage_capacity_camera` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `camera_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `coexist_video_number_max` int NOT NULL,
  `available_video_number` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



# manage_device
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device`;

CREATE TABLE `manage_device` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `device_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `device_type` smallint NOT NULL,
  `sub_type` smallint NOT NULL,
  `domain` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `version` smallint NOT NULL,
  `device_index` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `child_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `device_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url_normal` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url_select` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_sn_UNIQUE` (`device_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



# manage_device_dictionary
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
	(16,2,56,0,'DJI Smart Controller','Remote control for M300'),
	(17,1,20,0,'Z30',NULL),
	(18,1,26,0,'XT2',NULL),
	(19,1,39,0,'FPV',NULL),
	(20,1,41,0,'XTS',NULL),
	(21,1,42,0,'H20',NULL),
	(22,1,43,0,'H20T',NULL),
	(24,1,90742,0,'L1',NULL),
	(27,1,50,0,'P1 24mm lens',NULL),
	(28,1,50,1,'P1 35mm lens',NULL),
	(29,1,50,2,'P1 50mm lens',NULL),
	(30,0,67,0,'Matrice 30',NULL),
    (31,0,67,1,'Matrice 30T',NULL),
    (32,2,119,0,'DJI RC Plus','Remote control for M30'),
    (33,1,52,0,'M30',NULL),
    (34,1,53,0,'M30T',NULL);

/*!40000 ALTER TABLE `manage_device_dictionary` ENABLE KEYS */;
UNLOCK TABLES;


# manage_device_payload
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_device_payload`;

CREATE TABLE `manage_device_payload` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `payload_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `payload_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `payload_type` smallint NOT NULL,
  `sub_type` smallint NOT NULL,
  `version` smallint DEFAULT NULL,
  `payload_index` smallint NOT NULL,
  `device_sn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `payload_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `payload_sn_UNIQUE` (`payload_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



# manage_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `manage_user`;

CREATE TABLE `manage_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_id` int NOT NULL,
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
	(1,'a1559e7c-8dd8-4780-b952-100cc4797da2','adminPC','adminPC',1,1,'admin','admin',1634898410751,1634898410751),
	(2,'be7c6c3d-afe9-4be4-b9eb-c55066c0914e','pilot','pilot123',1,2,'pilot','pilot123',1634898410751,1634898410751);

/*!40000 ALTER TABLE `manage_user` ENABLE KEYS */;
UNLOCK TABLES;


# manage_workspace
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `workspace_id_UNIQUE` (`workspace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

LOCK TABLES `manage_workspace` WRITE;
/*!40000 ALTER TABLE `manage_workspace` DISABLE KEYS */;

INSERT INTO `manage_workspace` (`id`, `workspace_id`, `workspace_name`, `workspace_desc`, `platform_name`, `create_time`, `update_time`)
VALUES
	(1,'e3dea0f5-37f2-4d79-ae58-490af3228069','Test Group One','Cloud Sample Test Platform','Cloud Api Platform',1634898410751,1634898410751);

/*!40000 ALTER TABLE `manage_workspace` ENABLE KEYS */;
UNLOCK TABLES;


# map_element_coordinate
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



# map_group
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


# map_group_element
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



# media_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `media_file`;

CREATE TABLE `media_file` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `fingerprint` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `tinny_fingerprint` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `object_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `sub_file_type` int NOT NULL,
  `is_original` tinyint(1) NOT NULL,
  `drone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `payload` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'undefined',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fingerprint_UNIQUE` (`fingerprint`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



# wayline_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wayline_file`;

CREATE TABLE `wayline_file` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `wayline_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `drone_model_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `payload_model_keys` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `workspace_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `favorited` tinyint(1) NOT NULL DEFAULT '0',
  `template_types` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `object_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL COMMENT 'required, can not modify.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `wayline_id_UNIQUE` (`wayline_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

