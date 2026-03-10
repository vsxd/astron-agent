# 合并后清理与规范化实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 将 toolkit 原生代码全面向 hub 规范看齐，消除运行时问题、统一响应/异常/i18n 体系、统一 DI 风格、合并配置文件。

**Architecture:** 先验证运行时启动，修复阻塞问题。然后自底向上：先补齐 i18n 消息和 ResponseEnum 映射，再批量替换 Result→ApiResult 和异常类，最后统一 DI 风格和合并配置。每步验证编译通过。

**Tech Stack:** Java 21, Spring Boot 3.5.4, MyBatis-Plus 3.5.7, Maven multi-module

---

## Task 1: 运行时启动验证与修复

**Goal:** 使用 .env.dev 启动 hub 模块，发现并修复所有运行时错误。

**Files:**
- Reference: `/Users/xudongsun/github/astron-agent/.env.dev`
- Modify: hub 模块中导致启动失败的文件（具体文件在启动后根据错误确定）

**Step 1: 尝试启动 hub 模块**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
# 使用 .env.dev 环境变量启动
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run -pl hub -Dspring-boot.run.jvmArguments="-Dspring.config.import=optional:file:../../.env.dev[.properties]"
```

注意：启动后观察日志，记录所有错误。常见问题：
- Bean 冲突（同名 Bean 定义）
- 类型别名冲突（MyBatis @Alias）
- 配置缺失或冲突
- 循环依赖

**Step 2: 逐个修复启动错误**

根据错误日志修复问题。每修复一个问题后重新启动验证。

**Step 3: 确认启动成功**

Expected: 应用正常启动，无 Bean 冲突或缺失，日志中无 ERROR 级别输出。

**Step 4: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "fix: resolve Spring Boot runtime startup issues"
```

---

## Task 2: 补齐 ResultStatus/CustomExceptionCode 到 ResponseEnum 的映射

**Goal:** 将 toolkit 的错误码映射到 hub 的 ResponseEnum 体系，补齐 i18n 消息。

**Files:**
- Modify: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/constant/ResponseEnum.java`
- Modify: `console/backend/commons/src/main/resources/messages_zh.properties`
- Modify: `console/backend/commons/src/main/resources/messages_en.properties`
- Reference: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatus.java`（44 个错误码）
- Reference: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatusEN.java`（44 个错误码）
- Reference: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/CustomExceptionCode.java`（200+ 个错误码）

**Step 1: 分析 ResultStatus 中的错误码**

读取 ResultStatus.java 和 ResultStatusEN.java，列出所有 44 个错误码及其中英文消息。
检查哪些已经在 ResponseEnum 中有对应项（通过语义匹配），哪些需要新增。

**Step 2: 分析 CustomExceptionCode 中的错误码**

读取 CustomExceptionCode.java，列出所有 200+ 个错误码。
检查哪些已经在 ResponseEnum 中有对应项，哪些需要新增。

**Step 3: 在 ResponseEnum 中新增缺失的错误码**

为每个缺失的错误码添加枚举值，使用 messageKey 模式：
```java
// 示例：ResultStatus.PARAM_ERROR(-1, "参数错误") 映射为
TOOLKIT_PARAM_ERROR(60100, "error.toolkit.param.error"),
```

命名规范：
- 来自 ResultStatus 的：直接映射到已有的或新增 `TOOLKIT_*` 前缀
- 来自 CustomExceptionCode 的：按业务域分组（KNOWLEDGE_*, WORKFLOW_*, BOT_*, CHAT_* 等）

**Step 4: 在 messages_zh.properties 和 messages_en.properties 中添加对应消息**

```properties
# messages_zh.properties
error.toolkit.param.error=参数错误

# messages_en.properties
error.toolkit.param.error=Parameter error
```

**Step 5: 验证编译**

Run: `cd console/backend && mvn compile`
Expected: BUILD SUCCESS

