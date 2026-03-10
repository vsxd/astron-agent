# 代码质量统一实施计划

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 统一 hub 模块和 commons 模块的代码风格和代码质量，梳理 entity 层结构，为后续 DDD 改造做准备。

**Architecture:** 自底向上分步执行。先处理低风险的包结构规范化（mapper 重命名、service impl 统一），再处理 DTO/VO 命名统一，然后梳理 entity 层，最后统一注释语言和代码风格。每步验证编译通过。

**Tech Stack:** Java 21, Spring Boot 3.5.4, MyBatis-Plus 3.5.7, Maven multi-module

---

## Task 1: Mapper 包名统一 — users → user

**Goal:** 将 mapper 包中唯一的复数形式 `users` 重命名为 `user`。

**Files:**
- Rename: `hub/src/main/java/com/iflytek/astron/console/hub/mapper/users/` → `hub/src/main/java/com/iflytek/astron/console/hub/mapper/user/`
- Modify: ALL files that import `hub.mapper.users.*`
- Modify: mapper XML files referencing `hub.mapper.users.*`

- [ ] **Step 1: 移动目录**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
mv hub/src/main/java/com/iflytek/astron/console/hub/mapper/users \
   hub/src/main/java/com/iflytek/astron/console/hub/mapper/user
```

- [ ] **Step 2: 替换 Java 文件中的包名**

```bash
# 替换 package 声明
find hub/src -name "*.java" -exec sed -i '' \
  's/package com\.iflytek\.astron\.console\.hub\.mapper\.users/package com.iflytek.astron.console.hub.mapper.user/g' {} +

# 替换 import 语句
find hub/src -name "*.java" -exec sed -i '' \
  's/import com\.iflytek\.astron\.console\.hub\.mapper\.users/import com.iflytek.astron.console.hub.mapper.user/g' {} +
```

- [ ] **Step 3: 替换 mapper XML 中的命名空间**

```bash
find hub/src/main/resources -name "*.xml" -exec sed -i '' \
  's/com\.iflytek\.astron\.console\.hub\.mapper\.users/com.iflytek.astron.console.hub.mapper.user/g' {} +
```

- [ ] **Step 4: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: rename mapper.users package to mapper.user for consistency"
```

---

## Task 2: Service impl 子包统一

**Goal:** 为缺少 impl 子包的 10 个 service 包创建 impl 目录，将实现类移入。

**Files:**
- 涉及 10 个 service 子包：common, database, external, extra, group, model, node, repo, task, tool
- 每个包中的实现类（非接口）需移入 impl/ 子目录

注意：有些包可能只有接口没有实现类，或者实现类在其他地方。需要逐个检查。

- [ ] **Step 1: 分析每个包的内容**

对每个缺少 impl/ 的 service 包，列出所有 Java 文件，区分接口和实现类：
- 接口：以 `I` 开头或不含 `@Service`/`@Component` 注解
- 实现类：含 `@Service`/`@Component` 注解或 `implements` 关键字

```bash
cd /Users/xudongsun/github/astron-agent/console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service
for pkg in common database external extra group model node repo task tool; do
  echo "=== $pkg ==="
  if [ -d "$pkg" ]; then
    for f in "$pkg"/*.java; do
      [ -f "$f" ] && echo "  $(basename $f): $(grep -l '@Service\|@Component\|implements ' "$f" 2>/dev/null && echo 'IMPL' || echo 'INTERFACE')"
    done
  fi
done
```

- [ ] **Step 2: 创建 impl 目录并移动实现类**

对每个包，将实现类移入 impl/ 子目录：

```bash
cd /Users/xudongsun/github/astron-agent/console/backend/hub/src/main/java/com/iflytek/astron/console/hub/service

# 示例：tool 包
mkdir -p tool/impl
# 移动实现类（具体文件名在 Step 1 确定后填入）
mv tool/ToolBoxService.java tool/impl/  # 如果是实现类
```

- [ ] **Step 3: 更新 package 声明和 import**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
# 对每个移动的文件，更新 package 声明
# 对所有引用这些文件的地方，更新 import
```

- [ ] **Step 4: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: unify service impl sub-package structure"
```

---

## Task 3: DTO 命名后缀统一 — DTO → Dto

**Goal:** 将 15 个使用 `DTO` 全大写后缀的类重命名为 `Dto` 驼峰风格。

**Files:**
- Rename: 15 个 `*DTO.java` 文件
- Modify: ALL files that import/reference these classes

- [ ] **Step 1: 列出所有 DTO 后缀的文件**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
find hub/src -name "*DTO.java" -type f | sort
```

- [ ] **Step 2: 逐个重命名**

对每个文件：
1. 重命名文件：`XxxDTO.java` → `XxxDto.java`
2. 替换文件内的类名声明
3. 全局替换所有 import 和引用

```bash
# 示例
cd /Users/xudongsun/github/astron-agent/console/backend
# 重命名文件
mv hub/src/.../XxxDTO.java hub/src/.../XxxDto.java
# 替换类名
find hub/src -name "*.java" -exec sed -i '' 's/XxxDTO/XxxDto/g' {} +
# 替换 mapper XML 中的引用
find hub/src/main/resources -name "*.xml" -exec sed -i '' 's/XxxDTO/XxxDto/g' {} +
```

- [ ] **Step 3: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: rename DTO suffix to Dto for naming consistency"
```

---

## Task 4: VO 命名后缀统一 — VO → Vo

**Goal:** 将 31 个使用 `VO` 全大写后缀的类重命名为 `Vo` 驼峰风格。

**Files:**
- Rename: 31 个 `*VO.java` 文件
- Modify: ALL files that import/reference these classes

