-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.13 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 collect_information 的数据库结构
CREATE DATABASE IF NOT EXISTS `collect_information` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `collect_information`;

-- 导出  表 collect_information.account 结构
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `username` varchar(50) NOT NULL DEFAULT '0' COMMENT '用户名',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态；0：禁用；1：启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 正在导出表  collect_information.account 的数据：~0 rows (大约)
DELETE FROM `account`;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`id`, `gmt_create`, `gmt_modified`, `username`, `status`) VALUES
	(1, '2019-07-14 10:05:24', '2019-07-14 10:05:25', 'XXXX', 1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

-- 导出  表 collect_information.account_resource_subscribe 结构
CREATE TABLE IF NOT EXISTS `account_resource_subscribe` (
  `account_id` bigint(20) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `last_subscribe_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次订阅时间',
  KEY `FK1_account_id` (`account_id`),
  KEY `FK2_resource_id` (`resource_id`),
  CONSTRAINT `FK1_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK2_resource_id` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订阅表';

-- 正在导出表  collect_information.account_resource_subscribe 的数据：~0 rows (大约)
DELETE FROM `account_resource_subscribe`;
/*!40000 ALTER TABLE `account_resource_subscribe` DISABLE KEYS */;
INSERT INTO `account_resource_subscribe` (`account_id`, `resource_id`, `last_subscribe_time`) VALUES
	(1, 1, '2019-07-12 12:19:19'),
	(1, 2, '2019-07-12 12:20:02'),
	(1, 3, '2019-07-12 12:20:20'),
	(1, 4, '2019-07-12 12:20:29'),
	(1, 5, '2019-07-12 13:04:17'),
	(1, 6, '2019-07-12 13:04:44'),
	(1, 7, '2019-07-12 13:04:52'),
	(1, 8, '2019-07-12 13:05:08');
/*!40000 ALTER TABLE `account_resource_subscribe` ENABLE KEYS */;

-- 导出  表 collect_information.contact_notify 结构
CREATE TABLE IF NOT EXISTS `contact_notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `notify_type` varchar(50) DEFAULT NULL COMMENT '通知类型：dingding；mail',
  `notify_value` varchar(128) DEFAULT NULL COMMENT '钉钉token；mail',
  `status` tinyint(4) DEFAULT NULL COMMENT '0：禁用；1：启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='通知方式';

-- 正在导出表  collect_information.contact_notify 的数据：~0 rows (大约)
DELETE FROM `contact_notify`;
/*!40000 ALTER TABLE `contact_notify` DISABLE KEYS */;
INSERT INTO `contact_notify` (`id`, `gmt_create`, `gmt_modified`, `account_id`, `notify_type`, `notify_value`, `status`) VALUES
	(1, '2019-07-14 11:09:59', '2019-07-14 10:10:00', 1, 'dingding', 'xxxxxxxxxxxxxxxxxxxxxxxxxx', 1);
/*!40000 ALTER TABLE `contact_notify` ENABLE KEYS */;

-- 导出  表 collect_information.content 结构
CREATE TABLE IF NOT EXISTS `content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `url` varchar(256) NOT NULL COMMENT '内容项URL',
  `title` varchar(256) NOT NULL COMMENT '内容标题',
  `description` text COMMENT '内容描述',
  `publish_time` varchar(50) DEFAULT NULL COMMENT '发布时间',
  `image_url` varchar(256) DEFAULT NULL COMMENT '图片地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '0：禁用；1：启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_url` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8 COMMENT='内容管理';


-- 导出  表 collect_information.content_subscribe 结构
CREATE TABLE IF NOT EXISTS `content_subscribe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `account_id` bigint(20) NOT NULL COMMENT '订阅用户ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `content_id` bigint(20) NOT NULL COMMENT '内容ID',
  `content_title` varchar(256) NOT NULL COMMENT '内容标题',
  `last_subscribe_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '弃用：最近一次订阅的时间',
  `send_status` tinyint(4) NOT NULL COMMENT '订阅状态；0：未订阅；1：订阅成功；2：订阅失败',
  `retry_time` tinyint(4) NOT NULL DEFAULT '0' COMMENT '重试次数；最大重发次数 = 5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8 COMMENT='订阅管理';

-- 导出  表 collect_information.resource 结构
CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `url` varchar(50) NOT NULL COMMENT '资源URL',
  `link` varchar(50) NOT NULL COMMENT '首页',
  `parser` varchar(50) NOT NULL DEFAULT 'xml' COMMENT '解析器： xml；html',
  `template` text COMMENT '解析模板',
  `description` varchar(256) DEFAULT NULL COMMENT '资源描述',
  `period` bigint(20) NOT NULL DEFAULT '1000' COMMENT '查询时间间隔',
  `next_execute_time` datetime NOT NULL COMMENT '下次执行时间',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0：禁用；1：启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='订阅资源库';

-- 正在导出表  collect_information.resource 的数据：~0 rows (大约)
DELETE FROM `resource`;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` (`id`, `gmt_create`, `gmt_modified`, `url`, `link`, `parser`, `template`, `description`, `period`, `next_execute_time`, `status`) VALUES
	(1, '2019-07-13 22:31:28', '2019-07-14 11:01:30', 'http://www.ityouknow.com/feed.xml', 'http://www.ityouknow.com', 'xml', '{\n	"channel": {\n		"tag": "channel",\n		"attr": ""\n	},\n	"item": {\n		"tag": "item",\n		"attr": ""\n	},\n	"title": {\n		"tag": "title",\n		"attr": ""\n	},\n	"description": {\n		"tag": "description",\n		"attr": ""\n	},\n	"pubDate": {\n		"tag": "pubDate",\n		"attr": ""\n	},\n	"link": {\n		"tag": "link",\n		"attr": ""\n	},\n	"category": {\n		"tag": "category",\n		"attr": ""\n	}\n}', '纯洁的微笑', 100000, '2019-07-15 13:15:10', 1),
	(2, '2019-07-13 22:31:28', '2019-07-14 11:48:56', 'https://coolshell.cn/feed', 'https://coolshell.cn', 'xml', '{\n	"channel": {\n		"tag": "channel",\n		"attr": ""\n	},\n	"item": {\n		"tag": "item",\n		"attr": ""\n	},\n	"title": {\n		"tag": "title",\n		"attr": ""\n	},\n	"description": {\n		"tag": "description",\n		"attr": ""\n	},\n	"pubDate": {\n		"tag": "pubDate",\n		"attr": ""\n	},\n	"link": {\n		"tag": "link",\n		"attr": ""\n	},\n	"category": {\n		"tag": "category",\n		"attr": ""\n	}\n}', 'Coding Your Ambition', 10000, '2019-07-14 15:06:27', 1),
	(3, '2019-07-13 22:31:28', '2019-07-14 09:41:12', 'https://www.liaoxuefeng.com/', 'https://www.liaoxuefeng.com', 'html', '{\r\n	"item": {\r\n		"tag": "//*[@id=\\"x-content\\"]/div[@class=\\"uk-margin uk-clearfix\\"]",\r\n		"attr": ""\r\n	},\r\n	"title": {\r\n		"tag": "./div[2]/h3/a",\r\n		"attr": ""\r\n	},\r\n	"description": {\r\n		"tag": "./div[2]/p",\r\n		"attr": ""\r\n	},\r\n	"pubDate": {\r\n		"tag": "--",\r\n		"attr": ""\r\n	},\r\n	"imageUrl": {\r\n		"tag": "/div[1]/a/img",\r\n		"attr": ""\r\n	},\r\n	"link": {\r\n		"tag": "./div[2]/h3/a",\r\n		"attr": "href"\r\n	},\r\n	"category": {\r\n		"tag": "--",\r\n		"attr": ""\r\n	}\r\n}', '研究互联网产品和技术，提供原创中文精品教程', 100000, '2019-07-15 13:28:21', 1),
	(4, '2019-07-13 22:31:28', '2019-07-14 11:01:15', 'http://www.iocoder.cn/Dubbo/good-collection/?vip', 'http://www.iocoder.cn', 'html', '{\r\n	"item": {\r\n		"tag": "//*[@id=\\"main\\"]/article/div/ul/li",\r\n		"attr": ""\r\n	},\r\n	"title": {\r\n		"tag": "./ul/li/a",\r\n		"attr": ""\r\n	},\r\n	"description": {\r\n		"tag": "./ul/li/a",\r\n		"attr": ""\r\n	},\r\n	"pubDate": {\r\n		"tag": "--",\r\n		"attr": ""\r\n	},\r\n	"imageUrl": {\r\n		"tag": "--",\r\n		"attr": ""\r\n	},\r\n	"link": {\r\n		"tag": "./ul/li/a",\r\n		"attr": "href"\r\n	},\r\n	"category": {\r\n		"tag": "--",\r\n		"attr": ""\r\n	}\r\n}', '「芋道源码」', 100000, '2019-07-15 13:28:50', 1),
	(7, '2019-07-13 22:31:28', '2019-07-14 09:38:26', 'https://h2pl.github.io/atom.xml', 'https://h2pl.github.io', 'xml', '{\n	"channel": {\n		"tag": "feed",\n		"attr": ""\n	},\n	"item": {\n		"tag": "entry",\n		"attr": ""\n	},\n	"title": {\n		"tag": "title",\n		"attr": ""\n	},\n	"description": {\n		"tag": "summary",\n		"attr": ""\n	},\n	"pubDate": {\n		"tag": "published",\n		"attr": ""\n	},\n	"link": {\n		"tag": "link",\n		"attr": "href"\n	},\n	"category": {\n		"tag": "category",\n		"attr": "term"\n	}\n}', 'Java后端开发之路', 10000, '2019-07-14 15:12:11', 1),
	(8, '2019-07-13 22:31:28', '2019-07-14 09:58:04', 'http://www.ifanr.com/feed', 'http://www.ifanr.com', 'xml', '{\n	"channel": {\n		"tag": "channel",\n		"attr": ""\n	},\n	"item": {\n		"tag": "item",\n		"attr": ""\n	},\n	"title": {\n		"tag": "title",\n		"attr": ""\n	},\n	"description": {\n		"tag": "description",\n		"attr": ""\n	},\n	"pubDate": {\n		"tag": "pubDate",\n		"attr": ""\n	},\n	"link": {\n		"tag": "link",\n		"attr": ""\n	},\n	"category": {\n		"tag": "category",\n		"attr": ""\n	}\n}', '爱范儿|报道未来，服务新生活引领者', 10000, '2019-07-14 15:40:09', 1),
	(9, '2019-07-13 22:31:28', '2019-07-14 09:59:39', 'http://www.adaymag.com/feed', 'http://www.adaymag.com', 'xml', '{\n	"channel": {\n		"tag": "channel",\n		"attr": ""\n	},\n	"item": {\n		"tag": "item",\n		"attr": ""\n	},\n	"title": {\n		"tag": "title",\n		"attr": ""\n	},\n	"description": {\n		"tag": "description",\n		"attr": ""\n	},\n	"pubDate": {\n		"tag": "pubDate",\n		"attr": ""\n	},\n	"link": {\n		"tag": "link",\n		"attr": ""\n	},\n	"category": {\n		"tag": "category",\n		"attr": ""\n	}\n}', '時尚生活雜誌', 10000, '2019-07-14 15:40:06', 1);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
