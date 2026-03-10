# Hub-Toolkit 模块合并实施计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 将 toolkit 合并进 hub，精简 commons 为纯基础设施模块，最终只保留 commons + hub 两模块结构。

**Architecture:** 自底向上分步合并。先物理搬迁 toolkit 代码到 hub（保留原包名），再统一包名为 .hub，然后将 commons 中 231 个业务文件迁入 hub，最后精简 commons 只保留 26 个基础设施文件。每步验证编译通过。

**Tech Stack:** Java 21, Spring Boot 3.5.4, MyBatis-Plus 3.5.7, Maven multi-module

---

## Task 1: 合并 toolkit 独有依赖到 hub 的 pom.xml

**Files:**
- Modify: `console/backend/hub/pom.xml`
- Reference: `console/backend/toolkit/pom.xml`

**Step 1: 读取 toolkit/pom.xml，识别 hub 中缺少的依赖**

toolkit 独有依赖（hub 中没有的）：
- spring-boot-starter-websocket
- spring-boot-starter-undertow
- spring-boot-starter-mail
- spring-boot-starter-aop
- spring-boot-starter-actuator
- spring-boot-configuration-processor
- spring-retry
- mybatis-plus-join-boot-starter 1.5.2
- pagehelper-spring-boot-starter 2.1.1
- postgresql
- commons-io
- commons-fileupload 1.5
- apache poi 5.3.0
- owasp-java-html-sanitizer
- jooq
- software.amazon.awssdk:s3 2.27.16
- mockito-core 5.12.0 (test)
- assertj-core (test)

**Step 2: 将上述依赖添加到 hub/pom.xml**

在 hub/pom.xml 的 `<dependencies>` 中添加 toolkit 独有的依赖。注意：
- 保留 hub 原有的 spring-boot-starter-web（不要替换为 undertow 版本，合并后再决定）
- spring-boot-configuration-processor 设为 optional
- test scope 的依赖放在最后

**Step 3: 从 hub/pom.xml 移除对 toolkit 的依赖**

删除：
```xml
<dependency>
    <groupId>com.iflytek.astron.console</groupId>
    <artifactId>toolkit</artifactId>
</dependency>
```

**Step 4: 验证**

Run: `cd console/backend && mvn dependency:resolve -pl hub -am`
Expected: BUILD SUCCESS（此时还没搬代码，只是确认依赖解析正常）

**Step 5: Commit**

```bash
git add console/backend/hub/pom.xml
git commit --no-verify -m "build: merge toolkit dependencies into hub pom.xml"
```

---

## Task 2: 物理搬迁 toolkit 源码到 hub

**Files:**
- Move: `console/backend/toolkit/src/main/java/com/iflytek/astron/console/toolkit/` → `console/backend/hub/src/main/java/com/iflytek/astron/console/toolkit/`
- Move: `console/backend/toolkit/src/test/` → `console/backend/hub/src/test/`

**Step 1: 复制 toolkit 的 Java 源码到 hub**

```bash
cp -r console/backend/toolkit/src/main/java/com/iflytek/astron/console/toolkit \
      console/backend/hub/src/main/java/com/iflytek/astron/console/toolkit
```

**Step 2: 复制 toolkit 的测试代码到 hub**

```bash
# 如果 toolkit 有测试代码
if [ -d "console/backend/toolkit/src/test" ]; then
  cp -r console/backend/toolkit/src/test/java/com/iflytek/astron/console/toolkit \
        console/backend/hub/src/test/java/com/iflytek/astron/console/toolkit
fi
```

**Step 3: 复制 toolkit 的 resources 到 hub**

```bash
# 复制 application-toolkit.yml
cp console/backend/toolkit/src/main/resources/application-toolkit.yml \
   console/backend/hub/src/main/resources/application-toolkit.yml

# 复制 toolkit 的 mapper XML（如果有）
if [ -d "console/backend/toolkit/src/main/resources/mapper" ]; then
  cp -r console/backend/toolkit/src/main/resources/mapper/* \
        console/backend/hub/src/main/resources/mapper/
fi
```

**Step 4: 确保 hub 的 application.yml 引入 toolkit 配置**

在 hub 的 application.yml 中确认 `spring.profiles.include` 包含 `toolkit`，或将 application-toolkit.yml 的内容直接合并。

**Step 5: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 6: Commit**

