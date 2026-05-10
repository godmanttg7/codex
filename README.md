# 智慧公厕管理系统（SpringBoot + Vue3 + MySQL + MyBatis）

## 功能范围（扩展版）
- 公众反馈：评分、意见提交，查看最新反馈与平均评分统计。
- 公厕基础信息管理：新增/查看公厕档案。
- 设施报修工单：创建工单、状态流转（PENDING/REPAIRING/CHECKING/FINISHED）。
- 耗材预警看板：查看低库存预警。
- 管理驾驶舱：公厕数、反馈数、待处理工单数、低库存数。

## 启动步骤
1. 创建数据库并导入：`mysql -uroot -proot < backend/schema.sql`
2. 启动后端：`cd backend && mvn spring-boot:run`
3. 启动前端：`cd frontend && npm install && npm run dev`
4. 打开 `http://localhost:5173`

## 主要接口
- `POST /api/feedback`
- `GET /api/feedback/latest`
- `GET /api/feedback/stats`
- `GET /api/manage/dashboard`
- `GET/POST /api/manage/toilets`
- `GET/POST /api/manage/repairs`
- `PATCH /api/manage/repairs/{id}/status`
- `GET /api/manage/consumables`
