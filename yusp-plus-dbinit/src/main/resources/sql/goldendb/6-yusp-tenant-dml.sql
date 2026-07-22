-- token刷新
update oauth_client_details set `refresh_token_validity` = 21000 where `client_id` = 'test';


-- 多租户的国际化
-- ----------------------------
-- Records of admin_sm_tenant_en_us
-- ----------------------------
-- 新增多租户表
INSERT INTO `admin_sm_tenant_en_us` VALUES ('1', 'System default tenant', 'System default tenant unit', '0', 'admin', '2021-09-17 15:40:10', '1');

-- 多租户菜单新增
INSERT INTO `admin_sm_menu_en_us` VALUES ('db7af96c549f4ff2905cf2c14bd830e0', 'test', 'ED8F4F3846CB43C68D26D7E15F6C83C2', '74d47cbb74d64263b5b2da4f18bfdd2d', 'Tenant MGT', 45, 'el-icon-yx-sphere', NULL, '40', '2020-04-26 09:56:46', NULL, '0', 0, '1');

-- 多租户菜单授权
INSERT INTO `admin_sm_auth_reco` VALUES ('07057FE9DF5A42908CB8B7D52026FF8D', 'test', 'R', 'cc81a8d86f274c81bc680a0bbd27e358', 'M', 'db7af96c549f4ff2905cf2c14bd830e0', '40', '2021-09-17 17:26:25', 'db7af96c549f4ff2905cf2c14bd830e0', '1');





-- 把所有data_tenant_id为null的改为1
update admin_file_upload_info
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_auth_info
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_auth_reco
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_busi_func
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_crel_stra
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_crel_stra_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_data_auth
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_data_auth_tmpl
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_dpt
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_duty
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_func_mod
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_instu
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_log
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_logic_sys
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_login_log
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_lookup_dict
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_lookup_dict_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_menu
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_menu_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_message
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_message_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_notice
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_notice_read
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_notice_recive
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_org
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_password_log
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_prop
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_res_contr
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_res_contr_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_richedit_file_info
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_richedit_info
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_role
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_role_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_tenant
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_user
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_user_duty_rel
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_user_en_us
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_user_mgr_org
set data_tenant_id='1'  
where data_tenant_id is null;
update admin_sm_user_role_rel
set data_tenant_id='1'  
where data_tenant_id is null;


-- 修改表中含有DATA_TENANT_ID字段的表结构，默认DATA_TENANT_ID为1
alter table admin_file_upload_info modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_auth_info modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_auth_reco modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_busi_func modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_crel_stra modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_crel_stra_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_data_auth modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_data_auth_tmpl modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_dpt modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_duty modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_func_mod modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_instu modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_log modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_logic_sys modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_login_log modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_lookup_dict modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_lookup_dict_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_menu modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_menu_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_message modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_message_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_notice modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_notice_read modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_notice_recive modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_org modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_password_log modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_prop modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_res_contr modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_res_contr_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_richedit_file_info modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_richedit_info modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_role modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_role_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_tenant modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_user modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_user_duty_rel modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_user_en_us modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_user_mgr_org modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;
alter table admin_sm_user_role_rel modify column DATA_TENANT_ID varchar (32) default '1'   not null comment '数据所属租户id'  ;


-- 修改客户端和xys_id统一为YUSP
UPDATE `oauth_client_details` set client_id = 'YUSP'
 where client_id = 'test';
UPDATE `admin_sm_logic_sys` set sys_id = 'YUSP',
 SYS_CODE = 'YUSP' where sys_id = 'test';
UPDATE `admin_sm_auth_reco` set sys_id = 'YUSP' where sys_id = 'test';
UPDATE `admin_sm_crel_stra` set sys_id = 'YUSP' where sys_id = 'test';
UPDATE `admin_sm_instu` set sys_id = 'YUSP' where sys_id = 'test';
UPDATE `admin_sm_menu` set sys_id = 'YUSP' where sys_id = 'test';


UPDATE `admin_sm_menu_en_us` set sys_id = 'YUSP' where sys_id = 'test';
UPDATE `admin_sm_crel_stra_en_us` set sys_id = 'YUSP' where sys_id = 'test';