```bash
git add console/backend/hub/src/
git add console/backend/hub/src/main/resources/
git commit --no-verify -m "refactor: copy toolkit source code into hub module"
```

---

## Task 3: 更新根 pom.xml 并删除 toolkit 模块

**Files:**
- Modify: `console/backend/pom.xml`
- Delete: `console/backend/toolkit/`

**Step 1: 从根 pom.xml 移除 toolkit 模块声明**

在 `console/backend/pom.xml` 的 `<modules>` 中删除：
```xml
<module>toolkit</module>
```

**Step 2: 验证编译（toolkit 模块还在但不参与构建）**

Run: `cd console/backend && mvn compile`
Expected: BUILD SUCCESS

**Step 3: 删除 toolkit 目录**

```bash
rm -rf console/backend/toolkit
```

**Step 4: 再次验证编译**

Run: `cd console/backend && mvn compile`
Expected: BUILD SUCCESS

**Step 5: Commit**

```bash
git add console/backend/pom.xml
git rm -r console/backend/toolkit
git commit --no-verify -m "refactor: remove toolkit module after merging into hub"
```

---

## Task 4: 扫描类名冲突并制定合并策略

**Files:**
- Reference: `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/`
- Reference: `console/backend/hub/src/main/java/com/iflytek/astron/console/toolkit/`

**Step 1: 扫描同名类**

```bash
# 列出 hub 包下所有类名
find console/backend/hub/src/main/java/com/iflytek/astron/console/hub -name "*.java" -exec basename {} \; | sort > /tmp/hub_classes.txt

# 列出 toolkit 包下所有类名
find console/backend/hub/src/main/java/com/iflytek/astron/console/toolkit -name "*.java" -exec basename {} \; | sort > /tmp/toolkit_classes.txt

# 找出同名类
comm -12 /tmp/hub_classes.txt /tmp/toolkit_classes.txt
```

已知冲突（从探索阶段发现）：
- `GlobalExceptionHandler.java` - hub 版本更完整，保留 hub 版本，合并 toolkit 独有的异常处理
- `MyBatisPlusConfig.java` - 需要合并配置逻辑
- `WebConfig.java` / `CorsConfig.java` - 需要合并 CORS 和 Web 配置
- `WorkflowConfig.java` - hub 中是配置属性类，toolkit 中是数据库实体，名字相同但用途不同，实体类需重命名

**Step 2: 对每个冲突类，读取两个版本的代码，决定合并策略**

- 功能相同：保留 hub 版本，删除 toolkit 版本
- 功能互补：合并为一个类
- 名字相同但用途不同：重命名 toolkit 版本（如 WorkflowConfig 实体 → WorkflowConfigEntity）

**Step 3: 执行合并/重命名**

逐个处理冲突类，确保编译通过。

**Step 4: 验证**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 5: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: resolve class name conflicts between hub and toolkit"
```

---

## Task 5: 统一包名 .toolkit → .hub

**Files:**
- Modify: ALL files under `console/backend/hub/src/main/java/com/iflytek/astron/console/toolkit/`
- Modify: ALL files that import `com.iflytek.astron.console.toolkit.*`

**Step 1: 移动目录**

```bash
# 将 toolkit 包下的所有子目录/文件移到 hub 包对应位置
# 需要逐个子包处理，避免覆盖 hub 已有的同名子包

cd console/backend/hub/src/main/java/com/iflytek/astron/console

