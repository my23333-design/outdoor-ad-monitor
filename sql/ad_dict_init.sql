-- ----------------------------
-- 广告相关字典类型和数据
-- ----------------------------

-- 广告行业分类字典类型
insert into sys_dict_type values(null, '广告行业分类', 'ad_industry_type', '0', 'admin', sysdate(), '', null, '广告行业分类列表');

-- 广告行业分类字典数据
insert into sys_dict_data values(null, 1, '餐饮', '1', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '餐饮行业');
insert into sys_dict_data values(null, 2, '房地产', '2', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '房地产行业');
insert into sys_dict_data values(null, 3, '教育培训', '3', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '教育培训行业');
insert into sys_dict_data values(null, 4, '医疗健康', '4', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '医疗健康行业');
insert into sys_dict_data values(null, 5, '金融', '5', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '金融行业');
insert into sys_dict_data values(null, 6, '汽车', '6', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '汽车行业');
insert into sys_dict_data values(null, 7, '旅游', '7', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '旅游行业');
insert into sys_dict_data values(null, 8, '其他', '99', 'ad_industry_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '其他行业');

-- 广告媒体类型字典类型
insert into sys_dict_type values(null, '广告媒体类型', 'ad_medium_type', '0', 'admin', sysdate(), '', null, '广告媒体类型列表');

-- 广告媒体类型字典数据
insert into sys_dict_data values(null, 1, '电视', '1', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '电视媒体');
insert into sys_dict_data values(null, 2, '报纸', '2', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '报纸媒体');
insert into sys_dict_data values(null, 3, '杂志', '3', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '杂志媒体');
insert into sys_dict_data values(null, 4, '户外', '4', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '户外媒体');
insert into sys_dict_data values(null, 5, '网络', '5', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '网络媒体');
insert into sys_dict_data values(null, 6, '广播', '6', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '广播媒体');
insert into sys_dict_data values(null, 7, '其他', '99', 'ad_medium_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '其他媒体');

-- 广告营利类型字典类型
insert into sys_dict_type values(null, '广告营利类型', 'ad_profitability_type', '0', 'admin', sysdate(), '', null, '广告营利类型列表');

-- 广告营利类型字典数据
insert into sys_dict_data values(null, 1, '营利性', '1', 'ad_profitability_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '营利性广告');
insert into sys_dict_data values(null, 2, '非营利性', '2', 'ad_profitability_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '非营利性广告');

-- 广告查看状态字典类型
insert into sys_dict_type values(null, '广告查看状态', 'ad_check_status', '0', 'admin', sysdate(), '', null, '广告查看状态列表');

-- 广告查看状态字典数据
insert into sys_dict_data values(null, 1, '已查看', '1', 'ad_check_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '已查看');
insert into sys_dict_data values(null, 2, '未查看', '2', 'ad_check_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '未查看');

-- 广告处理状态字典类型
insert into sys_dict_type values(null, '广告处理状态', 'ad_handle_status', '0', 'admin', sysdate(), '', null, '广告处理状态列表');

-- 广告处理状态字典数据
insert into sys_dict_data values(null, 1, '已处理', '1', 'ad_handle_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '已处理');
insert into sys_dict_data values(null, 2, '未处理', '2', 'ad_handle_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '未处理');

-- 广告审核状态字典类型
insert into sys_dict_type values(null, '广告审核状态', 'ad_audit_status', '0', 'admin', sysdate(), '', null, '广告审核状态列表');

-- 广告审核状态字典数据
insert into sys_dict_data values(null, 1, '通过', '1', 'ad_audit_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '审核通过');
insert into sys_dict_data values(null, 2, '拒绝', '2', 'ad_audit_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '审核拒绝');
insert into sys_dict_data values(null, 3, '待审核', '3', 'ad_audit_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '待审核');

-- 广告违规类型字典类型
insert into sys_dict_type values(null, '广告违规类型', 'ad_violation_type', '0', 'admin', sysdate(), '', null, '广告违规类型列表');

-- 广告违规类型字典数据
insert into sys_dict_data values(null, 1, '虚假宣传', '1', 'ad_violation_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '虚假宣传');
insert into sys_dict_data values(null, 2, '违法内容', '2', 'ad_violation_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '违法内容');
insert into sys_dict_data values(null, 3, '不正当竞争', '3', 'ad_violation_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '不正当竞争');
insert into sys_dict_data values(null, 4, '其他违规', '99', 'ad_violation_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '其他违规');

-- 广告AI状态字典类型
insert into sys_dict_type values(null, '广告AI状态', 'ad_ai_status', '0', 'admin', sysdate(), '', null, '广告AI状态列表');

-- 广告AI状态字典数据
insert into sys_dict_data values(null, 1, '已分析', '1', 'ad_ai_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '已分析');
insert into sys_dict_data values(null, 2, '未分析', '2', 'ad_ai_status', '', '', 'N', '0', 'admin', sysdate(), '', null, '未分析');
