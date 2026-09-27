# Web 端与 Android 儿童端答题逻辑对比报告

## 1. 报告范围

本报告对比以下两套实现：

- 原 Web 端逻辑：`docs/儿童端答题逻辑与技术实现.md`
- 当前 Android 端实现：
  - `app/src/main/java/com/xingmou/ui/child/ChildScreen.kt`
  - `app/src/main/java/com/xingmou/XingmouViewModel.kt`
  - `app/src/main/java/com/xingmou/data/catalog/QuestionCatalog.kt`
  - `app/src/main/java/com/xingmou/core/domain/BaselineEngine.kt`
  - `app/src/main/java/com/xingmou/core/domain/CourseProgressEngine.kt`
  - `app/src/main/java/com/xingmou/core/domain/TrainingEngine.kt`
  - `app/src/main/java/com/xingmou/core/agent/AgentEventProcessor.kt`
  - `app/src/main/java/com/xingmou/data/db/Entities.kt`

当前 Android 端已完成“儿童端训练 / 设置”分栏，并将训练首页调整为“起点小测 + 关卡进度地图”；点击当前已解锁关卡后才进入答题界面。

## 2. 总体结论

当前 Android 端已经复现了 Web 端的主流程骨架：

```text
儿童进入
→ 起点小测
→ 课程地图
→ 选择已解锁关卡
→ 完成训练活动
→ 保存训练记录
→ 调整难度与支持等级
→ 更新进度并解锁后续内容
```

但两端还不是同一套答题标准。Android 当前更接近“本地可运行的简化版闭环”，而 Web 端是“包含提示、计时、分级判定、方案门控、动态课程和同步能力的完整训练闭环”。

最重要的差异有四项：

1. Android 正式课程目前仍由 UI 传入 `Boolean` 判定，第一个选项固定正确，尚未真正读取题库的 `correctOption`。
2. Android 起点小测是 6 题，Web 端是 24 题（6 个领域 × 4 题），两者产生的起点画像不可直接等价。
3. Web 端按“完成数 + 正确率”判断关卡通过，Android 目前按“正确完成过的题目 ID 数量”推进，缺少尝试次数和正确率门槛。
4. Web 端具有训练前安全门、提示分级、反应时间、题型差异和兴趣奖励链，Android 目前只有部分字段和基础规则，尚未形成完整交互。

因此，当前 Android 端可以作为独立的本地演示和开发基线，但如果目标是与原 Web 端功能对齐，还不能宣称“答题逻辑一致”。

## 3. 分项对比

### 3.1 首次评估 / 起点小测

| 对比项 | Web 端 | 当前 Android 端 | 结论 |
|---|---|---|---|
| 题量 | 24 题，6 个领域各 4 题 | 6 题，每个领域 1 题 | 明显不一致 |
| 题目选项 | 最多 3 项，选项可打乱 | 当前通常 2 项，未打乱 | 部分实现 |
| 记忆题 | 先预览约 1.8 秒，再显示选项 | 有 `MEMORY` 类型，但没有预览阶段 | 缺失 |
| 反应时间 | 真实计时，扣除重复播放时间 | `reactionMs` 模型存在，但基线未实际计时 | 缺失 |
| 重复播放 | 支持“再听一遍”并记录 `repeatCount` | 无重复播放按钮 | 缺失 |
| 提示 | 支持隐藏一个错误选项并记录 `hintCount` | 无提示操作 | 缺失 |
| 判定 | 普通题比较目标；观察题按是否完成判定 | 普通题按 `correctOption` 判定；观察题未形成独立交互 | 部分实现 |
| 结果字段 | `firstCorrect`、`promptLevel`、`repeatCount`、`hintCount`、`supportOutcome`、`responseTimeMs` 等 | 主要保存选择结果、正确性和过程答案 JSON | 部分实现 |
| 完成后动作 | 生成六域画像、方案、叙述、AI 审计和同步数据 | 写入本地 `AbilityProfileEntity`，无完整方案与叙述闭环 | 部分实现 |

Android 基线实现位置为 `BaselineEngine.kt`。它已经具备可恢复、可重新开始、过程记录和基础评分，但目前更像“六题起点检查”，不能等价替代 Web 端的 24 题彩虹岛起点评估。

### 3.2 课程地图和关卡入口

