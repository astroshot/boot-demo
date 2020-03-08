CREATE TABLE `province` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` bigint(20) NOT NULL COMMENT '省代码',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '省名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_code_name` (`code`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `province_code` bigint(20) NOT NULL DEFAULT '0' COMMENT '省代码',
  `code` bigint(20) NOT NULL COMMENT '市代码',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '市名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_code_name` (`province_code`,`code`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `county` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `city_code` bigint(20) NOT NULL DEFAULT '0' COMMENT '市代码',
  `code` bigint(20) NOT NULL COMMENT '区代码',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '区名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_code_name` (`city_code`,`code`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `town` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `county_code` bigint(20) NOT NULL DEFAULT '0' COMMENT '区代码',
  `code` bigint(20) NOT NULL COMMENT '乡镇代码',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '乡镇名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_code_name` (`county_code`,`code`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;