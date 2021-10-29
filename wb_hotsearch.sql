/*
 Navicat Premium Data Transfer

 Source Server         : hellodkdotcom
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : hellodk.com:3307
 Source Schema         : wb_hotsearch

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 28/10/2021 17:16:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wb_hotsearch
-- ----------------------------
DROP TABLE IF EXISTS `wb_hotsearch`;
CREATE TABLE `wb_hotsearch`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'post id',
  `post_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'post time',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'if same city hot search, specify the city name',
  `post_title` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'post title',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'post content',
  `created_time` datetime NULL DEFAULT NULL COMMENT 'created time',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