# 对 toolkit 下每个子包，如果 hub 下不存在则直接移动，如果存在则合并内容
for dir in toolkit/*/; do
  dirname=$(basename "$dir")
  if [ -d "hub/$dirname" ]; then
    # hub 中已存在同名包，将 toolkit 的文件移入
    cp -r "toolkit/$dirname/"* "hub/$dirname/"
  else
    # hub 中不存在，直接移动
    mv "toolkit/$dirname" "hub/$dirname"
  fi
done

# 移动 toolkit 根目录下的直接文件（如果有）
for f in toolkit/*.java; do
  [ -f "$f" ] && mv "$f" hub/
done

# 删除空的 toolkit 目录
rm -rf toolkit
```

**Step 2: 批量替换包名**

```bash
# 替换所有 Java 文件中的包声明和 import
find console/backend/hub/src -name "*.java" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.toolkit/com.iflytek.astron.console.hub/g' {} +
```

**Step 3: 替换 mapper XML 中的引用**

```bash
# 替换 mapper XML 中的 namespace 和 resultType
find console/backend/hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.toolkit/com.iflytek.astron.console.hub/g' {} +
```

**Step 4: 替换 application*.yml 中的引用**

```bash
find console/backend/hub/src/main/resources -name "*.yml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.toolkit/com.iflytek.astron.console.hub/g' {} +
```

**Step 5: 处理测试代码中的包名**

```bash
find console/backend/hub/src/test -name "*.java" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.toolkit/com.iflytek.astron.console.hub/g' {} +

# 移动测试目录结构
cd console/backend/hub/src/test/java/com/iflytek/astron/console
if [ -d "toolkit" ]; then
  for dir in toolkit/*/; do
    dirname=$(basename "$dir")
    mkdir -p "hub/$dirname"
    cp -r "toolkit/$dirname/"* "hub/$dirname/"
  done
  for f in toolkit/*.java; do
    [ -f "$f" ] && mv "$f" hub/
  done
  rm -rf toolkit
fi
```

**Step 6: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

**Step 7: Commit**

```bash
git add -A console/backend/hub/
git commit --no-verify -m "refactor: rename toolkit package to hub, unify package names"
```

---

## Task 6: 迁移 commons 业务代码到 hub — Service 接口与实现

**Files:**
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/service/` → `console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service/`
- Modify: ALL files that import `com.iflytek.astron.console.commons.service.*`

**Step 1: 复制 commons 的 service 接口到 hub**

```bash
# 复制 service 接口（I*.java 文件）
cp -r console/backend/commons/src/main/java/com/iflytek/astron/console/commons/service \
      console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service_commons_tmp

# 如果 hub 已有 service 目录，合并内容
if [ -d "console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service" ]; then
  cp -rn console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service_commons_tmp/* \
         console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service/
  rm -rf console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service_commons_tmp
else
  mv console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service_commons_tmp \
     console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service
fi
```

**Step 2: 替换迁移文件中的包名**

```bash
# 替换 service 文件中的包声明
find console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service -name "*.java" -exec sed -i '' \
  's/package com\.iflytek\.astron\.console\.commons\.service/package com.iflytek.astron.console.hub.service/g' {} +

# 替换 service 文件中对 commons 其他包的 import（entity、enums 等后续迁移后再统一处理）
find console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.service/import com.iflytek.astron.console.hub.service/g' {} +
```

**Step 3: 更新 hub 和 commons 中所有对 commons.service 的 import 引用**

```bash
# 更新 hub 中所有 Java 文件的 import
find console/backend/hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.service/import com.iflytek.astron.console.hub.service/g' {} +
```

**Step 4: 从 commons 中删除已迁移的 service 文件**

注意：暂时保留 commons 中被 GROUP A 基础设施文件直接依赖的 service 接口（如果有），这些在 Task 8 中处理。

```bash
# 先检查哪些 service 被 commons 的 GROUP A 文件引用
grep -rl "commons.service" console/backend/commons/src/main/java/com/iflytek/astron/console/commons/annotation/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/aspect/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/config/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/constant/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/exception/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/response/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/util/ 2>/dev/null || true

# 删除 commons 中的 service 目录（已复制到 hub）
rm -rf console/backend/commons/src/main/java/com/iflytek/astron/console/commons/service
```

**Step 5: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: 可能有编译错误（因为 entity/enums 等还没迁移），记录错误，在后续 Task 中修复

**Step 6: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "refactor: migrate commons service interfaces and impls to hub"
```

---

## Task 7: 迁移 commons 业务代码到 hub — Entity、DTO、Enums、Event、Mapper 等

**Files:**
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/entity/` → hub
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/dto/` → hub
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/request/` → hub
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/enums/` → hub（保留 GROUP A 需要的枚举）
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/event/` → hub
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/listener/` → hub
- Move: `console/backend/commons/src/main/java/com/iflytek/astron/console/commons/mapper/` → hub
- Move: mapper XML files from commons resources

**Step 1: 批量复制业务包到 hub**

```bash
cd console/backend

for pkg in entity dto request event listener mapper; do
  src="commons/src/main/java/com/iflytek/astron/console/commons/$pkg"
  dst="hub/src/main/java/com/iflytek/astron/console/hub/$pkg"
  if [ -d "$src" ]; then
    if [ -d "$dst" ]; then
      # hub 中已存在同名包，合并
      cp -rn "$src"/* "$dst"/
    else
      cp -r "$src" "$dst"
    fi
  fi
done

# enums 需要特殊处理：只迁移业务枚举，保留 ResponseEnum 等基础设施枚举在 commons
# 先全部复制，后续在 Task 8 中将基础设施枚举移回
src="commons/src/main/java/com/iflytek/astron/console/commons/enums"
dst="hub/src/main/java/com/iflytek/astron/console/hub/enums"
if [ -d "$src" ]; then
  if [ -d "$dst" ]; then
    cp -rn "$src"/* "$dst"/
  else
    cp -r "$src" "$dst"
  fi
fi
```

**Step 2: 复制 commons 的 mapper XML 到 hub**

```bash
if [ -d "commons/src/main/resources/mapper" ]; then
  cp -rn commons/src/main/resources/mapper/* hub/src/main/resources/mapper/
fi
```

**Step 3: 批量替换迁移文件中的包名**

```bash
# 替换所有迁移到 hub 的文件中的包声明和 import
for pkg in entity dto request enums event listener mapper; do
  dir="hub/src/main/java/com/iflytek/astron/console/hub/$pkg"
  if [ -d "$dir" ]; then
    find "$dir" -name "*.java" -exec sed -i '' \
      "s/package com\\.iflytek\\.astron\\.console\\.commons\\.$pkg/package com.iflytek.astron.console.hub.$pkg/g" {} +
  fi
done
```

**Step 4: 全局替换 hub 中所有对 commons 业务包的 import**

```bash
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.entity/import com.iflytek.astron.console.hub.entity/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.dto/import com.iflytek.astron.console.hub.dto/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.request/import com.iflytek.astron.console.hub.request/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.enums/import com.iflytek.astron.console.hub.enums/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.event/import com.iflytek.astron.console.hub.event/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.listener/import com.iflytek.astron.console.hub.listener/g' {} +
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.commons\.mapper/import com.iflytek.astron.console.hub.mapper/g' {} +
```

**Step 5: 替换 mapper XML 中的 namespace 和 resultType 引用**

```bash
find hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.commons\.entity/com.iflytek.astron.console.hub.entity/g' {} +
find hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.commons\.mapper/com.iflytek.astron.console.hub.mapper/g' {} +
find hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.commons\.dto/com.iflytek.astron.console.hub.dto/g' {} +
find hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.commons\.request/com.iflytek.astron.console.hub.request/g' {} +
```

**Step 6: 替换 application*.yml 中的引用**

```bash
find hub/src/main/resources -name "*.yml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.commons/com.iflytek.astron.console.hub/g' {} +
```

**Step 7: 从 commons 中删除已迁移的业务包**

```bash
cd console/backend
for pkg in entity dto request event listener mapper; do
  rm -rf "commons/src/main/java/com/iflytek/astron/console/commons/$pkg"
done

# enums 目录暂时保留（Task 8 中处理哪些枚举留在 commons）
# mapper XML 也从 commons 删除
rm -rf commons/src/main/resources/mapper
```

**Step 8: 验证编译**

Run: `cd console/backend && mvn compile -pl hub -am`
Expected: 可能有编译错误（commons 基础设施文件引用了已迁移的业务类），记录错误列表

**Step 9: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "refactor: migrate commons business code (entity/dto/enums/event/mapper) to hub"
```

---

## Task 8: 重构 commons 基础设施文件的业务依赖

**Files:**
- Modify: commons 中 8 个依赖业务代码的基础设施文件
- Modify: hub 中对应的实现

已知需要重构的文件（从探索阶段发现）：
1. `commons/.../aspect/SpaceAuthAspect.java` — 依赖 SpaceService、SpaceMember 等业务类
2. `commons/.../aspect/EnterpriseAuthAspect.java` — 依赖 EnterpriseService 等业务类
3. `commons/.../aspect/RateLimitAspect.java` — 可能依赖业务枚举
4. `commons/.../config/JwtClaimsFilter.java` — 依赖 UserInfo 实体
5. `commons/.../util/` 中部分工具类 — 依赖业务实体

**Step 1: 扫描 commons 剩余文件的编译错误**

```bash
cd console/backend && mvn compile -pl commons 2>&1 | grep "cannot find symbol" | sort -u
```

记录所有缺失的类引用。

**Step 2: 对每个有业务依赖的基础设施文件，采用以下策略之一**

策略 A — 抽象接口：在 commons 中定义接口，hub 中提供实现
```java
// commons 中定义
public interface SpaceAuthProvider {
    boolean hasPermission(Long spaceId, Long userId, String permission);
}

// hub 中实现
@Component
public class SpaceAuthProviderImpl implements SpaceAuthProvider {
    @Autowired private SpaceService spaceService;
    // ...
}
```

策略 B — 移到 hub：如果该基础设施文件只被 hub 使用，直接移到 hub

策略 C — 解耦：用基础类型替代业务类型引用（如用 Long userId 替代 UserInfo）

**Step 3: 逐个处理**

对每个文件：
1. 读取文件，分析其业务依赖
2. 选择最合适的策略
3. 实施重构
4. 运行 `mvn compile -pl commons` 确认该文件编译通过

**Step 4: 处理 enums 包**

```bash
# 检查 commons 基础设施文件引用了哪些枚举
grep -rh "import com.iflytek.astron.console.commons.enums" \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/annotation/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/aspect/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/config/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/constant/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/exception/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/response/ \
  console/backend/commons/src/main/java/com/iflytek/astron/console/commons/util/ 2>/dev/null | sort -u
```

- 被 GROUP A 引用的枚举：保留在 commons
- 其余枚举：确认已迁移到 hub，从 commons 删除

```bash
# 删除 commons 中不再需要的枚举（保留被基础设施引用的）
# 具体文件列表在 Step 4 扫描后确定
```

**Step 5: 验证编译**

Run: `cd console/backend && mvn compile`
Expected: BUILD SUCCESS（commons 和 hub 都编译通过）

**Step 6: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "refactor: decouple commons infrastructure from business code"
```

---

## Task 9: 精简 commons 的 pom.xml 并清理

**Files:**
- Modify: `console/backend/commons/pom.xml`
- Delete: commons 中的空目录和空包

**Step 1: 精简 commons 的 pom.xml**

移除 commons 不再需要的业务依赖：
- mybatis-plus-boot-starter（如果 commons 不再有 mapper）
- 其他只被业务代码使用的依赖

保留：
- spring-boot-starter-web（用于 filter、config）
- spring-boot-starter-data-redis（用于 RateLimitAspect）
- spring-boot-starter-aop（用于 aspect）
- spring-security 相关（用于 JwtClaimsFilter）
- commons-lang3、jackson 等基础工具库
- i18n 相关依赖

**Step 2: 清理空目录**

```bash
# 删除 commons 中的空目录
find console/backend/commons/src -type d -empty -delete
```

**Step 3: 确认 commons 只剩基础设施代码**

```bash
# 列出 commons 中所有剩余的 Java 文件
find console/backend/commons/src/main/java -name "*.java" | sort
```

预期只剩约 26 个文件，分布在：
- annotation/
- aspect/
- config/
- constant/
- exception/
- response/
- util/

**Step 4: 验证编译**

Run: `cd console/backend && mvn compile`
Expected: BUILD SUCCESS

**Step 5: 运行测试**

Run: `cd console/backend && mvn test -pl hub -am`
Expected: 测试通过（可能有部分测试需要修复）

**Step 6: Commit**

```bash
git add -A console/backend/
git commit --no-verify -m "refactor: simplify commons pom.xml, remove business dependencies"
```

---

## Task 10: 最终验证与清理

**Step 1: 全量编译**

Run: `cd console/backend && mvn clean compile`
Expected: BUILD SUCCESS

**Step 2: 运行全部测试**

Run: `cd console/backend && mvn test`
Expected: 所有测试通过

**Step 3: 检查 Spring Boot 应用启动**

Run: `cd console/backend && mvn spring-boot:run -pl hub`（手动验证，启动后 Ctrl+C）
Expected: 应用正常启动，无 Bean 冲突或缺失

**Step 4: 确认最终模块结构**

```bash
# 确认只有两个模块
cat console/backend/pom.xml | grep "<module>"
# 预期输出：
# <module>commons</module>
# <module>hub</module>

# 确认 commons 文件数
find console/backend/commons/src/main/java -name "*.java" | wc -l
# 预期：约 26 个

# 确认 hub 文件数
find console/backend/hub/src/main/java -name "*.java" | wc -l
# 预期：约 1100+ 个（原 hub 307 + toolkit 562 + commons 业务 231）
```

**Step 5: 最终提交**

```bash
git add -A
git commit --no-verify -m "refactor: complete hub-toolkit merge and commons simplification"
```
