/*
 Navicat Premium Data Transfer

 Source Server         : demo
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : weblog

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 02/08/2023 15:56:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '访问量',
  `comment_count` int(0) NULL DEFAULT 0 COMMENT '评论数',
  `like_count` int(0) NULL DEFAULT 0 COMMENT '点赞数',
  `is_comment` tinyint(1) NULL DEFAULT 1 COMMENT '是否允许评论（0不允许，1允许）',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0下架,1编辑中,2审核中,3已发布,4审核未通过)',
  `order_by` tinyint(1) NULL DEFAULT 0 COMMENT '排序值',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '摘要',
  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缩略图',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 1, '测试文章2', 'test111\n\n', 4, 1, 0, 1, 3, 10, 'test111\n\n', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '2023-07-26 12:03:47', '1', 'null', '2023-08-02 15:00:00');
INSERT INTO `article` VALUES (2, 1, '测试文章1', '<p data-v-md-line=\"1\">test</p>\n', 5, 0, 0, 1, 3, 10, 'test\n\n', '', '2023-07-26 13:16:46', '1', 'null', '2023-08-02 15:00:00');
INSERT INTO `article` VALUES (3, 1, '留言板', '<p data-v-md-line=\"1\">一些优秀的留言</p>\n', 4, 6, 3, 1, 1, 10, '一些优秀的留言', NULL, '2023-07-27 19:09:52', '1', 'null', '2023-08-02 15:00:00');
INSERT INTO `article` VALUES (4, 3, '测试博客', 'cetset1', 2, 0, 1, 1, 3, 10, 'cetset1', '', '2023-07-31 21:22:28', '3', 'null', '2023-08-02 15:00:00');
INSERT INTO `article` VALUES (5, 3, '测试文章啊', '啊倒萨', 0, 0, 0, 1, 0, 10, '啊倒萨', 'http://kyou:9000/kkk/b7ec79e388de4757a6e6a6c061fe771a.jpg', '2023-07-31 21:31:29', '3', '1', '2023-07-31 22:10:07');

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `aid` bigint(0) NULL DEFAULT NULL COMMENT '文章id',
  `cid` bigint(0) NULL DEFAULT NULL COMMENT '分类id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_category
-- ----------------------------
INSERT INTO `article_category` VALUES (25, 1, 1);
INSERT INTO `article_category` VALUES (26, 1, 6);
INSERT INTO `article_category` VALUES (27, 1, 7);
INSERT INTO `article_category` VALUES (28, 1, 8);
INSERT INTO `article_category` VALUES (29, 1, 9);
INSERT INTO `article_category` VALUES (30, 1, 10);
INSERT INTO `article_category` VALUES (31, 1, 11);
INSERT INTO `article_category` VALUES (32, 1, 2);
INSERT INTO `article_category` VALUES (33, 3, 13);
INSERT INTO `article_category` VALUES (34, 3, 12);

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `aid` bigint(0) NULL DEFAULT NULL COMMENT '文章id',
  `tid` bigint(0) NULL DEFAULT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_tag
-- ----------------------------
INSERT INTO `article_tag` VALUES (6, 1, 2);
INSERT INTO `article_tag` VALUES (7, 1, 3);
INSERT INTO `article_tag` VALUES (8, 1, 5);
INSERT INTO `article_tag` VALUES (9, 3, 9);
INSERT INTO `article_tag` VALUES (10, 3, 10);
INSERT INTO `article_tag` VALUES (11, 5, 10);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `pid` bigint(0) NULL DEFAULT NULL COMMENT '父分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 0, 'java', NULL);
INSERT INTO `category` VALUES (2, 0, 'python', NULL);
INSERT INTO `category` VALUES (3, 0, 'redis', 'redis缓存');
INSERT INTO `category` VALUES (4, 0, '多线程', NULL);
INSERT INTO `category` VALUES (5, 0, '资源分享', NULL);
INSERT INTO `category` VALUES (6, 1, '设计模式', NULL);
INSERT INTO `category` VALUES (7, 1, 'java基础', NULL);
INSERT INTO `category` VALUES (8, 1, 'java进阶', NULL);
INSERT INTO `category` VALUES (9, 8, 'java网络编程', NULL);
INSERT INTO `category` VALUES (10, 1, 'javaWeb', NULL);
INSERT INTO `category` VALUES (11, 10, 'jsp', '模板引擎');
INSERT INTO `category` VALUES (12, 0, '人生感悟', NULL);
INSERT INTO `category` VALUES (13, 0, '生活琐事', NULL);

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '上级评论ID',
  `pname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级评论人昵称',
  `article_id` bigint(0) NULL DEFAULT NULL COMMENT '文章id',
  `uid` bigint(0) NULL DEFAULT NULL COMMENT '评论人id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论人昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论人头像',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器地址',
  `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论人IP',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '评论时间',
  `likes` bigint(0) NULL DEFAULT 0 COMMENT '评论点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, NULL, NULL, 3, 1, 'kyou', 'https://img0.baidu.com/it/u=3927518935,2597159060&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1689181200&t=67138d7b6d58ad1b882a8bf595ca14c6', '测试评论', 'Google Chrome', '0:0:0:0:0:0:0:1', '2023-07-27 21:35:06', 1);
INSERT INTO `comments` VALUES (2, 1, 'admin', 3, 1, 'kyou', 'https://img0.baidu.com/it/u=3927518935,2597159060&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1689181200&t=67138d7b6d58ad1b882a8bf595ca14c6', '回复 <span style=\"color: var(--u-color-success-dark-2);\">@admin:</span> 111', 'Google Chrome', '0:0:0:0:0:0:0:1', '2023-07-27 22:49:46', 0);
INSERT INTO `comments` VALUES (3, NULL, NULL, 3, 1, 'kyou', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '123', 'Google Chrome', '0:0:0:0:0:0:0:1', '2023-07-28 00:11:19', 0);
INSERT INTO `comments` VALUES (4, 3, 'kyou', 3, 1, 'kyou', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '回复 <span style=\"color: var(--u-color-success-dark-2);\">@kyou:</span> 测试', 'Chromium', '0:0:0:0:0:0:0:1', '2023-07-28 17:35:22', 0);
INSERT INTO `comments` VALUES (5, 3, 'kyou', 3, 1, 'kyou', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '回复 <span style=\"color: var(--u-color-success-dark-2);\">@kyou:</span> 测试2', 'Chromium', '0:0:0:0:0:0:0:1', '2023-07-28 17:35:34', 0);
INSERT INTO `comments` VALUES (8, 1, 'kyou', 3, 3, NULL, 'https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100', '回复 <span style=\"color: var(--u-color-success-dark-2);\">@kyou:</span> 222', 'Chromium', '0:0:0:0:0:0:0:1', '2023-07-31 20:37:20', 0);
INSERT INTO `comments` VALUES (9, NULL, NULL, 1, 3, '我的新名字', 'https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100', '666', 'Chromium', '0:0:0:0:0:0:0:1', '2023-07-31 22:38:59', 0);
INSERT INTO `comments` VALUES (10, NULL, NULL, 2, 1, 'kyou', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '6', 'Chromium', '0:0:0:0:0:0:0:1', '2023-08-02 15:32:29', 0);

-- ----------------------------
-- Table structure for links
-- ----------------------------
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of links
-- ----------------------------
INSERT INTO `links` VALUES (1, 'https://github.com/', '跳转github主页');
INSERT INTO `links` VALUES (2, 'https://blog.csdn.net/', '跳转csdn');
INSERT INTO `links` VALUES (3, 'https://element-plus.org/zh-CN/component', '跳转element-plus');
INSERT INTO `links` VALUES (4, 'https://cloud.tencent.com/product/', '前往腾讯云');
INSERT INTO `links` VALUES (5, 'https://mp.weixin.qq.com/', '微信公众平台');
INSERT INTO `links` VALUES (6, 'http://doc.ruoyi.vip/', '若依官网');
INSERT INTO `links` VALUES (7, 'https://mybatis.org/mybatis-3/zh/', 'mybatis官网');
INSERT INTO `links` VALUES (8, 'http://www.jsons.cn/jsoncheck/', 'json在线工具');
INSERT INTO `links` VALUES (9, 'https://www.runoob.com/', '菜鸟教程');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '菜单面板', 0, 'menuBoard', '', 'M', '0', '0', 'menuBoard:all:list', 'Menu', '1', '2023-07-12 08:39:40', '1', '2023-07-12 08:39:46');
INSERT INTO `menu` VALUES (2, '系统模块', 0, 'os', '', 'M', '0', '0', 'os:all:list', 'HomeFilled', '1', '2023-07-12 11:05:28', '1', '2023-07-12 11:05:32');
INSERT INTO `menu` VALUES (3, '用户模块', 0, 'user', '', 'M', '0', '0', 'user:all:list', 'Avatar', '1', '2023-07-12 11:06:05', '1', '2023-07-12 11:06:09');
INSERT INTO `menu` VALUES (4, '设置', 0, 'setting', '', 'M', '0', '0', 'setting:all:list', 'Tools', '1', '2023-07-12 11:06:53', '1', '2023-07-12 11:06:57');
INSERT INTO `menu` VALUES (5, 'Dashboard', 1, 'dashboard', 'sys/dashboard/index', 'C', '0', '0', 'menuBoard:dashboard:list', NULL, '1', '2023-07-12 11:08:52', '1', '2023-07-12 11:08:55');
INSERT INTO `menu` VALUES (6, '发布博客', 1, 'publish', 'sys/dashboard/publish', 'C', '0', '0', 'menuBoard:publish:list', NULL, '1', '2023-07-12 11:12:38', '1', '2023-07-12 11:12:41');
INSERT INTO `menu` VALUES (7, '博客管理', 2, 'blog', 'sys/os/index', 'C', '0', '0', 'os:blog:list', NULL, '1', '2023-07-12 11:13:22', '1', '2023-07-12 11:13:27');
INSERT INTO `menu` VALUES (8, '评论管理', 2, 'comments', 'sys/os/comments', 'C', '0', '0', 'os:comments:list', NULL, '1', '2023-07-12 11:13:43', '1', '2023-07-12 11:13:47');
INSERT INTO `menu` VALUES (9, '分类管理', 2, 'category', 'sys/os/category', 'C', '0', '0', 'os:category:list', NULL, '1', '2023-07-12 11:14:02', '1', '2023-07-12 11:14:05');
INSERT INTO `menu` VALUES (10, '标签管理', 2, 'tags', 'sys/os/tags', 'C', '0', '0', 'os:tags:list', NULL, '1', '2023-07-12 11:14:27', '1', '2023-07-12 11:14:31');
INSERT INTO `menu` VALUES (11, '友情链接', 2, 'links', 'sys/os/links', 'C', '0', '0', 'os:links:list', NULL, '1', '2023-07-12 11:14:44', '1', '2023-07-12 11:14:48');
INSERT INTO `menu` VALUES (12, '用户列表', 3, 'useList', 'sys/user/index', 'C', '0', '0', 'user:userList:list', NULL, '1', '2023-07-12 11:15:17', '1', '2023-07-21 10:35:35');
INSERT INTO `menu` VALUES (14, '个人信息', 3, 'userInfo', 'sys/user/info', 'C', '0', '0', 'user:userInfo:list', NULL, '1', '2023-07-12 11:15:55', '1', '2023-07-12 11:15:59');
INSERT INTO `menu` VALUES (15, '菜单管理', 4, 'menu', 'sys/setting/menu', 'C', '0', '0', 'setting:menu:list', NULL, '1', '2023-07-12 11:16:25', '1', '2023-07-12 11:16:30');
INSERT INTO `menu` VALUES (16, '权限分配', 4, 'auth', 'sys/setting/auth', 'C', '0', '0', 'setting:auth:list', NULL, '1', '2023-07-12 11:16:46', '1', '2023-07-12 11:16:49');
INSERT INTO `menu` VALUES (17, '角色管理', 4, 'role', 'sys/setting/role', 'C', '0', '0', 'setting:role:list', NULL, '1', '2023-07-12 11:17:04', '1', '2023-07-12 11:17:07');
INSERT INTO `menu` VALUES (18, '日志管理', 4, 'log', 'sys/setting/log', 'C', '0', '0', 'setting:log:list', NULL, '1', '2023-07-12 11:17:26', '1', '2023-07-12 11:17:28');
INSERT INTO `menu` VALUES (20, '前往博客', 1, 'dashboard', 'sys/dashboard/forward', 'C', '0', '0', 'menuBoard:dashboard:list', 'Star', '1', '2023-07-13 19:28:32', '1', '2023-07-13 19:28:38');
INSERT INTO `menu` VALUES (21, '查询仪表盘', 5, '', '', 'F', '0', '0', 'menuBoard:dashboard:select', 'HelpFilled', '1', '2023-07-18 10:43:44', '1', '2023-07-18 10:43:44');
INSERT INTO `menu` VALUES (22, '新增仪表盘', 5, '', '', 'F', '0', '0', 'menuBoard:dashboard:add', 'Tools', '1', '2023-07-18 10:45:39', '1', '2023-07-18 10:45:39');
INSERT INTO `menu` VALUES (23, '删除仪表盘', 5, '', '', 'F', '0', '0', 'menuBoard:dashboard:remove', 'Avatar', '1', '2023-07-18 10:46:29', '1', '2023-07-18 10:46:29');
INSERT INTO `menu` VALUES (24, '修改仪表盘', 5, '', '', 'F', '0', '0', 'menuBoard:dashboard:edit', 'Calendar', '1', '2023-07-18 10:47:12', '1', '2023-07-18 10:47:12');
INSERT INTO `menu` VALUES (25, '新增用户列表', 12, '', '', 'F', '0', '0', 'user:userList:add', 'Calendar', '1', '2023-07-19 23:06:04', '1', '2023-07-19 23:07:21');
INSERT INTO `menu` VALUES (26, '修改用户列表', 12, '', '', 'F', '0', '0', 'user:userList:edit', 'Avatar', '1', '2023-07-19 23:06:38', '1', '2023-07-19 23:12:07');
INSERT INTO `menu` VALUES (27, '删除用户列表', 12, '', '', 'F', '0', '0', 'user:userList:remove', 'Calendar', '1', '2023-07-19 23:07:04', '1', '2023-07-19 23:07:04');
INSERT INTO `menu` VALUES (28, '查询用户列表', 12, '', '', 'F', '0', '0', 'user:userList:select', 'Calendar', '1', '2023-07-19 23:13:41', '1', '2023-07-21 10:31:57');
INSERT INTO `menu` VALUES (29, '查询个人信息', 14, '', '', 'F', '0', '0', 'user:userInfo:select', 'Calendar', '1', '2023-07-19 23:14:23', '1', '2023-07-19 23:14:23');
INSERT INTO `menu` VALUES (30, '修改个人信息', 14, '', '', 'F', '0', '0', 'user:userInfo:edit', 'Avatar', '1', '2023-07-19 23:14:58', '1', '2023-07-19 23:14:58');
INSERT INTO `menu` VALUES (31, '删除个人信息', 14, '', '', 'F', '0', '0', 'user:userInfo:remove', 'Avatar', '1', '2023-07-19 23:15:21', '1', '2023-07-19 23:15:21');
INSERT INTO `menu` VALUES (32, '新增个人信息', 14, '', '', 'F', '0', '0', 'user:userInfo:add', 'Avatar', '1', '2023-07-19 23:15:45', '1', '2023-07-19 23:15:45');
INSERT INTO `menu` VALUES (33, '新增角色', 17, '', '', 'F', '0', '0', 'setting:role:add', 'Avatar', '1', '2023-07-19 23:58:45', '1', '2023-07-19 23:58:45');
INSERT INTO `menu` VALUES (34, '删除角色', 17, '', '', 'F', '0', '0', 'setting:role:remove', 'Menu', '1', '2023-07-19 23:59:56', '1', '2023-07-19 23:59:56');
INSERT INTO `menu` VALUES (35, '查询角色', 17, '', '', 'F', '0', '0', 'setting:role:select', 'Tools', '1', '2023-07-20 00:00:59', '1', '2023-07-20 00:00:59');
INSERT INTO `menu` VALUES (36, '修改角色', 17, '', '', 'F', '0', '0', 'setting:role:edit', 'Menu', '1', '2023-07-20 00:01:41', '1', '2023-07-20 00:01:41');
INSERT INTO `menu` VALUES (37, '查询菜单', 15, '', '', 'F', '0', '0', 'setting:menu:select', 'Menu', '1', '2023-07-20 00:04:59', '1', '2023-07-20 00:04:59');
INSERT INTO `menu` VALUES (38, '修改菜单', 15, '', '', 'F', '0', '0', 'setting:menu:edit', 'Menu', '1', '2023-07-20 00:05:46', '1', '2023-07-20 00:05:46');
INSERT INTO `menu` VALUES (39, '删除菜单', 15, '', '', 'F', '0', '0', 'setting:menu:remove', 'Calendar', '1', '2023-07-20 00:06:57', '1', '2023-07-20 00:06:57');
INSERT INTO `menu` VALUES (40, '新增菜单', 15, '', '', 'F', '0', '0', 'setting:menu:add', 'Tools', '1', '2023-07-20 00:07:15', '1', '2023-07-20 00:07:15');
INSERT INTO `menu` VALUES (41, '新增权限', 16, '', '', 'F', '0', '0', 'setting:auth:add', 'Male', '1', '2023-07-21 00:40:53', '1', '2023-07-21 00:40:53');
INSERT INTO `menu` VALUES (42, '查询权限', 16, '', '', 'F', '0', '0', 'setting:auth:select', 'Comment', '1', '2023-07-21 00:41:27', '1', '2023-07-21 00:41:27');
INSERT INTO `menu` VALUES (43, '删除权限', 16, '', '', 'F', '0', '0', 'setting:auth:remove', 'Menu', '1', '2023-07-21 00:41:53', '1', '2023-07-21 00:41:53');
INSERT INTO `menu` VALUES (44, '修改权限', 16, '', '', 'F', '0', '0', 'setting:auth:edit', 'User', '1', '2023-07-21 00:42:37', '1', '2023-07-21 00:42:37');
INSERT INTO `menu` VALUES (45, '新增标签', 10, '', '', 'F', '0', '0', 'os:tags:add', 'Avatar', '1', '2023-07-21 11:12:52', '1', '2023-07-21 11:12:52');
INSERT INTO `menu` VALUES (46, '删除标签', 10, '', '', 'F', '0', '0', 'os:tags:remove', 'Avatar', '1', '2023-07-21 11:13:31', '1', '2023-07-21 11:13:31');
INSERT INTO `menu` VALUES (47, '查询标签', 10, '', '', 'F', '0', '0', 'os:tags:select', 'Calendar', '1', '2023-07-21 11:13:55', '1', '2023-07-21 11:13:55');
INSERT INTO `menu` VALUES (48, '修改标签', 10, '', '', 'F', '0', '0', 'os:tags:edit', 'Avatar', '1', '2023-07-21 11:14:18', '1', '2023-07-21 11:14:18');
INSERT INTO `menu` VALUES (49, '新增分类', 9, '', '', 'F', '0', '0', 'os:category:add', 'Avatar', '1', '2023-07-25 09:20:59', '1', '2023-07-25 09:20:59');
INSERT INTO `menu` VALUES (50, '删除分类', 9, '', '', 'F', '0', '0', 'os:category:remove', 'Share', '1', '2023-07-25 09:21:20', '1', '2023-07-25 09:21:20');
INSERT INTO `menu` VALUES (51, '修改分类', 9, '', '', 'F', '0', '0', 'os:category:edit', 'Sort', '1', '2023-07-25 09:21:44', '1', '2023-07-25 09:21:44');
INSERT INTO `menu` VALUES (52, '查询分类', 9, '', '', 'F', '0', '0', 'os:category:select', 'Discount', '1', '2023-07-25 09:22:10', '1', '2023-07-25 09:22:10');
INSERT INTO `menu` VALUES (54, '新增发布', 6, '', '', 'F', '0', '0', 'menuBoard:publish:add', 'Avatar', '1', '2023-07-25 09:24:39', '1', '2023-07-25 09:24:39');
INSERT INTO `menu` VALUES (55, '修改发布', 6, '', '', 'F', '0', '0', 'menuBoard:publish:edit', 'Calendar', '1', '2023-07-25 09:25:06', '1', '2023-07-25 09:25:06');
INSERT INTO `menu` VALUES (56, '日志删除', 18, '', '', 'F', '0', '0', 'setting:log:remove', 'Male', '1', '2023-07-31 10:12:17', '1', '2023-07-31 10:12:17');
INSERT INTO `menu` VALUES (57, '修改文章', 7, '', '', 'F', '0', '0', 'os:blog:edit', 'Pouring', '1', '2023-07-31 18:31:20', '1', '2023-07-31 18:31:20');
INSERT INTO `menu` VALUES (58, '新增文章', 7, '', '', 'F', '0', '0', 'os:blog:add', 'Coffee', '1', '2023-07-31 18:31:49', '1', '2023-07-31 18:31:49');
INSERT INTO `menu` VALUES (59, '删除文章', 7, '', '', 'F', '0', '0', 'os:blog:remove', 'Apple', '1', '2023-07-31 18:32:56', '1', '2023-07-31 18:32:56');

-- ----------------------------
-- Table structure for platform_type
-- ----------------------------
DROP TABLE IF EXISTS `platform_type`;
CREATE TABLE `platform_type`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编号',
  `name` varchar(524) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `level_index` int(0) NULL DEFAULT NULL COMMENT '等级',
  `sort_index` int(0) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platform_type
-- ----------------------------
INSERT INTO `platform_type` VALUES ('1', '110', 'zs', 1, 1, '0', NULL, NULL, NULL, NULL);
INSERT INTO `platform_type` VALUES ('2', '111', 'li', 2, 1, '1', NULL, NULL, NULL, NULL);
INSERT INTO `platform_type` VALUES ('3', '112', 'ww', 3, 2, '2', NULL, NULL, NULL, NULL);
INSERT INTO `platform_type` VALUES ('4', '113', 'a', 1, 1, '0', NULL, NULL, NULL, NULL);
INSERT INTO `platform_type` VALUES ('5', '114', 'b', 2, 1, '4', NULL, NULL, NULL, NULL);
INSERT INTO `platform_type` VALUES ('6', '115', 'c', 3, 1, '5', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `key_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 'admin', '0', '0', '0', '2023-07-13 23:22:51', '0', '2023-07-13 23:22:57');
INSERT INTO `role` VALUES (2, '作者', 'author', '0', '0', '0', '2023-07-13 23:24:11', '0', '2023-07-13 23:24:16');
INSERT INTO `role` VALUES (3, '游客', 'tourist', '0', '0', '0', '2023-07-13 23:24:57', '0', '2023-07-13 23:25:04');
INSERT INTO `role` VALUES (4, '审核员', 'checker', '0', '0', '0', '2023-07-13 23:26:08', '0', '2023-07-13 23:26:14');
INSERT INTO `role` VALUES (5, '普通用户', 'common', '0', '0', '0', '2023-07-13 23:27:30', '1', '2023-07-18 09:54:52');
INSERT INTO `role` VALUES (6, '测试角色', 'test', '0', '0', '1', '2023-07-18 09:55:41', '1', '2023-07-20 08:48:29');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `rid` bigint(0) NOT NULL COMMENT '角色id',
  `mid` bigint(0) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 268 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 6, 1);
INSERT INTO `role_menu` VALUES (2, 6, 5);
INSERT INTO `role_menu` VALUES (3, 6, 21);
INSERT INTO `role_menu` VALUES (4, 6, 22);
INSERT INTO `role_menu` VALUES (5, 6, 23);
INSERT INTO `role_menu` VALUES (6, 6, 24);
INSERT INTO `role_menu` VALUES (7, 6, 6);
INSERT INTO `role_menu` VALUES (8, 6, 19);
INSERT INTO `role_menu` VALUES (9, 6, 20);
INSERT INTO `role_menu` VALUES (94, 2, 1);
INSERT INTO `role_menu` VALUES (95, 2, 5);
INSERT INTO `role_menu` VALUES (96, 2, 21);
INSERT INTO `role_menu` VALUES (97, 2, 6);
INSERT INTO `role_menu` VALUES (98, 2, 54);
INSERT INTO `role_menu` VALUES (99, 2, 55);
INSERT INTO `role_menu` VALUES (100, 2, 20);
INSERT INTO `role_menu` VALUES (101, 2, 2);
INSERT INTO `role_menu` VALUES (102, 2, 7);
INSERT INTO `role_menu` VALUES (103, 2, 57);
INSERT INTO `role_menu` VALUES (104, 2, 58);
INSERT INTO `role_menu` VALUES (105, 2, 8);
INSERT INTO `role_menu` VALUES (106, 2, 9);
INSERT INTO `role_menu` VALUES (107, 2, 49);
INSERT INTO `role_menu` VALUES (108, 2, 52);
INSERT INTO `role_menu` VALUES (109, 2, 10);
INSERT INTO `role_menu` VALUES (110, 2, 45);
INSERT INTO `role_menu` VALUES (111, 2, 47);
INSERT INTO `role_menu` VALUES (112, 2, 48);
INSERT INTO `role_menu` VALUES (113, 2, 11);
INSERT INTO `role_menu` VALUES (114, 2, 14);
INSERT INTO `role_menu` VALUES (115, 2, 29);
INSERT INTO `role_menu` VALUES (116, 2, 30);
INSERT INTO `role_menu` VALUES (227, 5, 1);
INSERT INTO `role_menu` VALUES (228, 5, 5);
INSERT INTO `role_menu` VALUES (229, 5, 21);
INSERT INTO `role_menu` VALUES (230, 5, 6);
INSERT INTO `role_menu` VALUES (231, 5, 54);
INSERT INTO `role_menu` VALUES (232, 5, 55);
INSERT INTO `role_menu` VALUES (233, 5, 20);
INSERT INTO `role_menu` VALUES (234, 5, 2);
INSERT INTO `role_menu` VALUES (235, 5, 7);
INSERT INTO `role_menu` VALUES (236, 5, 57);
INSERT INTO `role_menu` VALUES (237, 5, 58);
INSERT INTO `role_menu` VALUES (238, 5, 59);
INSERT INTO `role_menu` VALUES (239, 5, 8);
INSERT INTO `role_menu` VALUES (240, 5, 10);
INSERT INTO `role_menu` VALUES (241, 5, 47);
INSERT INTO `role_menu` VALUES (242, 5, 3);
INSERT INTO `role_menu` VALUES (243, 5, 14);
INSERT INTO `role_menu` VALUES (244, 5, 29);
INSERT INTO `role_menu` VALUES (245, 5, 30);
INSERT INTO `role_menu` VALUES (246, 4, 1);
INSERT INTO `role_menu` VALUES (247, 4, 5);
INSERT INTO `role_menu` VALUES (248, 4, 21);
INSERT INTO `role_menu` VALUES (249, 4, 6);
INSERT INTO `role_menu` VALUES (250, 4, 54);
INSERT INTO `role_menu` VALUES (251, 4, 55);
INSERT INTO `role_menu` VALUES (252, 4, 20);
INSERT INTO `role_menu` VALUES (253, 4, 2);
INSERT INTO `role_menu` VALUES (254, 4, 7);
INSERT INTO `role_menu` VALUES (255, 4, 57);
INSERT INTO `role_menu` VALUES (256, 4, 58);
INSERT INTO `role_menu` VALUES (257, 4, 8);
INSERT INTO `role_menu` VALUES (258, 4, 9);
INSERT INTO `role_menu` VALUES (259, 4, 52);
INSERT INTO `role_menu` VALUES (260, 4, 10);
INSERT INTO `role_menu` VALUES (261, 4, 45);
INSERT INTO `role_menu` VALUES (262, 4, 47);
INSERT INTO `role_menu` VALUES (263, 4, 48);
INSERT INTO `role_menu` VALUES (264, 4, 11);
INSERT INTO `role_menu` VALUES (265, 4, 3);
INSERT INTO `role_menu` VALUES (266, 4, 14);
INSERT INTO `role_menu` VALUES (267, 4, 29);
INSERT INTO `role_menu` VALUES (268, 4, 30);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(0) NOT NULL COMMENT '标识',
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 操作人',
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `run_time` int(0) NULL DEFAULT NULL COMMENT '执行时间(秒)',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的方法',
  `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应结果',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1685960997169836034, 'admin', '/sys/menu/assignPerms/5', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[5, [1, 5, 21, 6, 20, 2, 9, 52, 10, 47, 3, 14, 29, 30, 4, 15, 37]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:28:43');
INSERT INTO `sys_log` VALUES (1685963513672544258, 'admin', '/sys/menu', 'INSERT', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.addMenu(Menu)', '[Menu(id=57, name=修改文章, parentId=7, path=, component=, type=F, visible=0, status=0, perms=os:blog:edit, icon=Pouring, createBy=1, createTime=2023-07-31T18:31:19.520, updateBy=1, updateTime=2023-07-31T18:31:19.520)]', '{\"data\":\"新增菜单成功\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:28:46');
INSERT INTO `sys_log` VALUES (1685963513672544259, 'admin', '/sys/menu', 'INSERT', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.addMenu(Menu)', '[Menu(id=58, name=新增文章, parentId=7, path=, component=, type=F, visible=0, status=0, perms=os:blog:add, icon=Coffee, createBy=1, createTime=2023-07-31T18:31:48.584, updateBy=1, updateTime=2023-07-31T18:31:48.584)]', '{\"data\":\"新增菜单成功\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:28:49');
INSERT INTO `sys_log` VALUES (1685963513672544260, 'admin', '/sys/menu', 'INSERT', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.addMenu(Menu)', '[Menu(id=59, name=删除文章, parentId=7, path=, component=, type=F, visible=0, status=0, perms=os:blog:remove, icon=Apple, createBy=1, createTime=2023-07-31T18:32:56.463, updateBy=1, updateTime=2023-07-31T18:32:56.463)]', '{\"data\":\"新增菜单成功\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:28:52');
INSERT INTO `sys_log` VALUES (1685991196108324866, 'admin', '/sys/menu/assignPerms/4', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[4, [1, 5, 21, 6, 54, 2, 7, 57, 8, 9, 52, 10, 45, 47, 14, 29, 30, 4, 15, 37]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:28:55');
INSERT INTO `sys_log` VALUES (1685991196108324867, 'admin', '/sys/menu/assignPerms/2', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[2, [1, 5, 21, 6, 54, 55, 20, 2, 7, 57, 58, 8, 9, 49, 52, 10, 45, 47, 48, 11, 14, 29, 30]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-01 00:28:58');
INSERT INTO `sys_log` VALUES (1685991196108324868, 'admin', '/sys/menu/assignPerms/4', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[4, [1, 5, 21, 6, 54, 2, 7, 57, 8, 9, 52, 10, 45, 47, 48, 11, 14, 29, 30]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:29:04');
INSERT INTO `sys_log` VALUES (1686001262496604162, 'test1', '/sys/user', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.UserController.saveUser(UserDto)', '[UserDto(id=3, email=asx123@123.com, username=test1, gender=1, nickname=我的新名字, headImage=https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100, remark=只是一个普普通通的测试用户罢了, delFlag=null, status=null, roles=[普通用户])]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '用户模块', '2023-08-02 00:29:07');
INSERT INTO `sys_log` VALUES (1686003779049660418, 'admin', '/sys/menu/assignPerms/5', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[5, [1, 5, 21, 6, 54, 55, 20, 2, 7, 59, 8, 9, 52, 10, 47, 3, 14, 29, 30]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:29:10');
INSERT INTO `sys_log` VALUES (1686003779049660419, 'admin', '/sys/menu/assignPerms/5', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[5, [1, 5, 21, 6, 54, 55, 20, 2, 7, 57, 58, 59, 8, 9, 52, 10, 47, 3, 14, 29, 30]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:29:12');
INSERT INTO `sys_log` VALUES (1686008812239613954, 'test1', '/sys/article/valid/4', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.ArticleController.validArticle(Long)', '[4]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '文章模块', '2023-08-02 00:29:15');
INSERT INTO `sys_log` VALUES (1686023911708844034, 'test1', '/sys/comments', 'INSERT', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.CommentsController.addComment(Map,HttpServletRequest)', '[{parentId=null, pname=null, uid=3, address=Chromium, content=666, likes=0, createTime=2023-07-31 22:38:59, articleId=1, user={username=我的新名字, avatar=https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100, level=1, homeLink=/3}}, SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@32034ca0]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '评论模块', '2023-08-02 00:29:19');
INSERT INTO `sys_log` VALUES (1686381266430648322, 'admin', '/sys/user', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.UserController.saveUser(UserDto)', '[UserDto(id=2, email=2485119372@qq.com, username=commonUser, gender=1, nickname=null, headImage=http://kyou:9000/kkk/8f471cdf6df64c9f969e9e6b19b7ed27.jpg, remark=一名普通的用户, delFlag=null, status=null, roles=[普通用户, 审核员])]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '用户模块', '2023-08-02 00:29:22');
INSERT INTO `sys_log` VALUES (1686381266430648323, 'admin', '/sys/menu/assignPerms/4', 'UPDATE', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.MenuController.assign(Long,List)', '[4, [1, 5, 21, 6, 54, 55, 20, 2, 7, 57, 58, 8, 9, 52, 10, 45, 47, 48, 11, 3, 14, 29, 30]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '菜单模块', '2023-08-02 00:29:25');
INSERT INTO `sys_log` VALUES (1686642991058919426, 'admin', '/sys/comments', 'INSERT', '0:0:0:0:0:0:0:1', 0, 'Result com.kyou.blog.background.web.CommentsController.addComment(Map,HttpServletRequest)', '[{parentId=null, pname=null, uid=1, address=Chromium, content=6, likes=0, createTime=2023-08-02 15:32:29, articleId=2, user={username=kyou, avatar=http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg, level=6, homeLink=/1}}, SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@230a0f6d]]', '{\"data\":\"\",\"msg\":\"成功\",\"code\":200}', '评论模块', '2023-08-02 15:32:29');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, 'java');
INSERT INTO `tag` VALUES (2, 'python');
INSERT INTO `tag` VALUES (3, 'c');
INSERT INTO `tag` VALUES (4, 'c++');
INSERT INTO `tag` VALUES (5, 'mysql');
INSERT INTO `tag` VALUES (6, 'redis');
INSERT INTO `tag` VALUES (7, 'rust');
INSERT INTO `tag` VALUES (8, 'mybatis');
INSERT INTO `tag` VALUES (9, 'life');
INSERT INTO `tag` VALUES (10, 'thinking');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户标识',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(62) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人说明',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '账户状态(0启用，1禁用)',
  `last_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户最后登录ip',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$RoIQHqASHRkV1yL4L3t2teDqtvaF77oD3yy8iU9.G1EL3nvDugXWK', '2967861744@qq.com', '1', 'kyou', 'http://kyou:9000/kkk/119954f26bf246ba99a6cfba6f39b7bc.jpg', '超级管理员', '0', 0, '0:0:0:0:0:0:0:1', '2023-07-14 09:51:07', '0', '1', '2023-08-01 22:14:53');
INSERT INTO `user` VALUES (2, 'commonUser', '$2a$10$dBoIVVOjY4TqqWKJr0bmoOoH0oo/TJNojqO4ExJQC94dGGFFipdsi', '2485119372@qq.com', '1', NULL, 'http://kyou:9000/kkk/8f471cdf6df64c9f969e9e6b19b7ed27.jpg', '一名普通的用户', '0', 0, '0:0:0:0:0:0:0:1', '2023-07-20 09:01:08', '1', '2', '2023-08-01 22:17:09');
INSERT INTO `user` VALUES (3, 'test1', '$2a$10$NeVEzQAJQZIThgxn7LmXpOOX0zmHfDOwtOvftlOPt5CwzjbvvXyhi', 'asx123@123.com', '1', '我的新名字', 'https://static.juzicon.com/avatars/avatar-200602130320-HMR2.jpeg?x-oss-process=image/resize,w_100', '只是一个普普通通的测试用户罢了', '0', 0, '0:0:0:0:0:0:0:1', '2023-07-20 09:20:05', '1', '3', '2023-08-01 08:58:19');
INSERT INTO `user` VALUES (4, 'test2', '$2a$10$FlwHtD7gPRp8rA9kHiGjveaAYwKooDRPHkuXvEj2aB6OST.C5olUq', 'test2@123.com', '1', '测试用户2', NULL, '测试用户2', '0', 0, '0:0:0:0:0:0:0:1', '2023-07-21 01:11:24', '1', '4', '2023-07-22 09:34:18');
INSERT INTO `user` VALUES (5, 'test3', '$2a$10$gooI8kcuSgiJLHQZ4s54e.yeo1wGIORASk5WTm89KZHzYR4Uh1i.S', 'test3@123.com', '1', '测试拥护3', NULL, '测试', '0', 0, '0:0:0:0:0:0:0:1', '2023-07-21 01:12:44', '1', '5', '2023-07-22 09:00:27');

-- ----------------------------
-- Table structure for user_comment
-- ----------------------------
DROP TABLE IF EXISTS `user_comment`;
CREATE TABLE `user_comment`  (
  `id` bigint(0) NOT NULL COMMENT '用户id',
  `level` tinyint(1) NULL DEFAULT 1 COMMENT '评论人等级(1-6)',
  `experience` int(0) NULL DEFAULT 0 COMMENT '经验',
  `home_link` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户主页',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_comment
-- ----------------------------
INSERT INTO `user_comment` VALUES (1, 6, 30000, '/userHome1');
INSERT INTO `user_comment` VALUES (2, 1, 0, '/userHome2');
INSERT INTO `user_comment` VALUES (3, 1, 330, '/userHome3');
INSERT INTO `user_comment` VALUES (4, 1, 0, '/userHome4');
INSERT INTO `user_comment` VALUES (5, 1, 0, '/userHome5');

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`  (
  `cur_id` bigint(0) NOT NULL COMMENT '用户id',
  `fans_id` bigint(0) NOT NULL COMMENT '粉丝id',
  PRIMARY KEY (`cur_id`, `fans_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
INSERT INTO `user_follow` VALUES (1, 3);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `uid` bigint(0) NOT NULL COMMENT '用户id',
  `rid` bigint(0) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (13, 1, 1);
INSERT INTO `user_role` VALUES (14, 1, 4);
INSERT INTO `user_role` VALUES (18, 4, 5);
INSERT INTO `user_role` VALUES (19, 4, 6);
INSERT INTO `user_role` VALUES (20, 5, 6);
INSERT INTO `user_role` VALUES (21, 6, 6);
INSERT INTO `user_role` VALUES (22, 3, 5);
INSERT INTO `user_role` VALUES (23, 2, 4);
INSERT INTO `user_role` VALUES (24, 2, 5);

SET FOREIGN_KEY_CHECKS = 1;
