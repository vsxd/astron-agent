# 合并后清理与规范化设计

**Goal:** 将 toolkit 原生代码全面向 hub 规范看齐，消除运行时问题和代码风格差异。

**Scope:** hub 模块中原 toolkit 来源的代码，以及相关配置文件。

---

## 1. 运行时验证与修复

使用 `.env.dev` 启动 hub 模块，发现并修复所有 Spring Boot 运行时错误（Bean 冲突、配置缺失、类型别名等）。

## 2. 响应体系统一 → ApiResult

- toolkit 的 `Result`/`ResultStatus`/`ResultStatusEN` 全部替换为 hub 的 `ApiResult`/`ResponseEnum`
- 移除 `@ResponseResultBody` 注解及 `ResponseResultBodyAdvice`，所有 controller 显式返回 `ApiResult<T>`
- 删除 `Result`、`ResultStatus`、`ResultStatusEN`、`LanguageContext` 等 toolkit 响应相关类

## 3. 异常体系统一 → BusinessException

- toolkit 的 `CustomException`/`OpenApiException` 统一为 hub 的 `BusinessException`
- 确保 `GlobalExceptionHandler` 覆盖所有异常类型

## 4. i18n 外部化

- `ResultStatus`/`ResultStatusEN` 中的硬编码消息提取到 `messages_zh.properties` / `messages_en.properties`
- 统一使用 `I18nUtil.getMessage(key)` 获取消息
- 删除 `LanguageContext`、`ResultStatusEN` 等硬编码 i18n 机制

## 5. DI 风格统一 → 构造器注入

- 所有 `@Autowired` 字段注入改为 `@RequiredArgsConstructor` + `private final`

## 6. 配置合并

- `application-toolkit.yml` 内容合并到 `application.yml`
- 去除重复配置项
- 删除 `application-toolkit.yml` 及 `spring.config.import` 引用

## 决策记录

| 决策 | 选择 |
|------|------|
| 清理范围 | 全面对齐 |
| 响应体系 | 统一到 ApiResult（hub 原生） |
| i18n | 全部外部化到 properties 文件 |
| DI 风格 | 统一为 @RequiredArgsConstructor |
| 配置文件 | 合并到 application.yml 并删除 toolkit 配置 |
