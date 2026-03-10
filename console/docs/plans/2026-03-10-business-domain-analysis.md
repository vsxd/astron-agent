# Hub 模块业务域全面分析报告

> 生成日期：2026-03-10
> 目的：为 DDD 重构提供完整的业务域梳理，识别限界上下文和域间依赖

---

## 一、业务域全景图

经过对 hub 模块所有包结构、Controller、Service、Entity、DTO、Mapper 的全面分析，识别出以下 **13 个业务域**：

| 序号 | 业务域 | 核心职责 | Controller 数量 | Service 数量 | DB 表数量 |
|------|--------|---------|----------------|-------------|----------|
| 1 | Bot（智能体） | 智能体创建、配置、管理 | 4 | 18+ | 15+ |
| 2 | Chat（对话） | 对话会话、消息、历史记录 | 5 | 12+ | 12+ |
| 3 | Workflow（工作流） | 工作流编排、版本、调试 | 5 | 10+ | 8+ |
| 4 | Space（空间） | 空间/企业团队管理 | 8 | 16+ | 8+ |
| 5 | Knowledge/Repo（知识库） | 知识库、文件、知识条目管理 | 3 | 8+ | 5+ |
| 6 | Publish（发布） | 多渠道发布、API 发布 | 2 | 6+ | 3+ |
| 7 | Tool（插件/工具） | 插件管理、RPA | 2 | 5+ | 6+ |
| 8 | Model（模型） | LLM 模型管理、配置 | 2 | 4+ | 3+ |
| 9 | User（用户） | 用户信息、我的智能体 | 2 | 2+ | 2+ |
| 10 | Share（分享） | 智能体分享 | 1 | 1+ | 2+ |
| 11 | Notification（通知） | 消息通知 | 1 | 2+ | 3 |
| 12 | WeChat（微信） | 微信公众号集成 | 1 | 2+ | 2+ |
| 13 | Database（数据库） | 数据库资源管理 | 1 | 1+ | 3 |

**辅助域/支撑域：**
- Homepage（首页/广场）：智能体广场展示
- Common（公共）：配置信息、图片上传、LLM 查询
- OpenAPI（开放接口）：外部 API 集成
- Extra（扩展）：语音识别等额外能力

---

## 二、各业务域详细分析

---

### 2.1 Bot 域（智能体管理）

#### 2.1.1 业务能力

Bot 域是系统的**核心域**，提供智能体的全生命周期管理能力：
- 创建智能体（普通 Prompt Bot、Workflow Bot、Talk Agent）
- 编辑智能体基本信息（名称、描述、头像、开场白）
- 复制智能体
- 删除智能体
- 智能体模板管理
- 智能体收藏
- 智能体人设配置（Personality）
- Prompt 增强与 AI 生成
- 语音配置（TTS 发音人、声音克隆）
- 智能体市场（上架/下架）
- 智能体标签管理
- 智能体数据集关联

#### 2.1.2 API 端点