**Step 6: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "feat: add ResponseEnum mappings for toolkit error codes and i18n messages"
```

---

## Task 3: 替换 Result 为 ApiResult — Controller 层

**Goal:** 将所有 Controller 中的 `Result<T>` 返回值替换为 `ApiResult<T>`。

**Files:**
- Modify: 约 30+ 个 Controller 文件（使用 Result 的 controller）
- Reference: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/response/ApiResult.java`

**Step 1: 识别所有使用 Result 的 Controller**

```bash
grep -rl "Result<\|Result\.success\|Result\.failure\|Result\.from" console/backend/hub/src/main/java --include="*Controller*.java" | sort
```

**Step 2: 批量替换 Controller 中的 Result 用法**

对每个 Controller：
1. 将返回类型 `Result<T>` 改为 `ApiResult<T>`
2. 将 `Result.success(data)` 改为 `ApiResult.success(data)`
3. 将 `Result.failure(ResultStatus.XXX)` 改为 `ApiResult.error(ResponseEnum.XXX)`
4. 将 `Result.from(zhStatus, enStatus, data)` 改为 `ApiResult.of(ResponseEnum.XXX, data)`
5. 更新 import 语句
6. 移除 `@ResponseResultBody` 注解（因为现在显式返回 ApiResult）

**Step 3: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 4: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: replace Result with ApiResult in all controllers"
```

---

## Task 4: 替换 Result 为 ApiResult — Service 层

**Goal:** 将所有 Service 中的 `Result<T>` 返回值替换为 `ApiResult<T>`，或改为直接返回数据/抛异常。

**Files:**
- Modify: 约 30+ 个 Service 文件

**Step 1: 识别所有使用 Result 的 Service**

```bash
grep -rl "Result<\|Result\.success\|Result\.failure\|Result\.from" console/backend/hub/src/main/java --include="*Service*.java" --include="*Handler*.java" | sort
```

**Step 2: 替换策略**

对每个 Service/Handler：
- 如果方法返回 `Result<T>`：改为直接返回 `T`，失败时 `throw new BusinessException(ResponseEnum.XXX)`
- 如果方法内部构造 `Result` 并返回：改为返回数据或抛异常
- 更新所有调用方

**Step 3: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 4: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: replace Result with ApiResult/exceptions in services"
```

---

## Task 5: 删除 toolkit 响应/异常/i18n 遗留类

**Goal:** 删除不再使用的 toolkit 响应体系和异常类。

**Files:**
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/Result.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatus.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatusEN.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/anno/ResponseResultBody.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/aop/ResponseResultBodyAdvice.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/handler/language/LanguageContext.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/exception/CustomException.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/exception/OpenApiException.java`
- Delete: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/CustomExceptionCode.java`

**Step 1: 确认无残留引用**

```bash
grep -r "import.*hub\.common\.Result\b" console/backend/hub/src/ || echo "Clean"
grep -r "import.*ResultStatus" console/backend/hub/src/ || echo "Clean"
grep -r "import.*ResultStatusEN" console/backend/hub/src/ || echo "Clean"
grep -r "import.*ResponseResultBody" console/backend/hub/src/ || echo "Clean"
grep -r "import.*LanguageContext" console/backend/hub/src/ || echo "Clean"
grep -r "import.*CustomException\b" console/backend/hub/src/ || echo "Clean"
grep -r "import.*OpenApiException" console/backend/hub/src/ || echo "Clean"
grep -r "import.*CustomExceptionCode" console/backend/hub/src/ || echo "Clean"
```

如果有残留引用，先修复再删除。

**Step 2: 删除文件**

```bash
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/Result.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatus.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/ResultStatusEN.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/anno/ResponseResultBody.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/aop/ResponseResultBodyAdvice.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/handler/language/LanguageContext.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/exception/CustomException.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/config/exception/OpenApiException.java
rm -f console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common/CustomExceptionCode.java
```

**Step 3: 清理空目录**

```bash
find console/backend/hub/src/main/java/com/iflytek/astron/console/hub/common -type d -empty -delete
find console/backend/hub/src/main/java/com/iflytek/astron/console/hub/handler/language -type d -empty -delete
```

