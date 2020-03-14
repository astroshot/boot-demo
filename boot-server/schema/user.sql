CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT 'user name',
  `email` varchar(32) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户类型，1 顾客 2 卖主',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0-created 1-normal 2-deleted',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';