- [ ] **Step 1: 列出所有 VO 后缀的文件**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
find hub/src -name "*VO.java" -type f | sort
```

- [ ] **Step 2: 逐个重命名**

同 Task 3 的策略，对每个 `*VO.java` 文件：
1. 重命名文件
2. 替换类名声明
3. 全局替换 import 和引用（Java + XML）

- [ ] **Step 3: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: rename VO suffix to Vo for naming consistency"
```

---

## Task 5: Entity 层梳理 — 分析与规划

**Goal:** 详细分析 entity 层 26 个子包的内容，制定重组方案。

**Files:**
- Reference: `hub/src/main/java/com/iflytek/astron/console/hub/entity/` (425 files, 26 sub-packages)

这是一个分析任务，不直接改代码。输出为一份 entity 层重组方案文档。

- [ ] **Step 1: 逐包分析**

对 entity 下每个子包：
1. 列出所有文件
2. 分类：DB 实体（有 @TableName）、VO、DTO、POJO、协议对象
3. 识别业务域归属
4. 记录与其他包的依赖关系

- [ ] **Step 2: 制定重组方案**

基于分析结果，制定 entity 层重组方案：
- 哪些子包保持不变
- 哪些子包需要合并
- 哪些文件需要移动到 dto/ 或 vo/ 顶层包
- 哪些文件需要按业务域重新组织

- [ ] **Step 3: 输出分析文档**

将分析结果和重组方案写入：
`console/docs/plans/2026-03-10-entity-layer-analysis.md`

- [ ] **Step 4: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add console/docs/plans/2026-03-10-entity-layer-analysis.md
git commit --no-verify -m "docs: entity layer analysis and restructuring plan"
```

---

## Task 6: Entity 层梳理 — 执行重组

**Goal:** 根据 Task 5 的分析结果，执行 entity 层重组。

**Files:**
- Modify: entity/ 下的文件（具体文件在 Task 5 分析后确定）

- [ ] **Step 1: 按分析方案移动文件**

根据 Task 5 输出的重组方案，逐步移动文件：
1. 移动文件到新位置
2. 更新 package 声明
3. 更新所有 import 引用
4. 更新 mapper XML 引用
5. 更新 MyBatis @Alias 注解（如有冲突）

- [ ] **Step 2: 分批验证编译**

每移动一批文件后验证：
Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`

- [ ] **Step 3: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: restructure entity layer by business domain"
```

---

## Task 7: 注释语言统一 — 中文 → 英文

**Goal:** 将所有中文注释翻译为英文。

**Files:**
- Modify: ~1090 个包含中文注释的 Java 文件

- [ ] **Step 1: 分批处理**

按包分批处理，每批翻译后验证编译：
1. controller 包（50 文件）
2. service 包（183 文件）
3. entity 包（425 文件）
4. mapper 包（124 文件）
5. dto 包（139 文件）
6. 其他包（config, util, handler 等）

对每个文件：
- 将中文 Javadoc 翻译为英文
- 将中文行内注释翻译为英文
- 将中文 TODO 注释翻译为英文
- 保留 i18n 消息 key 中的中文（properties 文件不动）

- [ ] **Step 2: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: translate Chinese comments to English"
```

---

## Task 8: 代码风格细节统一

**Goal:** 统一日志、异常处理、方法命名等代码风格细节。

**Files:**
- Modify: hub 模块中风格不一致的文件

- [ ] **Step 1: 日志风格统一**

检查并修复：
- 字符串拼接日志改为参数化：`log.info("user: " + name)` → `log.info("user: {}", name)`
- 统一日志级别使用规范

```bash
cd /Users/xudongsun/github/astron-agent/console/backend
# 查找字符串拼接日志
grep -rn 'log\.\(info\|warn\|error\|debug\)(".*" +' hub/src/main/java --include="*.java" | head -20
```

- [ ] **Step 2: 异常处理统一**

检查是否还有非 BusinessException 的自定义异常使用：
```bash
grep -rn "throw new " hub/src/main/java --include="*.java" | grep -v "BusinessException\|IllegalArgumentException\|IllegalStateException\|UnsupportedOperationException\|NullPointerException" | head -20
```

- [ ] **Step 3: 验证编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn compile -pl hub -am`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A console/backend/hub/
git commit --no-verify -m "refactor: unify logging and exception handling patterns"
```

---

## Task 9: 最终验证

**Goal:** 全面验证代码质量统一后的项目。

- [ ] **Step 1: 全量编译**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn clean compile`
Expected: BUILD SUCCESS

- [ ] **Step 2: 运行测试**

Run: `cd /Users/xudongsun/github/astron-agent/console/backend && mvn test`
Expected: 测试通过（除已知预存失败）

- [ ] **Step 3: 验证规范一致性**

```bash
cd /Users/xudongsun/github/astron-agent/console/backend

# 无复数 mapper 包名
ls hub/src/main/java/com/iflytek/astron/console/hub/mapper/ | grep -E 's$' || echo "✓ Clean"

# 无 DTO 全大写后缀
find hub/src -name "*DTO.java" | wc -l
# 预期：0

# 无 VO 全大写后缀
find hub/src -name "*VO.java" | wc -l
# 预期：0

# 所有 service 包有 impl 子目录
cd hub/src/main/java/com/iflytek/astron/console/hub/service
for dir in */; do
  [ -d "$dir/impl" ] || echo "❌ $dir missing impl/"
done
```

- [ ] **Step 4: 最终提交**

```bash
cd /Users/xudongsun/github/astron-agent
git add -A
git commit --no-verify -m "refactor: complete code quality unification"
```