**Step 4: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 5: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: remove toolkit Result/ResultStatus/CustomException legacy classes"
```

---

## Task 6: 合并 application-toolkit.yml 到 application.yml

**Goal:** 将 application-toolkit.yml 的配置合并到 application.yml，删除 application-toolkit.yml。

**Files:**
- Modify: `console/backend/hub/src/main/resources/application.yml`
- Delete: `console/backend/hub/src/main/resources/application-toolkit.yml`

**Step 1: 读取两个配置文件**

读取 application.yml 和 application-toolkit.yml，识别：
- 重复配置项（保留 application.yml 的版本）
- toolkit 独有配置项（需要合并到 application.yml）

**Step 2: 合并配置**

将 application-toolkit.yml 中的独有配置合并到 application.yml 的对应位置：
- `pagehelper` 配置
- `api.*` 端点配置
- `biz.*` 业务配置
- `xfyun.*` 认证配置
- `mcp-server.*` 配置
- `task.*` 任务调度配置
- `common.*` 通用配置

**Step 3: 移除 spring.config.import 引用**

从 application.yml 中删除：
```yaml
spring.config.import: optional:classpath:application-toolkit.yml
```

**Step 4: 删除 application-toolkit.yml**

```bash
rm console/backend/hub/src/main/resources/application-toolkit.yml
```

**Step 5: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 6: Commit**

```bash
git add -A console/backend/hub/src/main/resources/
git commit --no-verify -m "refactor: merge application-toolkit.yml into application.yml"
```

---

## Task 7: 统一 DI 风格 — @Autowired → @RequiredArgsConstructor

**Goal:** 将所有 @Autowired 字段注入改为 @RequiredArgsConstructor 构造器注入。

**Files:**
- Modify: 约 378 个使用 @Autowired 的 Java 文件

**Step 1: 识别所有使用 @Autowired 的文件**

```bash
grep -rl "@Autowired" console/backend/hub/src/main/java --include="*.java" | wc -l
```

**Step 2: 批量替换策略**

对每个文件：
1. 在类上添加 `@RequiredArgsConstructor`（如果没有）
2. 将 `@Autowired private XxxService xxxService;` 改为 `private final XxxService xxxService;`
3. 移除 `@Autowired` import（如果不再使用）
4. 添加 `import lombok.RequiredArgsConstructor;`（如果没有）

注意事项：
- `@Autowired` 在 setter 方法上的用法需要特殊处理
- `@Autowired(required = false)` 需要改为 `@Nullable` + 构造器参数
- 有些类可能同时使用 `@Autowired` 和手动构造器，需要合并
- `@Configuration` 类中的 `@Autowired` 可能需要保留（某些 Spring 配置场景）

**Step 3: 分批处理**

按包分批处理，每批验证编译：
1. controller 包
2. service 包
3. config 包
4. handler 包
5. 其他包

**Step 4: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 5: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: unify DI style to constructor injection with @RequiredArgsConstructor"
```

---

## Task 8: 最终验证

**Goal:** 全面验证重构后的项目。

**Step 1: 全量编译**

Run: `cd console/backend && mvn clean compile`
Expected: BUILD SUCCESS

**Step 2: 运行全部测试**

Run: `cd console/backend && mvn test`
Expected: 所有测试通过（除已知的 PromptServiceTest 9 个预存失败）

**Step 3: 验证无遗留 toolkit 模式**

```bash
# 无 Result 类引用
grep -r "import.*hub\.common\.Result\b" console/backend/hub/src/ || echo "Clean"
# 无 ResultStatus 引用
grep -r "ResultStatus\b" console/backend/hub/src/main/java/ || echo "Clean"
# 无 @Autowired 字段注入
grep -r "@Autowired" console/backend/hub/src/main/java/ | grep -v "// " | wc -l
# 预期：0 或极少数特殊情况
# 无 application-toolkit.yml
ls console/backend/hub/src/main/resources/application-toolkit.yml 2>/dev/null || echo "Clean"
```

**Step 4: 启动验证**

使用 .env.dev 启动 hub 模块，确认无运行时错误。

**Step 5: 最终提交**

```bash
git add -A
git commit --no-verify -m "refactor: complete post-merge cleanup and standardization"
```
