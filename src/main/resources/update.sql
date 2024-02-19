ALTER TABLE `order_tb`
ADD COLUMN `refund_no`  varchar(20) NULL AFTER `update_time`;
ALTER TABLE `order_tb`
ADD COLUMN `refund_time`  datetime NULL AFTER `refund_no`;

---------------------------- updated

ALTER TABLE `user_qrcode_tb`
ADD COLUMN `is_random`  tinyint(1) NULL DEFAULT 1 AFTER `content`,
ADD COLUMN `change_limit`  int(11) NULL DEFAULT 99999 AFTER `is_random`;

CREATE TABLE `wx_qrcode_tb` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`status`  smallint(2) NOT NULL DEFAULT 1 ,
`user_id`  bigint NOT NULL ,
`qrcode_id`  bigint NOT NULL ,
`scan_times`  int NOT NULL DEFAULT 0 ,
`add_times`  int NOT NULL DEFAULT 0 ,
`create_time`  datetime NOT NULL ,
`update_time`  datetime NULL ,
`title`  varchar(255) NULL ,
`description`  varchar(255) NULL ,
`file_url`  varchar(255) NULL ,
PRIMARY KEY (`id`),
INDEX `userId` (`user_id`) USING BTREE ,
INDEX `qrcodeId` (`qrcode_id`) USING BTREE
)
;

ALTER TABLE `wx_qrcode_tb`
ADD COLUMN `change_limit`  int NOT NULL DEFAULT 9999 AFTER `qrcode_id`;

CREATE TABLE `wxqrcode_history_tb` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`ip`  varchar(128) NOT NULL ,
`qrcode_id`  bigint NOT NULL ,
`wxqrcode_id`  bigint NOT NULL ,
`create_time`  datetime NOT NULL ,
PRIMARY KEY (`id`),
INDEX `qrcodeId` (`qrcode_id`) USING BTREE
)
;
---------------------------- updated


