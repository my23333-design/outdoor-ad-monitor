# 户外广告监测管理系统

> 基于 RuoYi-Vue 二次开发的全栈项目，覆盖 PC 管理后台 + 微信小程序双端，
> 落地"户外广告投放 + 现场巡检"业务闭环。

## 项目简介

本系统服务于户外广告投放与现场巡检业务，后台支持广告主管理广告位、巡查台账与违规事件，
小程序端支持现场巡检员通过手机完成定位、影像上报与表单提交。

## 技术栈

### 后端
- **语言**：Java
- **框架**：Spring Boot、MyBatis
- **安全**：Spring Security、JWT、RBAC 权限模型
- **数据库**：MySQL
- **工具**：Maven、Git

### 前端（管理后台）
- **框架**：Vue 2、RuoYi-Vue
- **组件**：Element UI
- **构建**：Vue CLI、Webpack

### 微信小程序
- **框架**：微信原生开发
- **图表**：ECharts（ec-canvas）
- **能力**：OAuth2 授权、定位、影像上传

### 开发工具
- **IDE**：IntelliJ IDEA、VS Code
- **AI 辅助**：GitHub Copilot
- **版本控制**：Git、GitHub

## 核心功能模块

| 模块 | 说明 |
| --- | --- |
| 广告位管理 | 广告位增删改查、地图标注、状态流转 |
| 巡查台账 | 巡检工单生成、现场上报、流程审核 |
| 违规事件 | 违规信息登记、图片取证、审核处理 |
| RBAC 权限 | 在 RuoYi-Vue 原生权限基础上扩展"广告主 / 巡检员 / 审核员"三类业务角色 |
| 数据看板 | 集成 ECharts 展示广告投放与巡检统计数据 |
| 微信小程序 | 巡检员现场使用：OAuth2 登录、定位上报、影像上传、表单提交 |

## 项目亮点

- **完整的全栈开发经验**：独立完成后端接口、Vue 前端与微信小程序三端开发及联调
- **RBAC 权限扩展**：在 RuoYi-Vue 框架基础上扩展业务角色与权限分配
- **移动端能力落地**：小程序端完成 OAuth2 授权、定位、影像压缩上传等核心能力
- **数据可视化**：集成 ECharts 实现巡检数据看板
- **AI 辅助开发**：使用 GitHub Copilot 提升 CRUD 代码生成与代码重构效率

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+ / 8.0
- Node.js 14+（前端依赖）

### 后端启动
```bash
# 1. 克隆仓库
git clone https://github.com/my23333-design/outdoor-ad-monitor.git

# 2. 初始化数据库
#    将 sql/ 目录下的脚本导入 MySQL
mysql -u root -p < sql/outdoor_ad.sql

# 3. 配置后端
#    进入 backend/ 目录，复制 application.yml.example 为 application.yml
#    填入你的 MySQL 密码、JWT 密钥、微信 AppID 等配置

# 4. 启动 Spring Boot
mvn spring-boot:run
```

### 前端启动（管理后台）
```bash
cd ruoyi-ui
npm install
npm run dev
```
访问 http://localhost:80

### 小程序启动
1. 用微信开发者工具打开 `miniprogram/` 目录
2. 在 `project.config.json` 中填入你的小程序 AppID
3. 配置后端 API 地址后即可预览

## 目录结构

```
outdoor-ad-monitor/
├── backend/
│   ├── ruoyi-advertisement/    # 核心业务模块（广告位、巡检、违规事件）
│   └── application*.example    # 配置示例
├── frontend/
│   └── advertisement-pages/    # 管理后台广告相关页面
├── miniprogram/                # 微信小程序
│   ├── pages/                  # 业务页面
│   ├── components/             # 自定义组件
│   ├── utils/                  # 工具函数
│   └── ec-canvas/              # ECharts 图表组件
├── sql/                        # 数据库脚本
└── doc/                        # 项目文档
```

> 本项目基于开源框架 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 二次开发，
> 仅展示本人实际参与的业务模块与定制化开发内容。

## 关于我

- **姓名**：王浩
- **学校**：河北工程技术学院 · 区块链工程（2027 届）
- **方向**：Web 全栈开发
- **邮箱**：312607103@qq.com

> 欢迎各位面试官 / HR 通过 Issue 或邮件联系我。
