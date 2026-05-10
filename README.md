# 公厕管理反馈系统（SpringBoot + Vue3 + MySQL + MyBatis）

本项目包含两个子模块：
- `backend`：SpringBoot + MyBatis + MySQL 后端接口
- `frontend`：Vue3 前端反馈与统计页面

## 功能
- 用户提交公厕使用反馈（卫生、排队、气味评分与文字意见）
- 后台统计反馈总数与平均分
- 展示最新反馈列表

## 启动步骤

### 1) 准备数据库
执行：`backend/schema.sql`

### 2) 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 3) 启动前端
```bash
cd frontend
npm install
npm run dev
```

前端地址：`http://localhost:5173`
后端地址：`http://localhost:8080`

## API
- `POST /api/feedback` 提交反馈
- `GET /api/feedback/stats` 获取统计
- `GET /api/feedback/latest?limit=10` 获取最新反馈

页面风格参考了你给出的清爽卡片式布局方向，并在 Vue 中实现了可用的基础版本。
