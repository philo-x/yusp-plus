alter table admin_sm_res_contr add column ENCODE_CHECK char(2) default '02' not null comment '强制加密';
alter table admin_sm_res_contr add column NONCE_CHECK char(2) default '02' not null comment '强制防重';
alter table admin_sm_res_contr_en_us add column ENCODE_CHECK char(2) default '02' not null comment '强制加密';
alter table admin_sm_res_contr_en_us add column NONCE_CHECK char(2) default '02' not null comment '强制防重';