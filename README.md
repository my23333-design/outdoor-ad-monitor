# 户外广告监测管理系统

> 基于 RuoYi-Vue 二次开发的全栈项目，覆盖 PC 管理后台 + 微信小程序双端，
> 落地"户外广告投放 + 现场巡检 + AI 审核"业务闭环。

## 📌 项目简介

本系统服务于户外广告投放与现场巡检业务，后台支持广告主管理广告位、巡查台账与违规事件，
小程序端支持现场巡检员通过手机完成定位、影像上报与表单提交。
集成百度 AI 开放能力对广告影像进行自动审核，配套数据集成、数据同步、
数据校验与可视化看板等能力。

## 🛠 技术栈

### 后端
- **语言**：Java 8
- **框架**：Spring Boot、MyBatis、Spring Security、JWT、RBAC
- **数据库**：MySQL 5.7+、Druid 连接池
- **第三方**：百度 AI 图像审核（contentCensor）
- **工具**：Maven、Git

### 管理后台
- **框架**：Vue 2、RuoYi-Vue、Element UI
- **构建**：Vue CLI、Webpack

### 微信小程序
- **框架**：微信原生开发
- **图表**：ECharts（ec-canvas）
- **能力**：OAuth2 授权、定位、影像上传、表单上报

### 开发工具
- **IDE**：IntelliJ IDEA、VS Code
- **AI 辅助**：GitHub Copilot
- **版本控制**：Git、GitHub

## ✨ 核心功能

### 业务模块（12 个 Controller）

| 模块 | 说明 |
| --- | --- |
| 广告位管理 | 广告位增删改查、地图标注、状态流转 |
| AI 智能审核 | 集成百度 AI 图像审核接口，自动识别违规内容 |
| 审计记录 | 审核流程记录与可追溯 |
| 数据集成 | 多源数据接入与整合 |
| 数据可视化 | ECharts 看板展示广告投放与巡检统计 |
| 执法管理 | 违规事件处理与执法流程 |
| 行业管理 | 广告行业分类维护 |
| 法律条款 | 法规条款维护与匹配 |
| 媒介管理 | 媒介资源管理 |
| 处理结果 | 审核处理结果记录 |
| 区域管理 | 区域数据维护 |
| 流程测试 | 流程接口调试 |

### 微信小程序（6 个页面）

- **广告详情**（adDetail）：查看广告位详细信息
- **历史记录**（historyList）：查看历史巡查记录
- **首页**（index）：业务入口
- **登录**（login）：OAuth2 授权登录
- **日志**（logs）：操作日志
- **我的**（my）：个人中心

### 数据服务

- **数据同步**（DataSyncService）：定期拉取外部数据
- **数据校验**（DataValidationService）：业务规则校验
- **定时任务**（AdvertisementTask）：调度任务执行

## 🚀 项目亮点

- **完整的全栈经验**：独立完成后端 12 个 Controller、Vue 前端与微信小程序三端开发与联调
- **第三方 AI 集成**：调用百度 AI 图像审核接口，实现广告影像自动识别违规
- **数据集成能力**：设计数据映射规则、集成方案、校验与同步流程，输出 2 份独立设计文档
- **可视化看板**：基于 ECharts 实现广告投放与巡检数据可视化
- **工程化实践**：Git 版本管理 + 分支规范，使用 AI 辅助开发工具提升编码效率

## 📁 目录结构

```
outdoor-ad-monitor/
├── ruoyi-advertisement/                    # 核心业务模块
│   ├── docs/                               # 数据映射规则、集成方案设计文档
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/ruoyi/advertisement/
│       │   ├── controller/                 # 12 个业务 Controller
│       │   ├── service/                    # 9 个 Service 接口
│       │   ├── service/impl/               # 9 个 Service 实现
│       │   ├── mapper/                     # 6 个 Mapper 接口
│       │   ├── domain/                     # 6 个实体类
│       │   ├── scheduled/                  # 定时任务
│       │   └── util/                       # 百度 AI 工具类
│       └── resources/mapper/advertisement/ # 6 个 Mapper XML
├── miniprogram/                            # 微信小程序
│   ├── pages/                              # 6 个业务页面
│   ├── utils/                              # 网络请求、工具函数
│   ├── style/                              # 通用样式
│   └── static/                             # 静态资源（图标、图片）
├── sql/                                    # 数据库脚本
└── README.md
```

> 本项目基于开源框架 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 二次开发，
> 仅展示本人实际参与的业务模块与定制化开发内容。

## ⚙️ 快速开始

### 环境要求
- JDK 1.8+、Maven 3.6+
- MySQL 5.7+ / 8.0
- Node.js 14+（前端依赖）

### 后端启动
```bash
# 1. 克隆仓库
git clone https://github.com/my23333-design/outdoor-ad-monitor.git

# 2. 初始化数据库
#    将 sql/ 目录下的脚本导入 MySQL
mysql -u root -p < sql/ad_dict_init.sql
mysql -u root -p < sql/add_role_permission.sql
mysql -u root -p < sql/test_data_for_ai_review.sql

# 3. 启动后端
cd ruoyi-advertisement
mvn spring-boot:run
```

### 小程序启动
1. 用微信开发者工具打开 `miniprogram/` 目录
2. 在 `project.config.json` 中填入小程序 AppID
3. 配置后端 API 地址后即可预览

## 📫 联系我

- **作者**：王浩
- **学校**：河北工程技术学院 · 区块链工程（2027 届）
- **方向**：Web 全栈开发
- **GitHub**：[my23333-design](https://github.com/my23333-design)

> 欢迎各位面试官 / HR 通过 Issue 或邮件联系。
