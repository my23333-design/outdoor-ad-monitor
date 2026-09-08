-- AI初审功能测试数据
-- 包含十条覆盖不同场景类型的测试数据
-- 数据结构完整，包含所有必要字段

-- ----------------------------
-- 广告数据
-- ----------------------------
INSERT INTO ad_advertisement (id, ad_profitability_type, ad_industry_type, ad_medium_type, ad_images, province, city, district, street, address, advertiser, survey_time, audit_status, violation_type, ad_content, contact_phone, contact_person, check_status, create_by, create_time, update_by, update_time, remark) VALUES
(1, '1', '餐饮', '户外广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=restaurant%20advertisement%20signboard%20with%20delicious%20food&image_size=square_hd', '广东省', '广州市', '天河区', '天河路', '天河路188号', '粤菜餐饮连锁', '2025-08-01', '0', '0', '正宗粤菜，传承百年。精选新鲜食材，呈现地道风味。开业优惠，全单8折。地址：天河路188号。订餐电话：020-88888888', '020-88888888', '陈经理', '0', 'admin', NOW(), '', NULL, '正常案例 - 餐饮行业广告'),
(2, '1', '教育培训', '网络', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=online%20education%20platform%20with%20students%20studying&image_size=square_hd', '北京市', '北京市', '海淀区', '中关村大街', '中关村大街1号', '智慧在线教育', '2025-08-02', '0', '0', '智慧在线教育——专业K12在线辅导平台。顶尖师资，一对一在线教学。让孩子在家也能享受优质教育资源。限时报名，赠送价值299元学习礼包。', '010-66666666', '王老师', '0', 'admin', NOW(), '', NULL, '正常案例 - 教育培训广告'),
(3, '1', '医疗健康', '电视', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pharmaceutical%20advertisement%20with%20medical%20cross%20symbol&image_size=square_hd', '上海市', '上海市', '浦东新区', '世纪大道', '世纪大道100号', '康德医药集团', '2025-08-03', '0', '0', '康德医药——专业皮肤科用药。我们承诺：所有药品均经过国家药监局审批。请在医师指导下使用。官方网站：www.kangde.com', '021-55555555', '李医生', '0', 'admin', NOW(), '', NULL, '边界案例 - 医疗健康广告（需人工复审）'),
(4, '1', '房地产', '电梯广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=luxury%20apartment%20building%20advertisement%20with%20modern%20design&image_size=square_hd', '广东省', '深圳市', '南山区', '科技园南路', '科技园南路88号', '华润置地', '2025-08-04', '0', '0', '华润城——深圳核心地段精装公寓。地铁上盖，配套完善。投资自住两相宜。开发商郑重承诺：不捆绑装修，不收取电商费。', '0755-22222222', '张销售', '0', 'admin', NOW(), '', NULL, '边界案例 - 房地产广告（包含"投资"等敏感词）'),
(5, '1', '金融', '报纸', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bank%20advertisement%20with%20money%20and%20finance%20symbols&image_size=square_hd', '北京市', '北京市', '西城区', '金融大街', '金融大街8号', '华信银行', '2025-08-05', '0', '0', '华信银行定期存款产品——安全稳健，收益可靠。1年期利率2.1%，3年期利率3.2%。银行存款受存款保险保障。理财有风险，投资需谨慎。', '010-33333333', '赵经理', '0', 'admin', NOW(), '', NULL, '边界案例 - 金融广告（包含收益率等敏感词）'),
(6, '1', '餐饮', '户外广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fast%20food%20restaurant%20advertisement%20with%20burger%20and%20cola&image_size=square_hd', '江苏省', '南京市', '鼓楼区', '中山北路', '中山北路188号', '麦乐快餐', '2025-08-06', '0', '0', '麦乐快餐——源自美国的经典快餐品牌。全球连锁，品质如一。汉堡薯条可乐套餐仅需29.9元。招加盟商，0加盟费，3个月回本。', '025-44444444', '刘老板', '0', 'admin', NOW(), '', NULL, '正常案例 - 快餐广告'),
(7, '1', '汽车', '户外广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=car%20dealership%20advertisement%20with%20luxury%20sedan%20in%20showroom&image_size=square_hd', '浙江省', '杭州市', '西湖区', '文三路', '文三路258号', '德系汽车销售', '2025-08-07', '0', '0', '德系汽车授权经销商——专注德系豪华品牌20年。提供新车销售、二手车置换、汽车金融等一站式服务。置换补贴最高3万元。', '0571-55555555', '孙经理', '0', 'admin', NOW(), '', NULL, '正常案例 - 汽车广告'),
(8, '2', '其他', '户外广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=public%20welfare%20advertisement%20with%20green%20earth%20and%20heart%20symbol&image_size=square_hd', '四川省', '成都市', '锦江区', '春熙路', '春熙路100号', '成都市环保协会', '2025-08-08', '0', '0', '低碳生活，绿色出行。成都市环保协会呼吁：少开一天车，多植一棵树。让我们共同守护蓝天白云，建设美丽成都。', '028-66666666', '环保志愿者', '0', 'admin', NOW(), '', NULL, '正常案例 - 公益广告'),
(9, '1', '旅游', '网络', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=travel%20advertisement%20with%20beach%20resort%20and%20palm%20trees&image_size=square_hd', '海南省', '三亚市', '海棠区', '海棠湾路', '海棠湾路88号', '阳光旅游度假村', '2025-08-09', '0', '0', '阳光旅游度假村——三亚海棠湾畔的豪华度假胜地。私人沙滩、无边泳池、热带花园。逃离城市喧嚣，享受宁静假期。预订热线：0898-88888888', '0898-88888888', '周经理', '0', 'admin', NOW(), '', NULL, '正常案例 - 旅游广告'),
(10, '1', '房地产', '户外广告', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=real%20estate%20development%20project%20advertisement%20with%20modern%20buildings&image_size=square_hd', '重庆市', '重庆市', '渝北区', '新南路', '新南路188号', '龙湖地产', '2025-08-10', '0', '0', '龙湖地产——中国房地产十强。品质住宅，匠心筑造。在售项目：龙湖天街、龙湖时代天街。尊享VIP购房优惠，详情咨询售楼处。', '023-77777777', '吴销售', '0', 'admin', NOW(), '', NULL, '正常案例 - 房地产广告');

-- ----------------------------
-- 测试数据说明
-- ----------------------------
-- 1. 正常案例（7条）：
--    - 粤菜餐饮连锁（ID=1）：合法的餐饮行业广告
--    - 智慧在线教育（ID=2）：合法的教育培训广告
--    - 麦乐快餐（ID=6）：合法的快餐广告
--    - 德系汽车销售（ID=7）：合法的汽车销售广告
--    - 公益广告（ID=8）：合法的公益广告
--    - 旅游度假村（ID=9）：合法的旅游广告
--    - 龙湖地产（ID=10）：合法的房地产广告
--
-- 2. 边界案例（3条）：
--    - 康德医药集团（ID=3）：医疗健康广告，需人工复审
--    - 华润置地（ID=4）：房地产广告，包含"投资"等敏感词
--    - 华信银行（ID=5）：金融广告，包含收益率等敏感词
--
-- 3. 异常案例（0条）：
--    - 本次测试数据中未包含明显的异常案例，所有数据均符合AI初审功能的输入格式要求
--
-- 4. 字段说明：
--    - ad_profitability_type：营利类型（1=营利性，2=非营利性）
--    - ad_industry_type：行业分类（餐饮、教育培训、医疗健康、房地产、金融、汽车、旅游、其他）
--    - ad_medium_type：媒体类型（户外广告、网络、电视、电梯广告、报纸）
--    - audit_status：审核状态（0=未审核，1=初审合法，2=初审存疑，3=初审违法，5=驳回初审）
--    - violation_type：违规类别（0=无违规，1=虚假宣传，2=违法内容，3=不正当竞争，4=其他违规）
--
-- 5. 测试建议：
--    - 测试AI初审功能时，建议先测试正常案例，确保基本功能正常
--    - 然后测试边界案例，验证AI审核的准确性
--    - 最后可以添加异常案例进行测试