| 对比项 | Web 端 | 当前 Android 端 | 结论 |
|---|---|---|---|
| 关卡数量 | 20 关，每关 5 个小游戏 | 20 关，每关按 5 道题生成 | 基本一致 |
| 首屏形式 | 课程地图，显示进度与关卡入口 | 课程地图，显示进度条和关卡按钮 | 已对齐主交互 |
| 解锁顺序 | 可按起点画像弱项排序 | 按本地题库固定顺序 | 明显差异 |
| 动态课程 | Agent 课程优先，本地规则兜底 | 本地 `TaskCatalog` 固定生成 | 缺少动态课程 |
| 兴趣链 | 每日兴趣入口可置顶兴趣关卡 | 兴趣仅调整本地示例主题 | 部分实现 |
| 关卡进入 | 进入具体 `curriculumLevelId` 和 `curriculumRunId` | 当前只保存题目 ID，选中关卡主要由界面状态控制 | 部分实现 |
| 关卡进度 | 以关卡通过结果为核心 | 以全课程中已正确题目 ID 数量重新计算 | 口径不一致 |

当前 Android UI 已实现用户要求的“先看地图、点击关卡后答题”。但 `selectedCourseLevel` 目前是 `ChildScreen` 内的临时界面状态，尚未作为持久化训练会话字段写入数据库；实际答题仍由 `courseQuestionId` 决定下一题。

### 3.3 正式答题与题型

Web 端至少有以下题型：

- `choice`：普通点选；
- `memory`：短暂预览后点选；
- `audio`：语音播放后点选；
- `spoken`：儿童表达或观察完成；
- `guided`：跟随示范完成。

Android 题库已经定义 `CHOICE`、`MEMORY`、`SEQUENCE`、`OBSERVED`，但当前答题 UI 统一采用两个按钮：

```kotlin
ChoiceButton(option0, true, ...)
ChoiceButton(option1, false, ...)
```

这意味着：

- 第一个按钮被固定传入 `correct = true`；
- 第二个按钮被固定传入 `correct = false`；
- UI 没有根据题目 `correctOption` 动态判题；
- `MEMORY`、`SEQUENCE`、`OBSERVED` 尚未形成不同的交互流程；
- 没有 Web 端的预览、播放、跟读、观察完成等题型行为。

这是当前最需要优先修正的逻辑问题。题库虽然已有 `correctOption` 字段，但正式答题链路没有真正使用它。

### 3.4 计分、答题结果和关卡通过

Web 端的核心通过规则是：

```text
单模块：尝试次数 ≥ 3 且正确率 ≥ 60%
20 关课程：完成数 ≥ 5 且正确率 ≥ 60%
```

Web 端还区分：

- 独立首答正确；
- 重复听后答对；
- 使用提示后答对；
- 得到帮助后完成；
- 未完成。

当前 Android 端的 `CourseProgressEngine` 主要统计：

```text
correct == true 的 taskId 去重集合
```

只要某题出现过正确记录，就被视为该题完成；当前没有按关卡统计尝试总数、答题总数和正确率，也没有 60% 的通过门槛。因此：

- Android 端不容易出现 Web 端意义上的“做过但未通过”；
- 错误次数不会直接阻止最终完成，只会通过 Agent 影响支持和难度；
- 课程进度和 Web 端的“通过进度”不是同一口径。

### 3.5 自适应难度与支持等级

Web 端：

- 连续 2 次独立答对，难度上升 1 档；
- 连续 2 次错误，难度下降 1 档；
- 进入模块前还会参考已签署方案、复核方案、画像分数和最近 6 题加权结果；
- 选项数量随难度变化。

Android 端 `TrainingEngine`：

- 最近 3 条全部正确且首次正确时，难度上升 1 档；
- 最近 3 条中至少 2 条错误时，难度下降 1 档；
- 支持等级在最近 3 条出现 2 次错误时上调；连续稳定正确时下调；
- 难度和支持等级通过本地 Agent 事件处理器更新。

两端都有自适应思想，但触发窗口、首答定义和方案优先级不同。Android 当前 `firstCorrect = correct`，所以“首次答对”和“重试后答对”没有真正区分，导致自适应证据质量低于 Web 端。

### 3.6 训练记录字段

Web 端统一记录字段包括：

```text
id, childId, module, moduleId, domain, difficulty,
correct, firstCorrect, promptLevel, hintCount, repeatCount,
supportOutcome, reactionMs, errorType, completed,
source, questionId, activityId, activityType,
curriculumLevelId, curriculumRunId, ts
```

Android `TrainingRecordEntity` 当前主要字段为：

```text
recordId, childId, domain, taskId, difficulty,
supportLevel, reactionMs, errorType, firstCorrect,
correct, promptLevel, createdAt
```

Android 已有难度、支持、错误类型、反应时间等基础字段，但缺少：