**BotController** (`/workflow`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/workflow/base-save` | 保存/更新智能体基本信息 |
| POST | `/workflow/update-bot-basic-info` | 更新智能体描述、开场白、输入示例 |
| POST | `/workflow/copy-bot` | 复制智能体 |

**BotCreateController** (`/bot`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/bot/create` | 创建工作流智能体 |
| POST | `/bot/update` | 更新智能体配置 |
| GET | `/bot/detail` | 获取智能体详情 |
| GET | `/bot/type-list` | 获取智能体类型列表 |
| POST | `/bot/ai-generation` | AI 生成智能体配置 |
| GET | `/bot/model-list` | 获取模型列表 |
| GET | `/bot/template-list` | 获取智能体模板列表 |

**BotFavoriteController** (`/bot/favorite`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/bot/favorite/list` | 收藏列表 |
| POST | `/bot/favorite/create` | 添加收藏 |
| POST | `/bot/favorite/delete` | 取消收藏 |

**PersonalityController** (`/personality`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/personality/aiGenerate` | AI 生成人设描述 |
| POST | `/personality/aiPolishing` | AI 润色人设 |
| GET | `/personality/getCategory` | 获取人设分类 |
| GET | `/personality/getRole` | 获取人设角色列表 |

**PromptController** (`/prompt`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/prompt/enhance` | Prompt 增强（SSE） |
| POST | `/prompt/next-question-advice` | 下一个问题建议 |
| POST | `/prompt/ai-generate` | AI 内容生成（SSE） |
| POST | `/prompt/ai-code` | AI 代码操作（SSE） |

**SpeakerTrainController** (`/speaker/train`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/speaker/train/create` | 创建声音克隆 |
| GET | `/speaker/train/get-text` | 获取训练文本 |
| GET | `/speaker/train/train-speaker` | 获取训练发音人 |
| POST | `/speaker/train/update-speaker` | 更新训练发音人 |
| POST | `/speaker/train/delete-speaker` | 删除训练发音人 |

**TalkAgentController** (`/talkAgent`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/talkAgent/getSceneList` | 获取对话场景列表 |
| POST | `/talkAgent/create` | 创建 Talk Agent |
| POST | `/talkAgent/upgradeWorkflow` | 升级为工作流 |
| POST | `/talkAgent/saveHistory` | 保存历史 |
| GET | `/talkAgent/signature` | 获取签名 |

**VoiceApiController** (`/voice`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/voice/tts-sign` | 获取 TTS 签名 |
| GET | `/voice/get-pronunciation-person` | 获取发音人列表 |

#### 2.1.3 核心服务

| 服务 | 职责 |
|------|------|
| `BotService` / `BotServiceImpl` | 智能体 CRUD 核心逻辑，创建/更新/复制/删除 |
| `BotTransactionalService` | 智能体事务性操作（复制等） |
| `BotAIService` | AI 生成智能体配置 |
| `ChatBotDataService` | 智能体数据查询（chat_bot_base/market/list） |
| `ChatBotMarketService` | 智能体市场数据管理 |
| `ChatBotTagService` | 智能体标签管理 |
| `BotFavoriteService` | 智能体收藏 |
| `BotDatasetService` | 智能体数据集关联 |
| `BotRepoRelService` | 智能体-知识库关联 |
| `BotToolRelService` | 智能体-工具关联 |
| `BotRepoSubscriptService` | 智能体知识库订阅 |
| `BotMarketDataService` | 市场数据统计 |
| `PersonalityConfigService` | 人设配置管理 |
| `PromptService` | Prompt 增强/AI 生成 |
| `SpeakerTrainService` | 声音克隆训练 |
| `CustomSpeakerService` | 自定义发音人管理 |
| `VoiceService` | 语音服务（TTS 签名） |
| `TalkAgentService` | Talk Agent 管理 |
| `BotTypeListService` | 智能体类型列表 |
| `OpenAiModelProcessService` | OpenAI 模型处理 |

#### 2.1.4 数据模型

**DB 表：**
| 表名 | 用途 |
|------|------|
| `chat_bot_base` | 智能体基本信息 |
| `chat_bot_market` | 智能体市场信息（上架状态） |
| `chat_bot_list` | 用户智能体列表 |
| `chat_bot_tag` | 智能体标签 |
| `chat_bot_prompt_struct` | 智能体 Prompt 结构 |
| `chat_bot_remove` | 已删除智能体 |
| `bot_template` | 智能体模板 |
| `bot_type_list` | 智能体类型 |
| `bot_favorite` | 用户收藏 |
| `bot_dataset` | 智能体数据集 |
| `bot_model_bind` | 智能体模型绑定 |
| `bot_model_config` (BotModelConfig) | 智能体模型配置 |
| `bot_repo_subscript` | 智能体知识库订阅 |
| `user_lang_chain_info` | 用户工作流链信息 |
| `user_lang_chain_log` | 工作流链日志 |
| `personality_config` | 人设配置 |
| `personality_category` | 人设分类 |
| `personality_role` | 人设角色 |
| `custom_speaker` | 自定义发音人 |
| `pronunciation_person_config` | 发音人配置 |
| `take_off_list` | 下架列表 |
| `bot_conversation_stats` | 对话统计 |
| `bot_chat_file_param` | 对话文件参数 |

**关系表：**
| 表名 | 连接域 |
|------|--------|
| `bot_repo_rel` | Bot ↔ Knowledge/Repo |
| `bot_tool_rel` | Bot ↔ Tool |
| `bot_flow_rel` | Bot ↔ Workflow |

#### 2.1.5 域间依赖

**依赖的域：**
- Chat 域：`ChatListDataService`（对话列表数据）
- Workflow 域：`UserLangChainDataService`（工作流链数据）
- User 域：`UserInfoDataService`（用户信息）
- Knowledge 域：`DatasetDataService`（数据集）
- Model 域：`LLMService`（模型列表）

**被依赖的域：**
- Chat 域、Workflow 域、Publish 域、Share 域、User 域、Homepage 域 均依赖 `ChatBotDataService`

#### 2.1.6 外部集成
- Redis（Redisson）：分布式锁、缓存
- MaaS 平台：`MaasUtil` 调用外部工作流同步 API
- S3 存储：头像等文件上传
- 讯飞 TTS/声音克隆 API

---

### 2.2 Chat 域（对话管理）

#### 2.2.1 业务能力

- 创建/删除对话列表
- 发送消息并获取 SSE 流式响应
- 对话历史记录查询
- 对话重启（新建对话树）
- 对话文件上传/解绑
- 对话调试（Debug）
- 停止生成
- 清除对话历史
- 多模态对话支持
- 推理记录（Reasoning）
- 溯源（Trace to Source）

#### 2.2.2 API 端点

**ChatMessageController** (`/chat-message`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/chat-message/chat` | 发送消息（SSE 流） |
| POST | `/chat-message/stop` | 停止生成 |
| POST | `/chat-message/debug` | 调试对话（SSE 流） |
| GET | `/chat-message/clear` | 清除对话历史 |

**ChatListController** (`/chat-list`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/chat-list/all-chat-list` | 获取所有对话列表 |
| POST | `/chat-list/v1/create-chat-list` | 创建对话列表 |
| POST | `/chat-list/v1/del-chat-list` | 删除对话列表 |
| POST | `/chat-list/v1/update-chat-list` | 更新对话列表名称 |

**ChatHistoryController** (`/chat-history`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/chat-history/all/{chatId}` | 获取对话历史 |

**ChatEnhanceController** (`/chat-enhance`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/chat-enhance/save-file` | 保存对话文件 |
| POST | `/chat-enhance/unbind-file` | 解绑文件 |

**ChatRestartController** (`/chat-restart`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/chat-restart/restart` | 重启对话 |

#### 2.2.3 核心服务

| 服务 | 职责 |
|------|------|
| `BotChatService` | 对话核心逻辑，消息发送、调试 |
| `ChatListService` | 对话列表 CRUD |
| `ChatEnhanceService` | 对话增强（文件上传/解绑） |
| `ChatRestartService` | 对话重启 |
| `ChatReqRespService` | 请求/响应记录管理 |
| `ChatHistoryMultiModalService` | 多模态历史合并 |
| `ChatReasonRecordsService` | 推理记录 |
| `TraceToSourceService` | 溯源服务 |
| `ChatBotApiService` | Bot API 对话 |
| `ChatDataService` | 对话数据访问层 |
| `ChatListDataService` | 对话列表数据访问层 |
| `ChatHistoryService` | 对话历史数据访问层 |
| `ChatRecordModelService` | 对话记录模型 |
| `SparkChatService`（service 根目录） | Spark 对话服务 |
| `PromptChatService`（service 根目录） | Prompt 对话服务 |

#### 2.2.4 数据模型

**DB 表：**
| 表名 | 用途 |
|------|------|
| `chat_list` | 对话列表 |
| `chat_tree_index` | 对话树索引 |
| `chat_req_records` | 请求记录 |
| `chat_resp_records` | 响应记录 |
| `chat_req_model` | 请求模型数据 |
| `chat_resp_model` | 响应模型数据 |
| `chat_reason_records` | 推理记录 |
| `chat_trace_source` | 溯源记录 |
| `chat_reanwser_records` | 重新回答记录 |
| `chat_token_records` | Token 消耗记录 |
| `chat_resp_alltool_data` | 工具调用数据 |
| `chat_file_user` | 用户对话文件 |
| `share_chat` | 分享对话 |
| `share_qa` | 分享问答 |

#### 2.2.5 域间依赖

**依赖的域：**
- Bot 域：`ChatBotDataService`、`BotService`、`PersonalityConfigService`
- Workflow 域：`WorkflowBotChatService`
- Knowledge 域：`KnowledgeService`
- Model 域：`ModelService`

**被依赖的域：**
- Bot 域依赖 `ChatListDataService`
- Share 域依赖 `ChatListService`

#### 2.2.6 外部集成
- Redis（Redisson）：SSE 停止信号发布/订阅
- MaaS 平台：工作流对话调用
- WebSocket：实时通信

---

### 2.3 Workflow 域（工作流管理）

#### 2.3.1 业务能力

- 工作流列表查询（分页、搜索、状态过滤）
- 工作流详情查看
- 工作流调试（SSE 流式）
- 工作流对话（SSE 流式）
- 工作流恢复对话
- 工作流版本管理（创建、恢复、查询）
- 工作流导入/导出
- 工作流模板管理
- 工作流发布管理
- 工作流对比
- 工作流反馈
- 工作流节点历史
- MCP 工具配置
- Agent 节点 Prompt 模板
- Talk Agent 配置

#### 2.3.2 API 端点

**WorkflowController** (`/workflow`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/workflow/list` | 工作流列表 |
| GET | `/workflow/detail` | 工作流详情 |
| POST | `/workflow/debug` | 工作流调试（SSE） |
| POST | `/workflow/chat` | 工作流对话（SSE） |
| POST | `/workflow/resume` | 恢复对话（SSE） |
| POST | `/workflow/import` | 导入工作流 |
| GET | `/workflow/export` | 导出工作流 |
| POST | `/workflow/save-comparison` | 保存对比 |
| GET | `/workflow/comparison-list` | 对比列表 |
| POST | `/workflow/save-feedback` | 保存反馈 |
| GET | `/workflow/feedback-list` | 反馈列表 |
| GET | `/workflow/dialog-list` | 对话列表 |
| POST | `/workflow/save-dialog` | 保存对话 |
| GET | `/workflow/node-history` | 节点历史 |
| GET | `/workflow/mcp-server-tool` | MCP 工具列表 |
| GET | `/workflow/mcp-server-tool-detail` | MCP 工具详情 |
| GET | `/workflow/agent-node/prompt-template` | Agent 节点 Prompt 模板 |
| GET | `/workflow/copy-flow` | 复制工作流 |
| GET | `/workflow/get-max-version` | 获取最大版本号 |
| GET | `/workflow/get-talk-agent-config` | 获取 Talk Agent 配置 |

**VersionController** (`/workflow/version`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/workflow/version/list` | 版本列表（按 flowId） |
| GET | `/workflow/version/list-botId` | 版本列表（按 botId） |
| POST | `/workflow/version` | 创建版本 |
| POST | `/workflow/version/restore` | 恢复版本 |
| POST | `/workflow/version/update-channel-result` | 更新发布结果 |
| POST | `/workflow/version/get-version-name` | 获取版本名 |
| GET | `/workflow/version/get-max-version` | 获取最大版本 |
| POST | `/workflow/version/get-version-sys-data` | 获取版本系统数据 |
| POST | `/workflow/version/have-version-sys-data` | 检查版本系统数据 |
| GET | `/workflow/version/publish-result` | 查询发布结果 |

**WorkflowBotController** (`/workflow/bot`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/workflow/bot/templateGroup` | 工作流模板分组 |
| POST | `/workflow/bot/createFromTemplate` | 从模板创建 |
| POST | `/workflow/bot/templateList` | 模板列表 |
| POST | `/workflow/bot/get-inputs-type` | 获取输入类型 |

**ChatWorkflowController** (`/workflow/web`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/workflow/web/info` | 获取工作流信息 |

**WorkflowChatController** (`/api/v1/workflow`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/api/v1/workflow/chat/stream` | 工作流对话流（SSE） |
| POST | `/api/v1/workflow/chat/resume` | 恢复工作流对话（SSE） |

#### 2.3.3 核心服务

| 服务 | 职责 |
|------|------|
| `WorkflowService` | 工作流核心逻辑（列表、详情、调试、对话） |
| `VersionService` | 版本管理 |
| `WorkflowBotService` | 工作流智能体关联 |
| `WorkflowBotChatService` | 工作流对话服务 |
| `WorkflowBotParamService` | 工作流参数管理 |
| `WorkflowReleaseService` | 工作流发布 |
| `WorkflowExportService` | 工作流导入/导出 |
| `WorkflowTemplateGroupService` | 模板分组管理 |
| `BotChainService` | Bot 链管理 |
| `BotMaasService` | MaaS 集成 |
| `TalkAgentConfigService` | Talk Agent 配置 |
| `WorkflowChatService` | 工作流对话（新版 API） |
| `WssListenerService` | WebSocket 监听 |

#### 2.3.4 数据模型

**DB 表：**
| 表名 | 用途 |
|------|------|
| `workflow_config_entity` (WorkflowConfigEntity) | 工作流配置 |
| `workflow_version` (WorkflowVersion) | 工作流版本 |
| `workflow_comparison` (WorkflowComparison) | 工作流对比 |
| `workflow_dialog` (WorkflowDialog) | 工作流对话 |
| `workflow_feedback` (WorkflowFeedback) | 工作流反馈 |
| `workflow_node_history` (WorkflowNodeHistory) | 节点历史 |
| `flow_protocol_temp` (FlowProtocolTemp) | 工作流协议模板 |
| `flow_release_channel` (FlowReleaseChannel) | 发布渠道 |
| `flow_release_aiui_info` (FlowReleaseAiuiInfo) | AIUI 发布信息 |
| `mcp_tool_config` (McpToolConfig) | MCP 工具配置 |
| `prompt_template` (PromptTemplate) | Prompt 模板 |
| `workflow_template_group` | 工作流模板分组 |

**关系表：**
| 表名 | 连接域 |
|------|--------|
| `bot_flow_rel` | Workflow ↔ Bot |
| `flow_repo_rel` | Workflow ↔ Knowledge/Repo |
| `flow_tool_rel` | Workflow ↔ Tool |
| `flow_db_rel` | Workflow ↔ Database |

#### 2.3.5 域间依赖

**依赖的域：**
- Bot 域：`ChatBotDataService`、`BotService`
- Chat 域：`ChatDataService`、`ChatHistoryService`
- Data 层：`UserLangChainDataService`

**被依赖的域：**
- Chat 域依赖 `WorkflowBotChatService`
- Tool 域依赖 `WorkflowService`
- Bot 域依赖 `BotChainService`

#### 2.3.6 外部集成
- MaaS 平台：工作流引擎 API
- WebSocket：工作流画布协同编辑
- SSE：流式调试/对话
---

### 2.4 Space 域（空间与企业团队管理）

#### 2.4.1 业务能力

Space 域管理多租户体系下的空间和企业团队：
- 个人空间创建/编辑/删除
- 企业团队创建/编辑
- 空间用户管理（添加/移除/角色变更/转让）
- 企业用户管理
- 空间/企业权限控制
- 邀请记录管理（邀请/接受/拒绝/撤销）
- 申请记录管理（申请加入/审批/拒绝）
- 空间访问记录

#### 2.4.2 API 端点

**SpaceController** (`/space`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/space/check-name` | 检查空间名是否存在 |
| GET | `/space/visit-space` | 访问空间 |
| GET | `/space/recent-visit-list` | 最近访问列表 |
| GET | `/space/get-last-visit-space` | 最近访问的空间 |
| GET | `/space/personal-list` | 个人所有空间 |
| GET | `/space/personal-self-list` | 个人创建的空间 |
| GET | `/space/detail` | 空间详情 |
| POST | `/space/create` | 创建空间 |
| DELETE | `/space/delete` | 删除空间 |
| POST | `/space/update` | 更新空间 |
| POST | `/space/create-corporate-space` | 企业创建空间 |
| DELETE | `/space/delete-corporate-space` | 企业删除空间 |
| POST | `/space/update-corporate-space` | 企业编辑空间 |
| GET | `/space/corporate-list` | 企业所有空间 |
| GET | `/space/corporate-count` | 企业空间数量 |
| GET | `/space/corporate-join-list` | 企业我的空间 |

**EnterpriseController** (`/enterprise`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/enterprise/visit-enterprise` | 访问企业团队 |
| GET | `/enterprise/check-need-create-team` | 检查是否需要创建团队 |
| GET | `/enterprise/check-certification` | 检查企业认证 |
| POST | `/enterprise/create` | 创建团队 |
| GET | `/enterprise/check-name` | 检查团队名 |
| POST | `/enterprise/update-name` | 更新团队名 |
| POST | `/enterprise/update-logo` | 设置团队 LOGO |
| POST | `/enterprise/update-avatar` | 设置团队头像 |
| GET | `/enterprise/detail` | 团队详情 |
| GET | `/enterprise/join-list` | 所有团队 |

**SpaceUserController** (`/space-user`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/space-user/enterprise-add` | 企业空间添加用户 |
| DELETE | `/space-user/remove` | 移除用户 |
| POST | `/space-user/update-role` | 更新用户角色 |
| POST | `/space-user/page` | 空间用户列表 |
| POST | `/space-user/quit-space` | 退出空间 |
| GET | `/space-user/list-space-member` | 查询空间成员 |
| POST | `/space-user/transfer-space` | 转让空间 |
| GET | `/space-user/get-user-limit` | 获取用户限额 |

**EnterpriseUserController** (`/enterprise-user`)
| 方法 | 路径 | 功能 |
|------|------|------|
| DELETE | `/enterprise-user/remove` | 移除用户 |
| POST | `/enterprise-user/update-role` | 更新角色 |
| POST | `/enterprise-user/page` | 团队用户列表 |
| POST | `/enterprise-user/quit-enterprise` | 退出团队 |
| GET | `/enterprise-user/get-user-limit` | 获取用户限额 |

**InviteRecordController** (`/invite-record`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/invite-record/get-invite-by-param` | 根据参数获取邀请记录 |
| GET | `/invite-record/space-search-user` | 空间邀请搜索用户 |
| GET | `/invite-record/space-search-username` | 空间邀请搜索用户名 |
| POST | `/invite-record/space-invite` | 邀请加入空间 |
| POST | `/invite-record/space-invite-list` | 空间邀请列表 |
| POST | `/invite-record/space-batch-invite` | 批量邀请 |
| GET | `/invite-record/enterprise-search-user` | 企业搜索用户 |
| GET | `/invite-record/enterprise-search-username` | 企业搜索用户名 |
| POST | `/invite-record/enterprise-invite` | 邀请加入企业 |
| POST | `/invite-record/enterprise-invite-list` | 企业邀请列表 |
| POST | `/invite-record/accept-invite` | 接受邀请 |
| POST | `/invite-record/refuse-invite` | 拒绝邀请 |
| POST | `/invite-record/revoke-enterprise-invite` | 撤销企业邀请 |
| POST | `/invite-record/revoke-space-invite` | 撤销空间邀请 |

**ApplyRecordController** (`/apply-record`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/apply-record/join-enterprise-space` | 申请加入企业空间 |
| POST | `/apply-record/agree-enterprise-space` | 审批通过 |
| POST | `/apply-record/refuse-enterprise-space` | 审批拒绝 |
| POST | `/apply-record/page` | 申请列表 |

**SpacePermissionController** (`/space-permission`) — 空壳，无端点
**EnterprisePermissionController** (`/enterprise-permission`) — 空壳，无端点

#### 2.4.3 核心服务

| 服务 | 职责 |
|------|------|
| `SpaceService` | 空间 CRUD、列表查询 |
| `SpaceBizService` | 空间业务逻辑（创建/删除/更新含验证） |
| `SpaceUserService` | 空间用户数据查询 |
| `SpaceUserBizService` | 空间用户业务操作 |
| `EnterpriseService` | 企业团队 CRUD |
| `EnterpriseBizService` | 企业业务逻辑 |
| `EnterpriseUserService` | 企业用户数据查询 |
| `EnterpriseUserBizService` | 企业用户业务操作 |
| `EnterpriseSpaceService` | 企业空间关联 |
| `SpacePermissionService` | 空间权限管理 |
| `EnterprisePermissionService` | 企业权限管理 |
| `InviteRecordService` | 邀请记录数据查询 |
| `InviteRecordBizService` | 邀请业务逻辑 |
| `ApplyRecordService` | 申请记录数据查询 |
| `ApplyRecordBizService` | 申请业务逻辑 |

#### 2.4.4 数据模型

| 表名 | 用途 |
|------|------|
| `agent_space` | 空间信息 |
| `agent_space_user` | 空间用户关系 |
| `agent_space_permission` | 空间权限配置 |
| `agent_enterprise` | 企业团队信息 |
| `agent_enterprise_user` | 企业用户关系 |
| `agent_enterprise_permission` | 企业权限配置 |
| `agent_invite_record` | 邀请记录 |
| `agent_apply_record` | 申请记录 |
| `application_form` | 申请表单 |

#### 2.4.5 域间依赖

**依赖的域：**
- Bot 域：`ChatBotDataService`（删除空间时清理智能体）
- User 域：`UserInfoDataService`（用户信息查询）
- Common 域：`MessageCodeService`（短信验证码）

**被依赖的域：**
- 几乎所有域通过 `SpaceInfoUtil` / `SpacePreAuth` / `EnterprisePreAuth` 依赖空间上下文

#### 2.4.6 外部集成
- 短信服务：删除空间时的验证码验证


---

### 2.5 Knowledge/Repo 域（知识库管理）

#### 2.5.1 业务能力

- 知识库（Repo）创建/编辑/删除/状态管理
- 文件上传/下载/删除/切片/向量化
- 知识条目（Knowledge）创建/编辑/启用/禁用/删除
- 文件目录树管理
- 命中测试（Hit Test）
- 知识库使用状态查询
- 大规模数据集管理

#### 2.5.2 API 端点

**RepoController** (`/repo`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/repo/create-repo` | 创建知识库 |
| POST | `/repo/update-repo` | 更新知识库 |
| POST | `/repo/update-repo-status` | 更新知识库状态 |
| POST | `/repo/page-list` | 知识库分页列表 |
| DELETE | `/repo/delete-repo` | 删除知识库 |
| GET | `/repo/detail` | 知识库详情 |
| POST | `/repo/hit-test` | 命中测试 |
| GET | `/repo/hit-test-history` | 命中测试历史 |
| DELETE | `/repo/delete-hit-test-history` | 删除命中测试历史 |
| GET | `/repo/file-list` | 知识库文件列表 |
| GET | `/repo/get-repo-use-status` | 知识库使用状态 |

**FileController** (`/file`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/file/upload` | 上传文件 |
| POST | `/file/create-html-file` | 创建 HTML 文件 |
| POST | `/file/slice-file` | 文件切片 |
| POST | `/file/embedding-file` | 文件向量化 |
| GET | `/file/get-file-info` | 获取文件信息 |
| POST | `/file/page-list` | 文件分页列表 |
| GET | `/file/download` | 下载文件 |
| GET | `/file/file-summary` | 文件摘要 |
| POST | `/file/create-directory` | 创建目录 |
| POST | `/file/update-directory` | 更新目录 |
| PUT | `/file/enable-file` | 启用/禁用文件 |
| DELETE | `/file/delete-file` | 删除文件 |
| GET | `/file/get-file-info-by-source-id` | 根据 sourceId 获取文件 |

**KnowledgeController** (`/knowledge`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/knowledge/create-knowledge` | 创建知识条目 |
| POST | `/knowledge/update-knowledge` | 更新知识条目 |
| PUT | `/knowledge/enable-knowledge` | 启用/禁用知识 |
| DELETE | `/knowledge/delete-knowledge` | 删除知识条目 |

#### 2.5.3 核心服务

| 服务 | 职责 |
|------|------|
| `RepoService` | 知识库 CRUD、命中测试、文件列表 |
| `FileInfoV2Service` | 文件上传/下载/切片/向量化/删除 |
| `KnowledgeRepoService` | 知识条目 CRUD |
| `FileDirectoryTreeService` | 文件目录树管理 |
| `HitTestHistoryService` | 命中测试历史 |
| `MassDatasetInfoService` | 大规模数据集信息 |
| `KnowledgeService`（knowledge 包） | 知识查询服务（被 Chat 域使用） |
| `ExtractKnowledgeTaskService` | 知识抽取任务 |

#### 2.5.4 数据模型

| 表名 | 用途 |
|------|------|
| `repo`（Repo entity） | 知识库信息 |
| `file_info_v2`（FileInfoV2 entity） | 文件信息 |
| `file_directory_tree`（FileDirectoryTree entity） | 文件目录树 |
| `knowledge`（MysqlKnowledge） | 知识条目 |
| `preview_knowledge`（MysqlPreviewKnowledge） | 预览知识 |
| `hit_test_history`（HitTestHistory） | 命中测试历史 |
| `bot_dataset` | 智能体数据集 |
| `bot_dataset_maas` | 智能体 MaaS 数据集 |
| `dataset_info` | 数据集信息 |
| `dataset_file` | 数据集文件 |
| `req_knowledge_records` | 知识请求记录 |

**关系表：**
| 表名 | 连接域 |
|------|--------|
| `bot_repo_rel` | Bot ↔ Repo |
| `flow_repo_rel` | Workflow ↔ Repo |

#### 2.5.5 域间依赖

**依赖的域：**
- Bot 域：`BotRepoSubscriptService`、`BotRepoRelService`（知识库与智能体关联）
- Common 域：`ConfigInfoService`（配置信息）

**被依赖的域：**
- Bot 域：`BotDatasetService`、`MassDatasetInfoService`
- Chat 域：`KnowledgeService`（对话中的知识检索）

#### 2.5.6 外部集成
- S3 存储：文件上传/下载
- 知识引擎 API：`KnowledgeV2ServiceCallHandler`（切片、向量化、检索）
- 开放平台：`OpenPlatformService`

---

### 2.6 Publish 域（发布管理）

#### 2.6.1 业务能力

- 智能体发布到多渠道（市场、API、微信公众号、MCP、飞书）
- 发布/取消发布
- 发布前准备（Prepare）
- 发布统计（对话量、用户量、时间序列）
- 版本管理
- Trace 日志查询
- API 发布（创建应用、创建 Bot API）
- MCP 发布

#### 2.6.2 API 端点

**BotPublishController** (`/publish`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/publish/bots` | 获取智能体列表 |
| GET | `/publish/bots/{botId}` | 获取智能体详情 |
| POST | `/publish/bots/{botId}/prepare` | 发布前准备 |
| POST | `/publish/bots/{botId}/publish` | 统一发布 |
| POST | `/publish/bots/{botId}/unpublish` | 取消发布 |
| GET | `/publish/bots/{botId}/stats` | 获取统计摘要 |
| GET | `/publish/bots/{botId}/stats/time-series` | 时间序列统计 |
| GET | `/publish/bots/{botId}/versions` | 版本历史 |
| GET | `/publish/bots/{botId}/trace` | Trace 日志 |

**PublishApiController** (`/publish-api`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/publish-api/create-user-app` | 创建用户应用 |
| GET | `/publish-api/app-list` | 应用列表 |
| POST | `/publish-api/create-bot-api` | 创建 Bot API |
| GET | `/publish-api/get-bot-api-info` | 获取 Bot API 信息 |

#### 2.6.3 核心服务

| 服务 | 职责 |
|------|------|
| `BotPublishService` | 发布核心逻辑、统计、版本管理 |
| `PublishChannelService` | 发布渠道管理 |
| `PublishApiService` | API 发布服务 |
| `McpService` | MCP 发布服务 |
| `TenantService` | 租户服务 |
| `ReleaseManageClientService` | 发布管理客户端 |
| `AppMstService`（extra） | 应用主数据服务 |

**策略模式：**
| 策略类 | 渠道 |
|--------|------|
| `MarketPublishStrategy` | 市场发布 |
| `ApiPublishStrategy` | API 发布 |
| `WechatPublishStrategy` | 微信发布 |
| `McpPublishStrategy` | MCP 发布 |
| `FeishuPublishStrategy` | 飞书发布 |
| `PublishStrategyFactory` | 策略工厂 |

#### 2.6.4 数据模型

| 表名 | 用途 |
|------|------|
| `flow_release_channel` | 发布渠道信息 |
| `flow_release_aiui_info`（FlowReleaseAiuiInfo） | AIUI 发布信息 |
| `mcp_data` | MCP 数据 |
| `app_mst` | 应用主数据 |
| `bot_conversation_stats` | 对话统计 |

#### 2.6.5 域间依赖

**依赖的域：**
- Bot 域：`ChatBotDataService`（智能体数据）
- Workflow 域：`UserLangChainDataService`（工作流数据）
- WeChat 域：`WechatThirdpartyService`（微信发布）

**被依赖的域：**
- User 域依赖发布状态

#### 2.6.6 外部集成
- MaaS 平台：发布 API
- 微信第三方平台 API
- MCP 服务
- 飞书 API


---

### 2.7 Tool 域（插件/工具管理）

#### 2.7.1 业务能力

- 插件创建/编辑/删除/临时保存
- 插件列表查询（分页）
- 插件详情查看
- 插件调试
- 插件导入/导出
- 插件发布到广场
- 插件收藏
- 用户操作历史
- 用户反馈
- RPA 机器人管理（创建/查询/更新/删除/调试）

#### 2.7.2 API 端点

**ToolBoxController** (`/tool`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/tool/create-tool` | 创建插件 |
| POST | `/tool/temporary-tool` | 临时保存插件 |
| PUT | `/tool/update-tool` | 编辑插件 |
| GET | `/tool/list-tools` | 插件分页列表 |
| GET | `/tool/detail` | 插件详情 |
| GET | `/tool/get-tool-default-icon` | 默认图标 |
| POST | `/tool/debug` | 调试插件 |
| GET | `/tool/delete-tool` | 删除插件 |
| GET | `/tool/favorite-tool` | 收藏/取消收藏 |
| GET | `/tool/add-tool-operateHistory` | 操作历史 |
| POST | `/tool/feedback` | 用户反馈 |
| GET | `/tool/publish-square` | 发布到广场 |
| GET | `/tool/export` | 导出插件 |
| POST | `/tool/import` | 导入插件 |

**RpaController** (`/api/rpa`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/api/rpa/source/list` | RPA 平台列表 |
| GET | `/api/rpa/list` | 用户 RPA 助手列表 |
| POST | `/api/rpa` | 创建 RPA 助手 |
| GET | `/api/rpa/{id}` | RPA 助手详情 |
| PUT | `/api/rpa/{id}` | 更新 RPA 助手 |
| DELETE | `/api/rpa/{id}` | 删除 RPA 助手 |
| POST | `/api/rpa/debug` | 调试 RPA |

#### 2.7.3 核心服务

| 服务 | 职责 |
|------|------|
| `ToolBoxService` | 插件全生命周期管理 |
| `RpaInfoService` | RPA 平台信息 |
| `RpaAssistantService` | RPA 助手管理 |
| `BotToolRelService` | 智能体-工具关联 |

#### 2.7.4 数据模型

| 表名 | 用途 |
|------|------|
| `tool_box`（ToolBox） | 插件信息 |
| `tool_box_feedback`（ToolBoxFeedback） | 插件反馈 |
| `tool_box_operate_history`（ToolBoxOperateHistory） | 操作历史 |
| `user_favorite_tool`（UserFavoriteTool） | 用户收藏工具 |
| `rpa_info` | RPA 平台信息 |
| `rpa_user_assistant` | RPA 用户助手 |
| `rpa_user_assistant_field` | RPA 助手字段 |

**关系表：**
| 表名 | 连接域 |
|------|--------|
| `bot_tool_rel` | Bot ↔ Tool |
| `flow_tool_rel` | Workflow ↔ Tool |

#### 2.7.5 域间依赖

**依赖的域：**
- Workflow 域：`WorkflowService`（RPA 调试中使用）
- Bot 域：`BotToolRelService`（工具关联查询）
- Common 域：`ConfigInfoService`

**被依赖的域：**
- Bot 域通过 `BotToolRelService` 关联

#### 2.7.6 外部集成
- 工具引擎 API：`ToolServiceCallHandler`
- S3 存储：图标等文件
- Redis：缓存

---

### 2.8 Model 域（模型管理）

#### 2.8.1 业务能力

- 模型添加/编辑/删除
- 模型列表查询
- 模型详情
- 模型启用/禁用
- 模型下架
- 本地模型管理
- 模型分类树
- RSA 公钥获取（密钥加密）
- LLM 授权列表查询
- 模型服务信息查询
- 自定义模型参数配置

#### 2.8.2 API 端点

**ModelController** (`/api/model`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/api/model` | 添加/编辑模型 |
| GET | `/api/model/delete` | 删除模型 |
| POST | `/api/model/list` | 模型列表 |
| GET | `/api/model/detail` | 模型详情 |
| GET | `/api/model/rsa/public-key` | RSA 公钥 |
| GET | `/api/model/check-model-base` | 检查模型归属 |
| GET | `/api/model/category-tree` | 模型分类树 |
| GET | `/api/model/{option}` | 启用/禁用模型 |
| GET | `/api/model/off-model` | 模型下架 |
| POST | `/api/model/local-model` | 添加/编辑本地模型 |
| GET | `/api/model/local-model/list` | 本地模型列表 |

**LLMController** (`/llm`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/llm/auth-list` | LLM 授权列表 |
| GET | `/llm/inter1` | 模型服务信息 |
| GET | `/llm/self-model-config` | 自定义模型参数 |
| GET | `/llm/flow-use-list` | 工作流使用的模型列表 |

#### 2.8.3 核心服务

| 服务 | 职责 |
|------|------|
| `ModelService` | 模型 CRUD、验证、分类管理 |
| `LLMService` | LLM 授权列表、模型服务信息 |
| `ModelCategoryService` | 模型分类管理 |
| `ModelCommonService` | 模型公共服务 |

#### 2.8.4 数据模型

| 表名 | 用途 |
|------|------|
| `base_model_map` | 基础模型映射 |
| `bot_model_bind` | 智能体模型绑定 |
| `config_info`（模型相关配置） | 模型配置信息 |

#### 2.8.5 域间依赖

**依赖的域：**
- Common 域：`ConfigInfoMapper`

**被依赖的域：**
- Bot 域：`LLMService`（创建智能体时获取模型列表）
- Chat 域：`ModelService`（对话时获取模型信息）

#### 2.8.6 外部集成
- 外部模型平台 API（获取模型列表、服务信息）
- S3 存储：本地模型文件
- Redis：模型列表缓存

---

### 2.9 User 域（用户管理）

#### 2.9.1 业务能力

- 获取当前用户信息
- 更新用户基本信息（昵称、头像）
- 用户协议同意
- 我的智能体列表
- 删除我的智能体
- 获取智能体详情

#### 2.9.2 API 端点

**UserInfoController** (`/user-info`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/user-info/me` | 获取当前用户信息 |
| POST | `/user-info/update` | 更新用户基本信息 |
| POST | `/user-info/agreement` | 同意用户协议 |

**MyBotController** (`/my-bot`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/my-bot/list` | 我的智能体列表 |
| POST | `/my-bot/delete` | 删除智能体 |
| POST | `/my-bot/bot-detail` | 智能体详情 |

#### 2.9.3 核心服务

| 服务 | 职责 |
|------|------|
| `UserInfoDataService` | 用户信息 CRUD |
| `UserBotService` | 用户智能体管理 |

#### 2.9.4 数据模型

| 表名 | 用途 |
|------|------|
| `user_info` | 用户信息 |
| `system_user`（SystemUser） | 系统用户 |

#### 2.9.5 域间依赖

**依赖的域：**
- Bot 域：`BotService`、`ChatBotDataService`、`BotFavoriteService`、`PersonalityConfigService`
- Chat 域：`ChatListService`、`ChatBotApiService`
- Publish 域：`McpDataService`
- Knowledge 域：`MassDatasetInfoService`
- WeChat 域：`BotOffiaccountService`

**被依赖的域：**
- Bot 域、Space 域、Homepage 域依赖 `UserInfoDataService`


---

### 2.10 Share 域（分享管理）

#### 2.10.1 业务能力

- 生成智能体分享链接
- 通过分享链接添加智能体

#### 2.10.2 API 端点

**ShareController** (`/share`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/share/get-share-key` | 获取分享标识 |
| POST | `/share/add-shared-agent` | 添加分享的智能体 |

#### 2.10.3 核心服务

| 服务 | 职责 |
|------|------|
| `ShareService` | 分享逻辑（生成 key、查询分享记录） |
| `ShareDataService` | 分享数据访问 |

#### 2.10.4 数据模型

| 表名 | 用途 |
|------|------|
| `agent_share_record` | 分享记录 |
| `share_chat` | 分享对话 |
| `share_qa` | 分享问答 |

#### 2.10.5 域间依赖

**依赖的域：**
- Bot 域：`ChatBotDataService`（获取智能体状态）
- Chat 域：`ChatListService`（创建对话列表）

---

### 2.11 Notification 域（通知管理）

#### 2.11.1 业务能力

- 查询用户通知列表
- 获取未读通知数量
- 标记通知已读
- 删除通知

#### 2.11.2 API 端点

**NotificationController** (`/notifications`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/notifications/list` | 查询通知列表 |
| GET | `/notifications/unread-count` | 未读通知数量 |
| POST | `/notifications/mark-read` | 标记已读 |
| DELETE | `/notifications/{notificationId}` | 删除通知 |

#### 2.11.3 核心服务

| 服务 | 职责 |
|------|------|
| `NotificationService` | 通知业务逻辑 |
| `NotificationDataService` | 通知数据访问 |

#### 2.11.4 数据模型

| 表名 | 用途 |
|------|------|
| `notifications` | 通知消息 |
| `user_notifications` | 用户通知关系 |
| `user_broadcast_read` | 用户广播已读记录 |

#### 2.11.5 域间依赖

相对独立，不依赖其他业务域。被 Space 域的邀请/申请流程触发通知。

---

### 2.12 WeChat 域（微信集成）

#### 2.12.1 业务能力

- 微信第三方平台回调处理
- Verify Ticket 刷新
- 授权事件处理（授权/更新授权/取消授权）
- 用户消息转发
- 公众号绑定管理

#### 2.12.2 API 端点

**WechatCallbackController** (`/api/wx`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST/GET | `/api/wx/callback` | 系统消息回调 |
| POST | `/api/wx/msg/{appId}` | 用户消息回调 |
| POST | `/api/wx/authCallback` | 前端授权回调 |
| POST | `/api/wx/test/set-verify-ticket` | 测试设置 ticket |

#### 2.12.3 核心服务

| 服务 | 职责 |
|------|------|
| `WechatThirdpartyService` | 微信第三方平台核心逻辑 |
| `BotOffiaccountService` | 公众号绑定管理 |

#### 2.12.4 数据模型

| 表名 | 用途 |
|------|------|
| `bot_offiaccount` | 公众号绑定信息 |
| `bot_offiaccount_chat` | 公众号对话 |
| `bot_offiaccount_record` | 公众号记录 |

#### 2.12.5 域间依赖

**被依赖的域：**
- Publish 域：`WechatThirdpartyService`（微信渠道发布）
- User 域：`BotOffiaccountService`（查询公众号绑定状态）

#### 2.12.6 外部集成
- 微信第三方平台 API（授权、消息加解密）
- Redis：Verify Ticket 缓存

---

### 2.13 Database 域（数据库资源管理）

#### 2.13.1 业务能力

- 数据库连接创建/编辑/删除/复制
- 数据库表创建/编辑/删除/复制
- 表字段管理
- 表数据查询/导入/导出
- 数据库连接测试

#### 2.13.2 API 端点

**DataBaseController** (`/db`)
| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/db/create` | 创建数据库 |
| GET | `/db/detail` | 数据库详情 |
| POST | `/db/update` | 编辑数据库 |
| GET | `/db/delete` | 删除数据库 |
| GET | `/db/copy` | 复制数据库 |
| POST | `/db/page-list` | 数据库列表 |
| POST | `/db/create-table` | 创建表 |
| POST | `/db/update-table` | 编辑表 |
| GET | `/db/delete-table` | 删除表 |
| GET | `/db/table-list` | 表列表 |
| POST | `/db/update-table-field` | 更新表字段 |
| GET | `/db/table-field-list` | 表字段列表 |
| POST | `/db/select-table-data` | 查询表数据 |
| GET | `/db/copy-table` | 复制表 |
| POST | `/db/import-table-data` | 导入表数据 |
| POST | `/db/export-table-data` | 导出表数据 |
| GET | `/db/table-template` | 获取表模板 |

#### 2.13.3 核心服务

| 服务 | 职责 |
|------|------|
| `DatabaseService` | 数据库全生命周期管理 |
| `CoreSystemService`（extra） | 核心系统服务（数据库连接） |

#### 2.13.4 数据模型

| 表名 | 用途 |
|------|------|
| `db_info`（DbInfo） | 数据库连接信息 |
| `db_table`（DbTable） | 数据库表信息 |
| `db_table_field`（DbTableField） | 表字段信息 |

**关系表：**
| 表名 | 连接域 |
|------|--------|
| `flow_db_rel` | Workflow ↔ Database |

#### 2.13.5 域间依赖

相对独立，通过 `flow_db_rel` 与 Workflow 域关联。

#### 2.13.6 外部集成
- 外部数据库连接（JDBC）
- 核心系统 API：`CoreSystemService`


---

### 2.14 辅助域

#### Homepage 域（首页/广场）

**AgentSquareController** (`/home-page/agent-square`)
| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/home-page/agent-square/get-bot-type-list` | 获取智能体分类列表 |
| GET | `/home-page/agent-square/get-bot-page-by-type` | 按分类分页获取智能体 |

服务：`AgentSquareService` — 依赖 `BotTypeListService`、`ChatBotMarketService`、`UserInfoDataService`、`BotFavoriteService`、`ChatListDataService`

#### Common 域（公共服务）

**ConfigInfoController** (`/config-info`)：配置信息查询（按分类、按编码）
**ImageController** (`/image`)：图片上传到 S3
**LLMController** (`/llm`)：LLM 模型查询

#### OpenAPI 域

**OpenApiController** (`/open-api`)：外部 API 集成，通过 API Key 认证获取工作流 IO 转换数据

#### Extra 域

**RtasrController** (`/rtasr`)：实时语音识别签名获取
**SparkChatController**：Spark 对话（如存在）

#### 其他基础设施

| 组件 | 路径 | 功能 |
|------|------|------|
| `S3Controller` | `/api/s3/presign` | S3 预签名 URL |
| `HealthController` | 健康检查 | 服务健康状态 |
| `TextNodeConfigController` | `/textNode/config` | 文本节点配置管理 |
| `WorkflowChatController` | `/api/v1/workflow` | 工作流对话（基于 AgentClient） |

---

## 三、跨域关系分析

### 3.1 Service 注入依赖矩阵

以下矩阵展示各域 Service 之间的依赖关系（行依赖列）：

| 依赖方 ↓ \ 被依赖方 → | Bot | Chat | Workflow | Space | Knowledge | Publish | Tool | Model | User | Share | Notification | WeChat | Database |
|------------------------|-----|------|----------|-------|-----------|---------|------|-------|------|-------|-------------|--------|----------|
| **Bot** | ● | ✓ | ✓ | - | ✓ | - | - | ✓ | ✓ | - | - | - | - |
| **Chat** | ✓ | ● | ✓ | - | ✓ | - | - | ✓ | - | - | - | - | - |
| **Workflow** | ✓ | ✓ | ● | - | - | - | - | - | - | - | - | - | - |
| **Space** | ✓ | - | - | ● | - | - | - | - | ✓ | - | - | - | - |
| **Knowledge** | ✓ | - | - | - | ● | - | - | - | - | - | - | - | - |
| **Publish** | ✓ | - | ✓ | - | - | ● | - | - | - | - | - | ✓ | - |
| **Tool** | ✓ | - | ✓ | - | - | - | ● | - | - | - | - | - | - |
| **Model** | - | - | - | - | - | - | - | ● | - | - | - | - | - |
| **User** | ✓ | ✓ | ✓ | - | ✓ | ✓ | - | - | ● | - | - | ✓ | - |
| **Share** | ✓ | ✓ | - | - | - | - | - | - | - | ● | - | - | - |
| **Homepage** | ✓ | ✓ | - | - | - | - | - | - | ✓ | - | - | - | - |

说明：● = 自身，✓ = 存在依赖，- = 无依赖

### 3.2 高频被依赖服务（共享服务）

以下服务被多个域共享，是跨域耦合的关键点：

| 服务 | 被依赖的域 | 分析 |
|------|-----------|------|
| `ChatBotDataService` | Bot、Chat、Workflow、Space、Publish、Share、User、Homepage | **最核心的共享服务**，提供智能体数据查询 |
| `UserLangChainDataService` | Bot、Workflow、Publish | 工作流链数据，连接 Bot 和 Workflow |
| `UserInfoDataService` | Bot、Space、Homepage | 用户信息查询 |
| `ChatListDataService` | Bot、Chat、Homepage | 对话列表数据 |
| `BotFavoriteService` | Bot、User、Homepage | 收藏功能 |
| `ChatListService` | Chat、Share、User | 对话列表业务 |
| `ConfigInfoService` | Knowledge、Tool、Model | 配置信息 |
| `S3Util` | Knowledge、Tool、Model、Common | S3 文件操作 |
| `DataPermissionCheckTool` | Knowledge、Tool、Model | 数据权限检查 |

### 3.3 关系表连接分析

| 关系表 | 连接的域 | 说明 |
|--------|---------|------|
| `bot_repo_rel` | Bot ↔ Knowledge | 智能体关联知识库 |
| `bot_tool_rel` | Bot ↔ Tool | 智能体关联工具 |
| `bot_flow_rel` | Bot ↔ Workflow | 智能体关联工作流 |
| `flow_repo_rel` | Workflow ↔ Knowledge | 工作流关联知识库 |
| `flow_tool_rel` | Workflow ↔ Tool | 工作流关联工具 |
| `flow_db_rel` | Workflow ↔ Database | 工作流关联数据库 |

### 3.4 事件驱动关系

| 事件 | 发布方 | 消费方 |
|------|--------|--------|
| `BotPublishStatusChangedEvent` | Publish 域 | 监听器处理发布状态变更 |
| `PublishChannelUpdateEvent` | Publish 域 | 监听器处理渠道更新 |
| `UserNicknameUpdatedEvent` | User 域 | `UserNicknameUpdateEventListener` 更新关联数据 |

### 3.5 潜在循环依赖

1. **Bot ↔ Chat**：`BotServiceImpl` 依赖 `ChatListDataService`，`BotChatServiceImpl` 依赖 `BotService`
2. **Knowledge 内部**：`KnowledgeRepoService` ↔ `FileInfoV2Service`（通过 `@Lazy` 解决）
3. **Knowledge ↔ Repo**：`RepoService` 依赖 `FileInfoV2Mapper`（`@Lazy`）


---

## 四、问题识别与边界模糊点

### 4.1 跨域职责混乱

| 问题 | 详情 | 影响 |
|------|------|------|
| `ChatBotDataService` 被过度共享 | 被 8+ 个域直接依赖，成为"上帝服务" | 任何修改都可能影响全局 |
| `UserLangChainDataService` 跨域使用 | 本质是 Bot-Workflow 的桥梁，但被 Publish 等域直接使用 | 域边界模糊 |
| `BotController` 路径为 `/workflow` | 命名与实际职责不匹配 | 容易混淆 |
| `MyBotController` 混合了多域逻辑 | 同时调用 Bot、Chat、Knowledge、Publish 域的服务 | 应拆分为聚合查询 |
| `SpaceBizServiceImpl` 依赖 `ChatBotDataService` | 空间删除时清理智能体，跨域操作 | 应通过事件解耦 |

### 4.2 数据层问题

| 问题 | 详情 |
|------|------|
| Entity 分散在多个包 | `entity/bot/`、`entity/table/bot/`、`entity/chat/` 等，同一域的实体分散 |
| DTO/VO 命名不规范 | 部分 DTO 放在 `entity/dto/`、部分在 `dto/`，存在重复命名（如两个 `BotInfoDto`） |
| Mapper 与 Entity 不对应 | 部分 Mapper 在 `mapper/` 下，部分 Entity 在 `entity/table/` 下，对应关系不清晰 |
| MongoDB 实体已注释 | `Knowledge`、`PreviewKnowledge` 的 `@Document` 注解已注释，迁移到 MySQL |
| `data` 包与 `service/data` 包混淆 | 顶层 `data/` 包和 `service/data/` 包都有数据服务 |

### 4.3 架构层问题

| 问题 | 详情 |
|------|------|
| Service 层混合了 Biz 和 Data 职责 | 如 `SpaceBizService` vs `SpaceService`，命名不统一 |
| Controller 直接注入 Mapper | `BotCreateController` 直接注入 `BotTemplateMapper`，绕过 Service 层 |
| 缺少统一的防腐层 | 外部 API 调用散落在各 Service 中（MaaS、微信、讯飞等） |
| `@Resource` 和 `@RequiredArgsConstructor` 混用 | 部分类用 `@Resource` 字段注入，部分用构造器注入 |
| Handler 包职责不清 | 混合了外部调用处理器、用户信息管理、数据库处理器等 |
| `tool` 包（非 service/tool）混合了工具类和业务工具 | `DataPermissionCheckTool`、`FileUploadTool` 等是业务组件而非纯工具 |

### 4.4 域边界不清晰的区域

1. **Bot 与 Workflow 的边界**：`user_lang_chain_info` 表同时被 Bot 和 Workflow 域使用，`BotController` 路径为 `/workflow`
2. **Chat 与 Bot 的边界**：`ChatBotDataService` 名义上在 Bot 域但被 Chat 域大量使用
3. **Knowledge 与 Repo 的边界**：`KnowledgeRepoService` 在 `service/repo/` 下但管理知识条目
4. **User 域过于薄弱**：`MyBotController` 实际上是 Bot 域的查询聚合，不应在 User 域
5. **Publish 域与 Bot 域的边界**：发布状态存储在 `chat_bot_market` 表中（Bot 域的表）

### 4.5 需要下沉到 Commons 的基础设施

| 组件 | 当前位置 | 建议 |
|------|---------|------|
| `S3Util` | hub/util | 已部分在 commons（`S3ClientUtil`），需统一 |
| `RedisUtil` | hub/util | 下沉到 commons |
| `DataPermissionCheckTool` | hub/tool | 下沉到 commons |
| `RequestContextUtil` | 已在 commons | 保持 |
| `SpaceInfoUtil` | hub/util/space | 考虑下沉 |
| `BotPermissionUtil` | hub/util | 考虑抽象为通用权限检查 |
| `MaasUtil` | hub/util | 应封装为防腐层服务 |

---

## 五、建议的限界上下文划分

基于以上分析，建议将 hub 模块重构为以下限界上下文（Bounded Context）：

### 5.1 核心域（Core Domain）

#### BC1: Agent Context（智能体上下文）
- **包含**：Bot 域的核心 CRUD、智能体配置、人设、Prompt
- **聚合根**：`Agent`（当前的 `ChatBotBase`）
- **关键表**：`chat_bot_base`、`chat_bot_prompt_struct`、`personality_config`、`bot_template`
- **边界**：只负责智能体本身的生命周期，不包含市场、发布、对话

#### BC2: Conversation Context（对话上下文）
- **包含**：Chat 域全部、对话文件管理
- **聚合根**：`Conversation`（当前的 `ChatList`）
- **关键表**：`chat_list`、`chat_tree_index`、`chat_req_records`、`chat_resp_records`、`chat_file_user`
- **边界**：对话的创建、消息收发、历史记录、文件管理

#### BC3: Workflow Context（工作流上下文）
- **包含**：Workflow 域全部、工作流版本、工作流调试
- **聚合根**：`Workflow`
- **关键表**：`user_lang_chain_info`、`workflow_version`、`flow_protocol_temp`
- **边界**：工作流编排、版本管理、调试运行

### 5.2 支撑域（Supporting Domain）

#### BC4: Knowledge Context（知识库上下文）
- **包含**：Knowledge/Repo 域全部
- **聚合根**：`Repository`、`KnowledgeEntry`
- **关键表**：`repo`、`file_info_v2`、`knowledge`、`file_directory_tree`
- **边界**：知识库管理、文件处理、知识检索

#### BC5: Publishing Context（发布上下文）
- **包含**：Publish 域、发布策略、渠道管理
- **聚合根**：`PublishRecord`
- **关键表**：`flow_release_channel`、`mcp_data`、`chat_bot_market`（发布相关字段）
- **边界**：多渠道发布、发布统计、版本发布

#### BC6: Tool Context（工具上下文）
- **包含**：Tool 域（ToolBox + RPA）
- **聚合根**：`ToolBox`、`RpaAssistant`
- **关键表**：`tool_box`、`rpa_info`、`rpa_user_assistant`
- **边界**：工具/插件管理、RPA 管理

#### BC7: Model Context（模型上下文）
- **包含**：Model 域全部
- **聚合根**：`ModelConfig`
- **关键表**：`base_model_map`、`bot_model_bind`
- **边界**：模型注册、配置、授权

### 5.3 通用域（Generic Domain）

#### BC8: Identity & Access Context（身份与访问上下文）
- **包含**：Space 域 + User 域 + 权限管理
- **聚合根**：`Space`、`Enterprise`、`User`
- **关键表**：`agent_space`、`agent_enterprise`、`user_info`、权限表
- **边界**：多租户管理、用户管理、权限控制、邀请/申请

#### BC9: Notification Context（通知上下文）
- **包含**：Notification 域
- **聚合根**：`Notification`
- **关键表**：`notifications`、`user_notifications`
- **边界**：消息通知

#### BC10: Integration Context（集成上下文）
- **包含**：WeChat 域、OpenAPI 域、外部 API 调用
- **聚合根**：无（防腐层）
- **边界**：外部系统集成的防腐层

#### BC11: Database Resource Context（数据库资源上下文）
- **包含**：Database 域
- **聚合根**：`DatabaseConnection`
- **关键表**：`db_info`、`db_table`、`db_table_field`
- **边界**：数据库资源管理

### 5.4 共享内核（Shared Kernel）

以下组件应作为共享内核，被所有限界上下文使用：

- `commons` 模块（已存在）：`ApiResult`、`ResponseEnum`、`BusinessException`、`RequestContextUtil`
- 配置信息服务：`ConfigInfoService`
- 文件存储服务：`S3Util` / `S3ClientUtil`
- 权限检查：`DataPermissionCheckTool`
- 空间上下文：`SpaceInfoUtil`
- Redis 工具：`RedisUtil`

### 5.5 上下文映射关系

```
Agent Context ──── [共享内核] ──── Conversation Context
      │                                    │
      │ [客户-供应商]                       │ [客户-供应商]
      ▼                                    ▼
Workflow Context ◄──── [合作关系] ────► Knowledge Context
      │                                    │
      │ [客户-供应商]                       │
      ▼                                    │
Publishing Context ◄── [防腐层] ──── Integration Context
      │
      │ [客户-供应商]
      ▼
Tool Context

Identity & Access Context ──── [开放主机服务] ──── 所有上下文
```

**关键映射说明：**
- Agent ↔ Conversation：通过 `botId` 关联，Conversation 是 Agent 的客户
- Agent ↔ Workflow：通过 `user_lang_chain_info` 关联，共享 `flowId`
- Agent ↔ Knowledge：通过 `bot_repo_rel` 关联
- Agent ↔ Tool：通过 `bot_tool_rel` 关联
- Publishing ↔ Integration：通过防腐层调用微信、MCP 等外部服务
- Identity & Access：作为开放主机服务，为所有上下文提供身份和权限信息

---

## 六、重构优先级建议

### 第一阶段：解耦核心共享服务
1. 拆分 `ChatBotDataService` 为域内查询接口 + 跨域查询接口
2. 将 `user_lang_chain_info` 的管理权明确归属 Workflow Context
3. 统一 Entity/DTO/VO 的包结构

### 第二阶段：建立限界上下文边界
1. 按 BC 划分模块/包结构
2. 引入域事件替代直接跨域调用（如空间删除 → 发布 BotCleanupEvent）
3. 建立防腐层封装外部 API 调用

### 第三阶段：数据模型重构
1. 将 `chat_bot_market` 中的发布相关字段迁移到 Publishing Context
2. 统一关系表的管理归属
3. 清理已废弃的 MongoDB 相关代码

### 第四阶段：基础设施下沉
1. 将通用工具类下沉到 commons
2. 统一依赖注入方式（全部使用构造器注入）
3. 消除 Controller 直接注入 Mapper 的情况

---

## 七、附录

### 7.1 完整 DB 表清单

共计 **80+** 张表，按域分类：

**Bot 域（20 张）：** `chat_bot_base`, `chat_bot_market`, `chat_bot_list`, `chat_bot_tag`, `chat_bot_prompt_struct`, `chat_bot_remove`, `bot_template`, `bot_type_list`, `bot_favorite`, `bot_dataset`, `bot_dataset_maas`, `bot_model_bind`, `bot_model_config`, `bot_repo_subscript`, `user_lang_chain_info`, `user_lang_chain_log`, `personality_config`, `personality_category`, `personality_role`, `bot_conversation_stats`, `bot_chat_file_param`, `custom_speaker`, `pronunciation_person_config`, `take_off_list`

**Chat 域（13 张）：** `chat_list`, `chat_tree_index`, `chat_req_records`, `chat_resp_records`, `chat_req_model`, `chat_resp_model`, `chat_reason_records`, `chat_reanwser_records`, `chat_trace_source`, `chat_token_records`, `chat_resp_alltool_data`, `chat_file_user`, `share_chat`, `share_qa`

**Workflow 域（10 张）：** `workflow_version`, `workflow_config_entity`, `workflow_dialog`, `workflow_feedback`, `workflow_node_history`, `workflow_comparison`, `flow_protocol_temp`, `flow_release_channel`, `flow_release_aiui_info`, `workflow_template_group`, `mcp_tool_config`, `prompt_template`

**Space 域（9 张）：** `agent_space`, `agent_space_user`, `agent_space_permission`, `agent_enterprise`, `agent_enterprise_user`, `agent_enterprise_permission`, `agent_invite_record`, `agent_apply_record`, `application_form`

**Knowledge 域（7 张）：** `repo`, `file_info_v2`, `file_directory_tree`, `knowledge`, `preview_knowledge`, `hit_test_history`, `dataset_info`, `dataset_file`, `req_knowledge_records`

**Tool 域（7 张）：** `tool_box`, `tool_box_feedback`, `tool_box_operate_history`, `user_favorite_tool`, `rpa_info`, `rpa_user_assistant`, `rpa_user_assistant_field`

**Eval 域（16 张）：** `effect_eval_dimension_v2`, `effect_eval_dimension_template`, `effect_eval_scene`, `effect_eval_set`, `effect_eval_set_ver`, `effect_eval_set_ver_data`, `effect_eval_task`, `effect_eval_task_data`, `effect_eval_task_online_data`, `effect_eval_task_online_log`, `effect_eval_task_report`, `effect_eval_task_unfinished`, `effect_eval_task_node_mark_data`, `effect_eval_task_node_score_data`, `effect_eval_set_ver_excel_headers`, `effect_eval_set_ver_excel_data_values`, `effect_model_optimize_task`, `train_set`, `train_set_ver`, `train_set_ver_data`, `user_thread_pool_config`

**Publish 域（3 张）：** `flow_release_channel`, `mcp_data`, `app_mst`

**Notification 域（3 张）：** `notifications`, `user_notifications`, `user_broadcast_read`

**WeChat 域（3 张）：** `bot_offiaccount`, `bot_offiaccount_chat`, `bot_offiaccount_record`

**Database 域（3 张）：** `db_info`, `db_table`, `db_table_field`

**关系表（6 张）：** `bot_repo_rel`, `bot_tool_rel`, `bot_flow_rel`, `flow_repo_rel`, `flow_tool_rel`, `flow_db_rel`

**其他（5 张）：** `config_info`, `user_info`, `system_user`, `base_model_map`, `fine_tune_task`, `auth_apply_record`, `call_log`, `chat_info`, `feedback_info`, `node_info`, `vcn_info`

**Prompt 相关（4 张）：** `ai_prompt_template`, `xingchen_official_prompt`, `xingchen_prompt_manage`, `xingchen_prompt_version`

**分享（1 张）：** `agent_share_record`

### 7.2 Eval 域说明

Eval（效果评估）是一个**独立的尚未上线功能**。当前代码中有大量 Eval 相关的实体和 Mapper（16+ 张表），但没有对应的 Controller。在 DDD 重构时应作为独立的限界上下文（BC12: Evaluation Context）预留位置，待功能上线时完善。

### 7.3 文件统计

| 类型 | 数量 |
|------|------|
| Controller | 48 个文件 |
| Service（接口+实现） | 100+ 个文件 |
| Entity | 120+ 个文件 |
| DTO | 60+ 个文件 |
| Mapper | 40+ 个文件 |
| 其他（Handler/Tool/Config/Util 等） | 50+ 个文件 |
| **总计** | **420+ 个 Java 文件** |
