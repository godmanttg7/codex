# 公厕管理反馈系统（SpringBoot + Vue3 + MySQL + MyBatis）

> 严格技术栈：**SpringBoot + Vue3 + MySQL + MyBatis**。

## 1. 环境要求
- JDK 17+
- Maven 3.9+
- MySQL 8.x
- Node.js 18+

## 2. 初始化数据库
先登录 MySQL 后执行：

```sql
SOURCE /你的项目路径/backend/schema.sql;
```

默认库名：`toilet_manage`。

## 3. 后端启动（SpringBoot）
```bash
cd backend
mvn spring-boot:run
```

后端地址：`http://localhost:8080`

### 后端自检
```bash
curl http://localhost:8080/api/feedback/stats
```
如果返回 JSON（哪怕是空统计），表示后端可用。

## 4. 前端启动（Vue3）
```bash
cd frontend
npm install
npm run dev
```

前端地址：`http://localhost:5173`

> 已在 `vite.config.js` 中配置 `host: 0.0.0.0`，局域网/远程环境也能访问。

## 5. API 列表
- `POST /api/feedback` 提交反馈
- `GET /api/feedback/stats` 获取统计
- `GET /api/feedback/latest?limit=10` 获取最新反馈

## 6. 常见“打不开”排查
1. **前端能打开但数据不显示**：通常是后端没启动或 MySQL 没连上。先执行后端自检 `curl`。
2. **5173 端口打不开**：确认前端是否打印 `VITE ready`，并检查端口是否被占用。
3. **后端报数据库连接错误**：检查 `backend/src/main/resources/application.yml` 的 MySQL 用户名、密码、端口。
4. **跨设备访问前端失败**：确保访问 `http://你的IP:5173`，并放通防火墙。
