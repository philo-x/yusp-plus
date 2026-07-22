-- 数据字典中添加 ZERO_FILL，用于序列好模板不足位数0补全的下拉选（之前是YESNO，只是01和02，是错误的，应该是Y和H）
INSERT INTO admin_sm_lookup_dict
VALUES ('87a8cf51429944cbb03627091e9bbb10', 'ZERO_FILL', '不足位数0补全', '87br87dcb83f4ab4b22d5fee13iec056', '公共参数', 'c471d440f46b4bbd91595da33bab26c8', 'Y', '是', NULL, 1, NULL, '40', '2022-04-27 14:10:46', '1');
INSERT INTO admin_sm_lookup_dict
VALUES ('bd4a569d769a4d7fad3ee440d2108944', 'ZERO_FILL', '不足位数0补全', '87br87dcb83f4ab4b22d5fee13iec056', '公共参数', 'c471d440f46b4bbd91595da33bab26c8', 'N', '否', NULL, 2, NULL, '40', '2022-04-27 14:10:46', '1');
INSERT INTO admin_sm_lookup_dict
VALUES ('c471d440f46b4bbd91595da33bab26c8', 'ZERO_FILL', '不足位数0补全', '87br87dcb83f4ab4b22d5fee13iec056', '公共参数', '-1', '0', '0', NULL, 0, NULL, '40', '2022-04-27 14:10:46', '1');



INSERT INTO admin_sm_lookup_dict_en_us
VALUES ('87a8cf51429944cbb03627091e9bbb10', 'ZERO_FILL', 'Supplemented by Zero', '87br87dcb83f4ab4b22d5fee13iec056', 'Public parameters', 'c471d440f46b4bbd91595da33bab26c8', 'N', 'no', NULL, 2, NULL, '40', '2022-04-27 14:23:38', '1');
INSERT INTO admin_sm_lookup_dict_en_us
VALUES ('bd4a569d769a4d7fad3ee440d2108944', 'ZERO_FILL', 'Supplemented by Zero', '87br87dcb83f4ab4b22d5fee13iec056', 'Public parameters', 'c471d440f46b4bbd91595da33bab26c8', 'Y', 'yes', NULL, 1, NULL, '40', '2022-04-27 14:23:38', '1');
INSERT INTO admin_sm_lookup_dict_en_us
VALUES ('c471d440f46b4bbd91595da33bab26c8', 'ZERO_FILL', 'Supplemented by Zero', '87br87dcb83f4ab4b22d5fee13iec056', 'Public parameters', '-1', '0', '0', NULL, 0, NULL, '40', '2022-04-27 14:23:38', '1');

