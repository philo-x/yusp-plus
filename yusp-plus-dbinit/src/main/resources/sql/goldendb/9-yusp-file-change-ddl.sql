ALTER TABLE `admin_sm_richedit_file_info`
MODIFY COLUMN `EXT_NAME`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件扩展名' AFTER `FILE_SIZE`;

ALTER TABLE `admin_sm_richedit_file_info`
MODIFY COLUMN `FILE_PATH`  varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件存储路径 ' AFTER `FILE_NAME`;
