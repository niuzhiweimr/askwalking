/*
 Navicat Premium Data Transfer

 Source Server         : masget-test
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 192.168.33.192:3306
 Source Schema         : gateway

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 04/07/2020 12:13:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gw_configure_api
-- ----------------------------
DROP TABLE IF EXISTS `gw_configure_api`;
CREATE TABLE `gw_configure_api` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `application_name` varchar(255) NOT NULL COMMENT '应用名称',
  `api_type` varchar(10) NOT NULL COMMENT '可选：API:(外部接口),ADMIN:(管理后台接口),SAAS：(开放平台接口)',
  `request_uri` varchar(100) NOT NULL COMMENT '通过URI映射不同的dubbo service此不可重复',
  `request_method` varchar(10) NOT NULL DEFAULT 'GET' COMMENT '验证是否是正常请求并通过不同请求获取参数',
  `api_interface` varchar(100) NOT NULL COMMENT 'service类地址:com.awservice.openapi.service.TestService',
  `api_version` varchar(5) DEFAULT '1.0.0' COMMENT '接口版本默认 1.0.0',
  `api_request_class` varchar(100) NOT NULL COMMENT '请求对象:com.awservice.account.AddAccountRequest',
  `api_method` varchar(100) NOT NULL COMMENT '方法名:test',
  `api_group` varchar(255) DEFAULT NULL COMMENT '分组，指定dubbo分组通过不同分组区分调用',
  `api_name` varchar(100) DEFAULT NULL COMMENT '接口名称：描述字段',
  `api_async` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否异步调用： 0：同步 1:异步',
  `api_reload` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Api是否支持重新加载 0：不支持 1：支持',
  `mock_response` varchar(5000) DEFAULT NULL COMMENT '报文，当status为0时直接响应报文',
  `system_guard` int(2) NOT NULL DEFAULT '1' COMMENT '是否开启系统保护 0：开启 1：不开启 默认不开启',
  `flow_control_rule_type` int(2) NOT NULL DEFAULT '2' COMMENT '限流类型：1：系统限流目前不做 2：api限流',
  `qps` float(10,5) NOT NULL DEFAULT '-1.00000' COMMENT '系统qps,每秒请求数',
  `avg_rt` bigint(20) NOT NULL DEFAULT '-1' COMMENT '平均响应时间,ms为单位',
  `max_thread` bigint(10) NOT NULL DEFAULT '-1' COMMENT '入口流量的最大并发数',
  `sign_check` int(2) NOT NULL DEFAULT '0' COMMENT '0:不校验，1：校验',
  `status` int(4) NOT NULL DEFAULT '2' COMMENT '0：mock_response报文，1：待发布，2：已发布',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-未删除,1-已删除',
  `operator` varchar(64) DEFAULT '0' COMMENT '操作人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网关api配置';

-- ----------------------------
-- Table structure for gw_saas_config
-- ----------------------------
DROP TABLE IF EXISTS `gw_saas_config`;
CREATE TABLE `gw_saas_config` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `open_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接入商授权id',
  `open_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接入商名称',
  `private_key` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内部私钥',
  `public_key` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内部公钥',
  `outer_public_key` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '外部公钥',
  `status` tinyint(2) DEFAULT '1' COMMENT '接入商状态，1-准入/2-禁止',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-未删除,1-已删除',
  `operator` varchar(64) CHARACTER SET utf8 DEFAULT '0' COMMENT '操作人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='网关saas秘钥配置';

-- ----------------------------
-- Table structure for gw_saas_resource
-- ----------------------------
DROP TABLE IF EXISTS `gw_saas_resource`;
CREATE TABLE `gw_saas_resource` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `open_id` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '商户id',
  `request_uri` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '通过URI映射不同的dubbo service此不可重复',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `status` tinyint(2) DEFAULT '1' COMMENT '资源接入状态，1-准入/2-禁止',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-未删除,1-已删除',
  `operator` varchar(64) CHARACTER SET utf8 DEFAULT '0' COMMENT '操作人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='网关saas资源配置';

-- ----------------------------
-- Table structure for gw_user
-- ----------------------------
DROP TABLE IF EXISTS `gw_user`;
CREATE TABLE `gw_user` (
  `id` int(11) NOT NULL,
  `nick_name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `user_name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `status` int(4) NOT NULL DEFAULT '2' COMMENT '用户状态：0:禁用 1：启用',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-未删除,1-已删除',
  `operator` varchar(64) CHARACTER SET utf8 DEFAULT '0' COMMENT '操作人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
