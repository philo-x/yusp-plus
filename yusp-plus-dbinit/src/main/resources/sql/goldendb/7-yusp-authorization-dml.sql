-- ----------------------------
-- 删除角色和用户交叉的权限（这些权限在初始化脚本中角色和用户都拥有）
-- ----------------------------

delete from admin_sm_auth_reco where AUTHRES_ID in
                                     (select a.AUTHRES_ID from (select AUTHRES_ID from admin_sm_auth_reco group by AUTHRES_ID having count(AUTHRES_ID) > 1 ) a) and AUTHOBJ_TYPE ='R';
-- ----------------------------
-- 把角色的所有权限给用户；
-- ----------------------------
update admin_sm_auth_reco set authobj_type='U',AUTHOBJ_ID ='40';


-- ----------------------------
-- Table structure for admin_sm_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_tenant_user_rel`;
CREATE TABLE `admin_sm_tenant_user_rel`
(
    `TENANT_USER_REL_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `USER_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
    `data_tenant_id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户编号',
    PRIMARY KEY (`TENANT_USER_REL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户用户关联表' ROW_FORMAT = Dynamic;

-- 插入admin和系统默认租户的关联
INSERT INTO `admin_sm_tenant_user_rel` VALUES ('a2fa6f5k28e34di2ab22cf549de41e31', '40', '1');
-- 把已有的租户信息添加到管理表中
insert into admin_sm_tenant_user_rel select (select md5(uuid())),user_id,DATA_TENANT_ID from admin_sm_user_role_rel asurr where asurr.ROLE_ID in (select role_id from admin_sm_role asr where asr.ROLE_LEVEL = '-1');


-- ----------------------------
-- Table structure for admin_sm_exclude_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_exclude_menu`;
CREATE TABLE `admin_sm_exclude_menu`
(
    `MENU_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单编号',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '不能授权的菜单' ROW_FORMAT = Dynamic;

-- 租户管理和公共参数管理不能给其它租户和用户授权
INSERT INTO `admin_sm_exclude_menu` VALUES ('4b6a5712e0e244179b289908dcad50b5','1');
INSERT INTO `admin_sm_exclude_menu` VALUES ('dafdca98d69041a5aa190a6e489be358','1');


-- 修改密码长度和复杂度策略
UPDATE `admin_sm_crel_stra` SET SYS_ID='YUSP', CREL_KEY='PASSWD_LENGTH_RULE', CREL_NAME='修改密码口令长度策略', ENABLE_FLAG='2', CREL_DETAIL='', CREL_DESCRIBE='修改密码时，密码长度必须不小于8位，不大于24位', ACTION_TYPE='2', LAST_CHG_USR='40', LAST_CHG_DT='2021-04-09 13:47:51', SYS_DEFAULT=0, DATA_TENANT_ID='1'
WHERE CREL_ID='34404cd4c4a94d059f831b253770df2f';
UPDATE `admin_sm_crel_stra` SET SYS_ID='YUSP', CREL_KEY='PASSWD_COMPLEX_RULE', CREL_NAME='修改密码口令复杂度策略', ENABLE_FLAG='2', CREL_DETAIL='number,specialCharacters,lowercase,uppercase', CREL_DESCRIBE='修改密码时，密码中至少有指定的一个字符', ACTION_TYPE='2', LAST_CHG_USR='40', LAST_CHG_DT='2021-04-13 16:22:07', SYS_DEFAULT=0, DATA_TENANT_ID='1'
WHERE CREL_ID='5a8f2c44a9c94b26adb6b9bbabcb1a9e';

UPDATE `admin_sm_crel_stra_en_us` SET SYS_ID='YUSP', CREL_KEY='PASSWD_LENGTH_RULE', CREL_NAME='Change password length policy', ENABLE_FLAG='2', CREL_DETAIL='', CREL_DESCRIBE='When changing the password,The length of the password must be no less than 8 digits and no more than 24 digits', ACTION_TYPE='2', LAST_CHG_USR='40', LAST_CHG_DT='2021-04-09 13:47:51', SYS_DEFAULT=0, DATA_TENANT_ID='1'
WHERE CREL_ID='34404cd4c4a94d059f831b253770df2f';
UPDATE `admin_sm_crel_stra_en_us` SET SYS_ID='YUSP', CREL_KEY='PASSWD_COMPLEX_RULE', CREL_NAME='Password complexity strategy', ENABLE_FLAG='2', CREL_DETAIL='number,specialCharacters,lowercase,uppercase', CREL_DESCRIBE='When changing the password,There is at least one character specified in the password', ACTION_TYPE='2', LAST_CHG_USR='40', LAST_CHG_DT='2021-04-13 16:22:07', SYS_DEFAULT=0, DATA_TENANT_ID='1'
WHERE CREL_ID='5a8f2c44a9c94b26adb6b9bbabcb1a9e';



-- 新增用户账号或者工号已存在的异常提示
INSERT INTO `admin_sm_message_en_us` values ('57189D9B49C74D0187A94D3903FF7E95', '51100005', 'error', 'The user or login code of the new user already exists. Please operate after replacing the code', 'MODULEINFO', 'oca_SmUser', '40', '2022-04-09 21:31:33', '1');
INSERT INTO `admin_sm_message` values ('7EBE3D294AA2458880815474750D73EA', '51100005', 'error', '新增的用户账号或者工号已经存在，请更换用户账号或者工号后操作', 'MODULEINFO', 'oca_SmUser', '40', '2022-04-09 21:38:15', '1');

-- 更新数据字典代码的错误提示
update `admin_sm_message_en_us` set message='Dictionary code  cannot be repeated' where code='50500002';

-- 更新系统参数名的错误提示
INSERT INTO `admin_sm_message_en_us` VALUES('59358EA21A56412B99D8095CC3583637', '50600003', 'error', 'The parameter name already exists. Please operate after replacing the parameter name!', 'MODULEINFO', 'oca_SmMsg', '40', '2022-04-09 21:57:39', '1');
INSERT INTO `admin_sm_message` VALUES('C523D8F5D244454CBCAD58C59909553F', '50600003', 'error', '参数名已存在，请替换参数名后进行操作！', 'MODULEINFO', 'oca_SmMsg', '40', '2022-04-10 22:00:34', '1');

INSERT INTO `admin_sm_message_en_us` VALUES('93B818D5C82B492599ECD81077B6F57A', '50600002', 'error', 'The information code already exists. Please operate after replacing the information code!', 'MODULEINFO', 'oca_SmMsg', '40', '2022-04-10 22:11:25', '1');
INSERT INTO `admin_sm_message` VALUES('E02E3E145FB04ED7BB1BDE3A7849CCDE', '50600002', 'error', '系统提示消息信息码已存在，请修改提示消息信息码后再进行操作！', 'MODULEINFO', 'oca_SmMsg', '40', '2022-04-10 22:13:16', '1');

INSERT INTO `admin_sm_message_en_us` VALUES('A8F7F3C36BD641C9A1560E8B13E817F6', '50600005', 'error', 'Business function name under the same module already exists', 'MODULEINFO', 'oca_SmBusiness', '40', '2022-04-10 22:27:42', '1');
INSERT INTO `admin_sm_message` VALUES('8771275A35DD45D6A7C217371A71D718', '50600005', 'error', '同模块下业务功能名称已存在', 'MODULEINFO', 'oca_SmBusiness', '40', '2022-04-10 22:29:28', '1');


INSERT INTO `admin_sm_message` VALUES('F88C623341B146D3AFFD641DA24EA171', '50600006', 'error', '已关联业务功能！', 'MODULEINFO', 'oca_SmBusiness', '40', '2022-04-10 22:33:06', '1');
INSERT INTO `admin_sm_message_en_us` VALUES('53F8841787C842BA89D4A2F0E5D8598A', '50600006', 'error', 'Associated business function', 'MODULEINFO', 'oca_SmBusiness', '40', '2022-04-10 22:34:28', '1');


INSERT INTO `admin_sm_message_en_us` VALUES('B96B5FC6847D444192DE0F3129674539', '50600004', 'error', 'Data template name already exists', 'MODULEINFO', 'oca_SmDatatemp', '40', '2022-04-10 22:38:42', '1');
INSERT INTO `admin_sm_message`  VALUES('074B55473EA64EFCBB62901343A3BA36', '50600004', 'error', '数据权限模板名已存在', 'MODULEINFO', 'oca_SmDatatemp', '40', '2022-04-10 22:39:04', '1');

update `admin_sm_message_en_us` set message='This role is bound to a user, please delete the associated information before operation' where code='50800001';

INSERT INTO `admin_sm_exclude_menu` VALUES('E95D3198608447ACB62AC15EAAC62F6D', '1');
INSERT INTO `admin_sm_exclude_menu` VALUES('869baf3c788448668f8e98fbc9687fde', '1');

-- 修改引文环境下的定时任务的菜单id同中文环境中一致
update `admin_sm_menu_en_us` set menu_id='f7e5e43f35fe40b49ada1a7f3dfbc4bf' where menu_id = 'fbdf32e65af740cbbb9c381b70f1582c';

-- cron不合法的提示
INSERT INTO  `admin_sm_message`  VALUES('C13D9BD5A32E4121B1F4BABE59B0F53B', '50910001', 'info', 'cron不合法', 'MODULEINFO', 'oca_SmTimer', '40', '2022-04-13 10:11:36', '1');
INSERT INTO  `admin_sm_message_en_us` VALUES('CF2D74FB15B548B99007AA0072D683F6', '50910001', 'error', 'Illegal Cron', 'MODULEINFO', 'oca_SmTimer', '40', '2022-04-13 10:11:12', '1');

-- 修改菜单图标
UPDATE `admin_sm_menu` SET `menu_icon` = 'yu-icon-home' where `menu_id` = 'E95D3198608447ACB62AC15EAAC62F6D';
UPDATE `admin_sm_menu` SET `menu_icon` = 'yu-icon-display' where `menu_id` = '39c0cf27c05a4eff8aba3be93ccb4bf6';
UPDATE `admin_sm_menu` SET `menu_icon` = 'yu-icon-user1' where `menu_id` = '39B2968841334098AC9AABB7F782BC82';
UPDATE `admin_sm_menu` SET `menu_icon` = 'yu-icon-projct' where `menu_id` = '99d2457ae47d4e149c4e1a5b594d5716';
UPDATE `admin_sm_menu_en_us` SET `menu_icon` = 'yu-icon-home' where `menu_id` = 'E95D3198608447ACB62AC15EAAC62F6D';
UPDATE `admin_sm_menu_en_us` SET `menu_icon` = 'yu-icon-display' where `menu_id` = '39c0cf27c05a4eff8aba3be93ccb4bf6';
UPDATE `admin_sm_menu_en_us` SET `menu_icon` = 'yu-icon-user1' where `menu_id` = '39B2968841334098AC9AABB7F782BC82';
UPDATE `admin_sm_menu_en_us` SET `menu_icon` = 'yu-icon-projct' where `menu_id` = '99d2457ae47d4e149c4e1a5b594d5716';

-- 添加公告错误提示

INSERT INTO `admin_sm_message` VALUES('4F094ABFF76A4619980DA37CECB4FE2E', '52000001', 'error', '请选择您想要删除的公告！', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 16:54:00', '1');
INSERT INTO `admin_sm_message` VALUES('BE711C86D0DB484CB1AE77248067B0FC', '52000002', 'error', '该公告已发布，不能删除!', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 16:56:16', '1');
INSERT INTO `admin_sm_message` VALUES('198C695D8D024A148EA13FD87F1EE772', '52000003', 'error', '该公告已发布，不能修改!', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 16:54:50', '1');

INSERT INTO `admin_sm_message_en_us` VALUES('31392B1211C341F4BAD99288BAB8780E', '52000002', 'error', 'This announcement has been published and cannot be deleted!', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 17:03:50', '1');
INSERT INTO `admin_sm_message_en_us` VALUES('C2A678AADFAD43AAB639348D04C583D8', '52000001', 'error', 'Please select the announcement you want to delete!', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 17:02:42', '1');
INSERT INTO `admin_sm_message_en_us` VALUES('DB2045BDB1AE41B983A3839DC38CD0A3', '52000003', 'error', 'This announcement has been published and cannot be modified!', 'MODULEINFO', 'oca_SmNotice', '40', '2022-04-13 17:04:35', '1');


INSERT INTO `admin_sm_message_en_us` VALUES('6E441C0EE5BB4AC7BB6745C2B87801A3', '50600007', 'error', 'The data template is associated with a control point and cannot be deleted!', 'MODULEINFO', 'oca_SmDataTemp', '40', '2022-04-14 13:37:09', '1');
INSERT INTO `admin_sm_message` VALUES('B7375BB95A564E02A546621333A620A5', '50600007', 'error', '数据模板关联了控制点，无法删除！', 'MODULEINFO', 'oca_SmDataTemp', '40', '2022-04-14 13:45:15', '1');




