# 代码质量统一设计

**Goal:** 统一 hub 模块和 commons 模块的代码风格和代码质量，梳理 entity 层结构，为后续 DDD 改造做准备。

**Scope:** hub 模块全部代码 + commons 模块基础设施代码。

---

## 1. 包结构规范化

- Service 层：所有 service 包统一使用 `impl` 子包放实现类（当前 15 有 / 10 无）
- Mapper 包名：统一为单数形式（`user` 而非 `users`）
- Controller/Handler 等其他包：检查并统一单复数

## 2. DTO/VO 命名后缀统一

- 统一为驼峰风格：`Dto`、`Vo`
- 清理 `DTO`、`VO` 全大写后缀
- 涉及类名重命名 + 所有引用更新（Java、XML、YML）

## 3. Entity 层梳理

当前 entity 包下 425 个文件、26 个子包，混杂了 table 映射、core 对象、biz 对象、dto、vo、pojo 等。需要：
- 详细分析每个子包的职责和内容
- 按业务域重新组织
- 分离数据库实体（table）和业务对象（vo/dto/pojo）
- 为后续 DDD 改造建立清晰的分层基础

## 4. 注释语言统一

- 统一为英文注释
- 将中文注释翻译为英文
- Javadoc、行内注释、TODO 注释全部英文化

## 5. 代码风格细节

- 日志：统一 `log.info/warn/error` 参数化写法，避免字符串拼接
- 异常：确保所有异常使用 `BusinessException` + `ResponseEnum`
- 方法命名：统一 CRUD 命名规范

## 决策记录

| 决策 | 选择 |
|------|------|
| 工作拆分 | 分两轮：先代码质量，再 DDD 准备 |
| DTO/VO 后缀 | 驼峰风格 Dto / Vo |
| 注释语言 | 统一英文 |
| impl 子包 | 全部统一使用 |
| 包名单复数 | 统一单数 |