- `hintCount`；
- `repeatCount`；
- `supportOutcome`；
- `completed`；
- `source`；
- `activityId` / `activityType`；
- `curriculumLevelId` / `curriculumRunId`。

这会影响后续的关卡复盘、Agent 审计、兴趣与能力区分以及 Web/Android 数据互导。

### 3.7 兴趣、奖励与鼓励

Web 端：

- 每天首次进入时选择颜色、图形或声音兴趣频道；
- 兴趣频道形成独立兴趣链；
- 完成一轮奖励 10 分；
- 50 分兑换徽章；
- 兴趣和能力明确分开解读。

Android 端：

- 提供“图片、动物、交通、生活用品”等静态主题；
- 主题当前主要调整占位素材，不改变课程路线；
- 小星星数量按成功答题数 × 10 计算；
- 完成整轮时记录 1 轮；
- 没有每日兴趣选择、兴趣链、徽章和幂等奖励记录。

因此 Android 的鼓励展示已经存在，但奖励语义与 Web 不一致，不能直接合并统计。

### 3.8 安全门和风险处理

Web 端在打开训练前执行 `trainingGate`，依次检查：

1. 儿童数据访问授权；
2. 是否存在活动安全风险；
3. 家长训练知情同意；
4. 是否存在已签署方案且当前模块在方案内；
5. 是否属于需要线下或混合执行的模块。

Android 端当前主要检查：

- 起点小测是否完成；
- 当前会话是否暂停或安全停止；
- 本地登录角色是否有端口权限；
- 答题后通过 `RiskEngine` 和连续失败规则决定是否暂停。

Android 目前没有在“进入关卡前”完成与 Web 等价的训练授权、活动风险、方案模块和线下模块门控。并且儿童点击答题时传入的 `TrainingCompletedEvent` 没有携带真实观察文本或风险标记，儿童端高危安全停止主要依赖后续事件输入，不能完全替代 Web 的前置安全门。

### 3.9 语音、提示和无障碍

Web 端提供：

- 题目和基线语音朗读；
- 重复播放；
- 正确/错误音效；
- 提示音和分级帮助；
- 高对比、大字号、减少动效；
- 语速与音量控制。

Android 端已经提供：

- TTS 朗读状态消息；
- 语速、音量设置；
- 大字体、高对比、慢动效；
- 训练暂停按钮。

仍缺少：

- 对题干和基线题目的完整朗读链路；
- 正确/错误音效；
- “再听一遍”；
- “提示一个选项”；
- 提示后答题的结果分类。

### 3.10 Agent、画像和数据同步

Web 端的 Agent 参与课程生成、个性化题目、计划生成、画像闭环、AI 推理审计和后端同步。

Android 端采用纯前端本地架构：

- Room 保存儿童、训练、Agent 事件、方案和审核数据；
- `AgentEventProcessor` 采用确定性本地规则；
- DeepSeek 是设备端可选能力，不是儿童答题的前置依赖；
- 儿童答题失败不会因为远程 AI 不可用而中断。

这符合当前“纯前端、设备端 BYOK”的产品方案，但意味着 Android 暂时不具备 Web 端的：

- 后端个性化课程生成；
- 多设备同步；
- Web/Android 共享训练会话；
- 基线完成后的完整 AI 叙述与推理审计闭环。

## 4. 当前 Android 端已实现内容

以下内容可以认为已经完成或基本完成：

- 儿童端独立入口；
- 六题起点小测的开始、继续、离开、重做；
- 基线答案和能力画像写入 Room；
- 20 关课程地图的数据模型；
- 训练首页先显示小测和关卡地图；
- 当前关卡按钮逐步解锁；
- 关卡总进度条；
- 点击当前关卡后进入答题；
- 答题事件写入 Room；
- 连续失败后的暂停；
- 难度和支持等级的本地规则调整；
- 儿童端安全停止状态展示；
- 训练设置、语音、字体、高对比和兴趣主题。

## 5. 当前 Android 端的关键缺口

### P0：必须优先修正

1. **改为题库驱动判题**
   - `ChoiceButton` 不再直接接收固定 `Boolean`；
   - 由当前题目的 `correctOption` 或题型判定函数统一计算结果；
   - 观察题、顺序题和记忆题需要分别定义判题规则。

2. **统一关卡通过口径**
   - 为每个关卡统计 attempts、completed、correct、accuracy；
   - 按“完成活动数 ≥ 5 且正确率 ≥ 60%”判断通过；
   - 保留“做过但未通过”的状态，避免只按正确题目 ID 去重。

