# 智慧公厕管理系统（SpringBoot + Vue3 + 达梦DM8/MyBatis）

## 本次重点修复
- 达梦初始化脚本改为 `backend/init_dm.sql`，不再硬编码自增 ID，所有种子数据外键均通过公厕编码/设施名称查询生成。
- 后端配置不再提交明文数据库密码和 JWT 密钥，统一从环境变量读取。
- 报修工单编号由服务端自动生成，前端不需要传 `orderNo`，避免被 `@Valid` 拦截。
- 报修工单创建、状态更新、删除都会同步设施状态，并写入站内消息。
- 设施、工单、消息接口和前端页面已对齐，前端可查看设施状态、工单状态和站内消息。
- MyBatis SQL 已替换 MySQL 的 `NOW()` / `LIMIT`，使用达梦兼容的 `SYSDATE` / `OFFSET ... FETCH`。

## 功能范围
- 公众反馈：评分、意见提交，查看最新反馈与平均评分统计。
- 公厕基础信息管理：新增/查看公厕档案。
- 设施状态管理：展示设施状态，工单流转时自动同步 `NORMAL/FAULT/REPAIR`。
- 设施报修工单：创建工单、状态流转（PENDING/REPAIRING/CHECKING/FINISHED/CANCELED）、删除工单。
- 耗材预警看板：查看低库存预警。
- 站内消息：展示工单创建、维修中、待验收、验收通过、取消/删除等通知。
- 运行检测：前端端口检测（应为5173）+ 后端健康检查（`/api/health`）。

## 达梦 DM8 初始化
1. 确认达梦服务运行，并准备好目标模式（默认脚本使用 `SYSDBA`）。
2. 执行初始化脚本：`disql SYSDBA/你的密码@localhost:5236 \`backend/init_dm.sql\``。
3. 如果你的模式不是 `SYSDBA`，先修改 `backend/init_dm.sql` 顶部的 `SET SCHEMA`。

## 后端启动
```bash
cd backend
export DB_URL='jdbc:dm://localhost:5236/TOILET_DB'
export DB_USERNAME='SYSDBA'
export DB_PASSWORD='你的达梦密码'
export JWT_SECRET='请换成至少32位的随机字符串'
mvn spring-boot:run
```

> 达梦 JDBC 驱动通常不在 Maven Central。若本机 Maven 仓库没有达梦驱动，请先按达梦官方文档将 DM JDBC 驱动安装到本地 Maven 仓库，或通过运行环境 classpath 提供驱动。

## 前端启动
```bash
cd frontend
npm install
npm run dev
```
打开 `http://localhost:5173`。

> 前端通过 Vite 代理 `/api` 到 `http://127.0.0.1:8080`，避免跨域和硬编码后端地址。

## 主要接口
- `GET /api/health`
- `POST /api/feedback`
- `GET /api/feedback/latest`
- `GET /api/feedback/stats`
- `GET /api/manage/dashboard`
- `GET/POST /api/manage/toilets`
- `GET /api/manage/facilities`
- `GET/POST /api/manage/repairs`
- `PATCH /api/manage/repairs/{id}/status`
- `DELETE /api/manage/repairs/{id}`
- `GET /api/manage/consumables`
- `GET /api/manage/messages`
