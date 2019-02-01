CREATE TABLE `NewTable` (
`id`  bigint(20) UNSIGNED NOT NULL COMMENT '用户id' ,
`nickname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称' ,
`password`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密文密码' ,
`salt`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '混淆盐' ,
`head`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像，云存储的ID' ,
`mobile`  int(11) NULL DEFAULT NULL COMMENT '电话号码' ,
`register_date`  datetime NULL DEFAULT NULL COMMENT '注册时间' ,
`last_login_date`  datetime NULL DEFAULT NULL COMMENT '上次登录时间' ,
`login_count`  int(11) NULL DEFAULT NULL COMMENT '登录次数' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=COMPACT
;