3. **补齐真实首答和提示语义**
   - `firstCorrect` 不能简单等于本次 `correct`；
   - 增加提示次数、重复次数、支持结果和未完成状态；
   - 反应时间必须从展示题目开始计时，不能固定写入 1500ms。

4. **建立持久化关卡会话**
   - 保存 `curriculumLevelId`、`curriculumRunId` 和当前活动；
   - 从答题页返回地图后可恢复同一关卡；
   - 进程重启后不丢失当前关卡上下文。

### P1：完成主要功能对齐

1. 将基线题库扩展为 24 题，或明确 Android 采用“轻量基线”并建立独立版本号。
2. 增加 memory、audio、observed、sequence 的真实交互，而不是全部渲染为二选一。
3. 加入题目朗读、重复播放、提示一个错误选项和答题音效。
4. 增加训练前安全门：训练授权、当前风险、方案范围和线下模块状态。
5. 让课程顺序能够读取画像弱项，而不是固定按 `TaskCatalog` 顺序推进。
6. 统一 Android 与 Web 的训练记录字段，支持未来导入导出和审计回放。

### P2：体验和生态对齐

1. 增加每日兴趣选择和兴趣关卡链。
2. 将奖励从“成功题数积分”调整为“完成一轮奖励”，并增加幂等奖励记录和徽章。
3. 补齐基线完成后的叙述、画像版本和 Agent 推理审计。
4. 如果未来需要跨设备协同，再设计可选同步协议；当前纯前端方案不应默认引入服务端依赖。

## 6. 建议的 Android 对齐路线

### 阶段 1：修正答题正确性

- 新增统一 `QuestionEvaluator`；
- 所有选项、观察题、顺序题都通过题目定义计算结果；
- 删除 UI 层固定 `true / false` 判定；
- 增加题目级测试：正确选项、错误选项、观察完成、无效选择。

### 阶段 2：补齐训练记录

- 扩展 `TrainingRecordEntity`；
- 增加 `attemptIndex`、`hintCount`、`repeatCount`、`supportOutcome`、`completed`、`curriculumLevelId`、`curriculumRunId`；
- 为 Room 增加数据库迁移；
- 将反应时间从固定值改为真实计时。

### 阶段 3：重做关卡进度引擎

- 按关卡聚合记录，而不是只按正确题目 ID 去重；
- 计算每关的完成数、尝试数、正确率和是否通过；
- 按 Web 规则实现 5 活动和 60% 正确率门槛；
- 地图按钮只允许进入当前已解锁关卡。

### 阶段 4：补齐题型和无障碍

- memory：预览后再显示选项；
- sequence：按顺序选择或拖动；
- audio：播放题干并支持重复播放；
- observed：记录完成、帮助完成和未完成；
- 增加提示、答题音效和题干朗读。

### 阶段 5：安全门和 Agent 闭环

- 进入关卡前执行本地训练授权和风险检查；
- 将当前方案、支持等级和线下模块规则接入 `startCourse`；
- 训练后保存可回放的决策证据；
- DeepSeek 只作为可选解释/分析能力，不能替代确定性安全规则。

## 7. 验收标准

完成对齐后，至少应通过以下验收：

1. 正确答案不是第一个选项时，Android 能正确判定；
2. 题目选项顺序变化不会改变正确结果；
3. 同一关卡答题 3 次、正确 1 次时，不能直接判定关卡通过；
4. 完成 5 个活动且正确率达到 60% 后，下一关才解锁；
5. 重复播放和提示会被记录，并影响 `promptLevel` 与 `supportOutcome`；
6. 进程重启后，当前关卡和训练活动可以恢复；
7. 有活动风险或未满足训练授权时，不能进入答题页；
8. Web 导出的训练记录与 Android 导出的记录字段能够对应；
9. 儿童端在没有 DeepSeek Key、没有网络时仍可完成本地训练；
10. 所有儿童答题记录都能在本地 Agent 审计中回放。

## 8. 最终判断

当前 Android 端已经完成了产品界面和本地训练闭环的第一版，尤其是“起点小测 → 关卡地图 → 点击进入答题”的交互方向与原 Web 端一致。

但是，核心答题引擎仍是简化实现。若以原 Web 端为功能基准，当前最紧迫的不是继续增加视觉页面，而是先完成：

```text
题库驱动判题
→ 真实答题记录
→ 统一关卡通过条件
→ 真实题型交互
→ 训练前安全门
```

完成以上链路后，Android 端才具备与原 Web 端进行有效数据对比和功能验收的基础。
