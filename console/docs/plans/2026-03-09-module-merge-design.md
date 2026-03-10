# Hub-Toolkit 模块合并与 Commons 精简设计方案

## 1. 背景

console/backend 项目当前包含三个模块：
- **commons**（257 Java 文件）：基础设施 + 业务代码混合
- **toolkit**（562 Java 文件）：业务库，提供工作流/知识库/模型/Bot 等功能
- **hub**（307 Java 文件）：Spring Boot 主应用

依赖链：`commons` ← `toolkit` ← `hub`

原先由两个团队协作，现在只由一人维护，多模块结构增加了不必要的复杂度。

## 2. 目标

- 合并 hub 和 toolkit 为一个主应用模块（保留 hub 名称）
- 大幅精简 commons，只保留纯基础设施
- 最终结构：`commons` ← `hub`（两模块，单向依赖）

## 3. 目标架构

```
console/backend/
├── pom.xml              (父 pom，两个子模块)
├── commons/             (纯基础设施模块)
│   └── com.iflytek.astron.console.commons
│       ├── annotation/     (@RateLimit, @SpacePreAuth, @EnterprisePreAuth)
│       ├── aspect/         (RateLimitAspect, SpaceAuthAspect, EnterpriseAuthAspect)
│       ├── config/         (JwtClaimsFilter, 安全配置)
│       ├── constant/       (ResponseEnum, RedisKeyConstant)
│       ├── exception/      (BusinessException)
│       ├── response/       (ApiResult)
│       ├── util/           (I18nUtil, S3ClientUtil, SseEmitterUtil 等)
│       └── resources/      (messages_*.properties)
│
└── hub/                 (主应用模块，包含所有业务代码)
    └── com.iflytek.astron.console.hub
        ├── controller/     (按功能域分子包)
        ├── service/        (按功能域分子包)
        ├── mapper/         (按功能域分子包)
        ├── entity/         (entity/DTO/VO)
        ├── config/         (合并后的配置类)
        ├── enums/
        ├── event/
        └── HubApplication.java
```

## 4. 执行步骤

### Step 1：物理合并 toolkit → hub（保留原包名）

- 将 toolkit/src/main/java 下所有代码复制到 hub/src/main/java（保留 .toolkit 包名）
- 将 toolkit/src/main/resources 下的配置（application-toolkit.yml、mapper XML）合并到 hub/src/main/resources
- 将 toolkit/src/test 下的测试代码复制到 hub/src/test
- 修改 hub 的 pom.xml：移除对 toolkit 的依赖，合并 toolkit 独有的依赖
- 修改根 pom.xml：移除 toolkit 模块声明
- 删除 toolkit 目录
- **验证：`mvn compile` 通过**

### Step 2：统一包名 .toolkit → .hub

- 将 hub/src 下所有 `com.iflytek.astron.console.toolkit` 重命名为 `com.iflytek.astron.console.hub`
- 修复所有 import 引用
- 处理类名冲突：同名类需合并或重命名
- 修复 mapper XML 中的 namespace、resultType 引用
- 修复 application.yml 中的包扫描路径
- **验证：`mvn compile` 通过**

### Step 3：commons 业务代码迁入 hub

- 将 commons 中 33 个 service 接口 + 29 个实现迁入 hub，包名改为 .hub.service
- 将 commons 中业务相关的 entity、DTO、request、enums、event、listener、mapper 迁入 hub
- 修复所有 import 引用
- **验证：`mvn compile` 通过**

### Step 4：精简 commons

- 确认 commons 只剩基础设施代码
- 删除已迁走的空包和空目录
- 精简 commons 的 pom.xml，移除业务依赖（如 MyBatis-Plus）
- **验证：`mvn compile` 通过，`mvn test` 通过**

## 5. 冲突处理策略

### 类名冲突
- Step 2 前由 agent 扫描 hub 和 toolkit 的类名，列出冲突项
- 功能相同的类合并为一个；功能不同的加前缀区分
- 重点关注 config 包下的配置类（MyBatisPlusConfig、WebConfig、异常处理器等）

### 配置文件合并
- application-toolkit.yml 内容合并到 hub 的 application.yml（或通过 spring.profiles.include 引入）
- mapper XML 放到同一 resources/mapper/ 目录下，按子目录区分
- Flyway 迁移脚本保留在 hub 中不动

### Spring Bean 冲突
- hub 的 ChatDataServiceImpl 覆盖 commons 同名实现，迁移后确认 Bean 注册正确
- 检查 @ConditionalOnMissingBean 等条件注解

### pom.xml 依赖合并
- toolkit 独有依赖（EasyExcel、JOOQ、iFlytek SDK、OkHttp SSE 等）合并到 hub
- 去重，版本冲突以较新版本为准

## 6. 决策记录

| 决策项 | 选择 |
|--------|------|
| 最终模块结构 | commons + hub（两模块） |
| 主模块命名 | hub（保留原名） |
| 包名策略 | 统一为 com.iflytek.astron.console.hub |
| commons 保留范围 | 基础设施全集：异常/错误码 + ApiResult + 常量 + i18n + 注解/AOP + 安全配置 + 工具类 |
| 执行策略 | 分步：物理合并 → 统一包名 → 迁移 commons 业务代码 → 精简 commons |
| 包结构组织 | 按功能域分包（保持现有风格） |
| 执行方案 | 方案 A：自底向上合并 |
