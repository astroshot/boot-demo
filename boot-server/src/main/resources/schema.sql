CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(64) NOT NULL DEFAULT '' COMMENT '邮箱',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '手机号',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态0|1|9:初始添加|正常|注销',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;