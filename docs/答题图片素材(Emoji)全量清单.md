# 答题图片素材（Emoji）全量清单

> 用途：迁移到其他系统（安卓端 / 换字体 / 换素材）时，精确知道哪些 emoji 出现在哪个文件、哪个模块，以及哪些有跨平台渲染风险。
> 扫描范围：`QizhiTrainingPlatform-Portable/js/*.js` + `index.html` + `css/*.css`（排除 `_internal` Python 运行时、`runtime` Ollama 二进制、`SHA256SUMS.txt`）。

---

## 一、总览与结论

1. **没有任何外部图片文件**（无 `.png/.jpg/.svg/.gif/.webp/.ico`），CSS 里也没有 `url()` / `background-image` / `<img>` 引用。
2. 所有"图片素材"都是 **Unicode Emoji 字符**，直接写在 JS 字符串字面量里，由操作系统/WebView 的彩色 emoji 字体渲染（Windows=Segoe UI Emoji，Android=Noto Color Emoji，iOS=Apple Color Emoji）。
3. 涉及 **13 个 JS 文件 + index.html**。其中真正作为"题目内容"（儿童必须识别的目标/选项）的只有 3 个文件：

| 文件 | 角色 | 说明 |
|---|---|---|
| `js/onboarding-assessment.js` | 首次评估 24 题（彩虹岛基线） | 目标+选项全是 emoji |
| `js/question-logic-v2.js` | 22 模块日常题库 + 6 题兜底题库 | 目标+选项全是 emoji |
| `js/level-curriculum.js` | 20 关地图的"玩法类型"图标 | 仅图标，非题目内容 |
| `js/rubric-alignment.js` | 3 个兴趣频道入口图标 | 仅图标 |
| 其余 9 个文件 | 界面功能图标 / 提示语点缀 | 装饰性，迁移风险低 |

---

## 二、核心题目素材清单（迁移必须全覆盖）

### 2.1 首次评估 24 题 —— `onboarding-assessment.js` `BASELINE_GAMES`（[第 9–26 行](QizhiTrainingPlatform-Portable/js/onboarding-assessment.js#L9-L26)）

| 域 | 题目 | 目标(target) | 选项(choices) | 备注 |
|---|---|---|---|---|
| A 注意 | 找找小动物 | 🐶 | 🐱 🐶 🐰 🐼 | |
| A 注意 | 颜色小侦探 | 🍎 | 🥦 🍎 🫐 🍌 | 红=🍎 |
| A 注意 | 谁不一样 | ⭐ | 🌙 🌙 ⭐ | 允许重复选项 |
| B 记忆 | 记忆宝盒 | 🍓🚗 | 🍓🚗 / 🚗🌙 / 🍌🚲 | preview `['🍓','🚗']` |
| B 记忆 | 顺序小火车 | 🌞🌳🏠 | 🏠🌳🌞 / 🌞🌳🏠 / 🌳🌞🏠 | preview `['🌞','🌳','🏠']` |
| B 记忆 | 记住最后一个 | 🦋 | 🐸 🦋 🐟 | preview `['🐟','🐸','🦋']` |
| C 逻辑 | 生活排序 | 🪥 | ⚽ 🪥 🎨 | scene `🛏️🌙` |
| C 逻辑 | 分类小能手 | 🚗 | 🍎 🍌 🚗 | |
| C 逻辑 | 接着排一排 | 🌙 | ☀️ 🌙 ⭐ | scene `☀️ 🌙 ☀️ ❓` |
| D 语言 | 听懂小任务 | 🐦 | 🐟 🐦 🐢 | |
| D 语言 | 需要什么呢 | 💧 | 🧸 💧 👟 | |
| D 语言 | 谁在做什么 | 🐱💤 | 🐱💤 / 🐱⚽ / 🐶🍎 | |
| E 社会情绪 | 表情猜猜看 | 😊 | 😢 😊 😠 | |
| E 社会情绪 | 轮到谁啦 | 🙋 | 💢 🙋 🏃 | |
| E 社会情绪 | 一起看哪里 | ☁️ | 👟 ☁️ 🍽️ | scene `🧒👉☁️` |
| F 生活适应 | 生活百宝箱 | 🪥 | 🪥 🧦 🥄 | |
| F 生活适应 | 动作模仿 | ✅ | ✅ | scene `👏 🙌 👏` |
| F 生活适应 | 穿衣小帮手 | 👕 | 🛏️ 👕 🛁 | scene `🚪🌤️` |

庆祝动画素材（[第 50 行](QizhiTrainingPlatform-Portable/js/onboarding-assessment.js#L50)）：
`['⭐','✨','🌈','🎈','💛']` `['🎉','🌟','🫧','🦋','🍀']` `['🚀','⭐','☀️','🎵','💫']`

### 2.2 22 模块日常题库 —— `question-logic-v2.js` `MODULE_ACTIVITY_BANK`（[第 8–29 行](QizhiTrainingPlatform-Portable/js/question-logic-v2.js#L8-L29)）

| 模块 | 用到的 emoji |
|---|---|
| P01 颜色 | 🔴 🟡 🔵 🟢 🟠 🟣 |
| P02 图形 | ● ■ ▲ ★ ◆（**几何符号，非 emoji**） |
| P03 异同辨别 | ⭐ 🌙 🍎 🍐 🐶 🐱 |
| P04 听指令 | 🐶 🐱 🐰 🐼 🐦 🐟 🐢 🐸 🚗 🚲 🚌 ✈️ |
| M01 关联记忆 | 🪥 👟 🚗 🎈 🧦 🥄 🧸 🍎 ☂️ ⚽ 📕 🌧️ |
| M02 视觉再认 | 🍓 🚗 🌙 🧸 🚲 🐶 🍌 ⭐ 🌻 🍎 🚌 🐟 |
| M03 序列记忆 | ☀️ 🌙 ⭐ 🍎 🚗 🍌 🚲 🐶 🐟 |
| M04 工作记忆 | 🐟 🐸 🦋 🐰 🍎 🚗 🌙 ⭐ ☀️ ☁️ 🌧️ |
| L01 命名 | 🍎 🐶 🚗 |
| L02 看图造句 | 🐱 💤 ⚽ 🍎 |
| L03 理解指令 | 🐦 🐟 🐢 🐶 🍎 🚗 👟 🥛 🧦 ⚽ 📕 |
| L04 功能性沟通 | 💧 🧸 👟 🚗 🛏️ ⚽ 🍎 🚲 🚻 🎨 🎈 📕 |
| E01 生活问题解决 | ☂️ 🧢 🪥 🥄 🍚 ⚽ 🛏️ 🎨 🧥 🩳 泳圈 扇子 |
| E02 分类 | 🚗 🍎 🍌 🍓 📕 🐶 🐱 🐟 🥄 👕 🧦 👖 |
| E03 数量 | 🍎 ⭐ 🍌（用 emoji 组合表示 1~4 个） |
| E04 排序 | 🪥 ⚽ 🎨 🚲 🚰 👟 |
| S01 情绪识别 | 😊 😢 😠 😴 😮 😄 |
| S02 社交互动(观察) | 👏 ↔️ ⏳ 🧑 👉 ⭐ |
| S03 社交规则 | 🙋 💢 🏃 🙅 👋 🙈 ⏳ |
| D01 生活自理 | 🚰 👕 洗手 |
| D02 精细动作 | ⭐ 🌙 ☁️ ☀️ 🌼 🍎 🚗 🐟 ⚽ 🎈 📕 🧸 |
| D03 动作(线下) | 👏 🙌 ⬇️ |

> 说明：`🪥膏` 是「牙刷 emoji + 汉字"膏"」拼接（[第 12 行](QizhiTrainingPlatform-Portable/js/question-logic-v2.js#L12)），用于"哪个和牙刷一起用"的关联题，不是独立 emoji。

### 2.3 兜底题库（LEGACY 6 题）—— `question-logic-v2.js`（[第 126–131 行](QizhiTrainingPlatform-Portable/js/question-logic-v2.js#L126-L131)）

👀 🧠 🧩 💬 😊 👐（领域图标）+ 题目内：🏠 🚗 🌳 💧 ☀️ 🎈 🥛 👟 ⚽

### 2.4 关卡/兴趣/玩法图标（非题目内容，仅分类标签）

**20 关地图玩法类型** `level-curriculum.js`（[第 3–6 行](QizhiTrainingPlatform-Portable/js/level-curriculum.js#L3-L6)）：
🎨 颜色 · 🔷 图形 · 👆 选择 · 🎧 听声音 · 🧩 配对 · 🧠 记忆 · 🚂 排顺序 · 🧺 分类 · 💬 说一说 · 🤝 跟着做 · ⭐ 点一点

**20 关大地图图标** `CURRICULUM_ICON`（[第 10 行](QizhiTrainingPlatform-Portable/js/level-curriculum.js#L10)）：
🌱 👀 🎁 🎧 🌈 🧩 🚂 🏠 😊 🌈 🧺 👂 💬 🤝 🌈 🗺️ 🧠 🏆 🚀 🏝️

**3 个兴趣频道** `rubric-alignment.js`（[第 7–9 行](QizhiTrainingPlatform-Portable/js/rubric-alignment.js#L7-L9)）：
🎨 颜色世界 · 🔷 图形乐园 · 🎵 声音森林

---

## 三、全量文件清单（含界面功能图标）

| 文件 | 出现的 emoji | 用途 |
|---|---|---|
| `index.html`（[25–26 行](QizhiTrainingPlatform-Portable/index.html#L25-L26)） | ⭐ ❤ | 登录角色选择（儿童/家长） |
| `js/onboarding-assessment.js` | 见 2.1 + 🎮 🔊 💡 🙈 👀 ✓ 🦊 🔥 🌱 ➜ 🤖 ⛏ 等 | 首次评估题目 + 流程 UI |
| `js/question-logic-v2.js` | 见 2.2/2.3 + 🎮 🔊 💡 🌟 🌱 🌈 | 题库 + 答题 UI |
| `js/level-curriculum.js` | 🎨🔷👆🎧🧩🧠🚂🧺💬🤝⭐ + 🎮🔁🎉🌈🏝️🔒👍 | 20 关地图 + 玩法图标 |
| `js/rubric-alignment.js` | 🎨 🔷 🎵 ⭐ 🌱 🎮 🔒 + ★☆ | 兴趣频道 + 积分奖励 |
| `js/rehabilitation-methods.js` | ⭐ 🗓️ 🌳 🧘 💬 👐 🏃 🎵 📖 🖼️ 🧠 🏠 🧩 ⚠️ | 12 类康复方法图标 |
| `js/safety-governance.js` | 🛡️ 🛑 ⛔ 🧑🤝🧑 | 安全门/暂停提示 |
| `js/assessment-framework.js` | 💬 😊 👐 | 家庭观察表单 3 个领域 |
| `js/profile-agent.js` | 🧠 🌟🎉 🌱💛 🌈 🎮 🔊 💡 | 个性化训练（Agent 出题）UI |
| `js/scanner-import.js` | 📚 🔒 📄 ⏳ ⚠️ ✓ 🧠 ✨ ⛔ | 扫描档案导入工作台 |
| `js/ui-role-experience.js` | ⚙️ 🌱 🔎 🐢 🔊 🗣️ 🔉 ▶ 🌿 🏠💛 ✅ 📈 ⛔ 🌤️ 💡 🛑 🧸 👀 ⭐ 🔁 🏢🛡️ | 三端口首页/舒适设置 |
| `js/children.js` | 🔎 | 儿童档案搜索 |
| `js/clinical-workflow.js` | 📋 ✅ ⏳ | 临床工作流入口/状态 |
| `js/enhanced.js` | ★ ☆（**dingbat 星星，非 emoji**） | 进度星级显示 |
| `js/baseline-logic-v2.js` | ⏸️ 🔊 💡 ✓ 🦊🎉 🔥 🦊🌱 | 基线流程 UI（复用评估组件） |

---

## 四、去重后的全量 Emoji 汇总（按语义分类）

**动物**：🐶 🐱 🐰 🐼 🐦 🐟 🐢 🐸 🦋 🐯?（无）→ 共 9 个：🐶🐱🐰🐼🐦🐟🐢🐸🦋

**水果/蔬菜/食物**：🍎 🍌 🍓 🫐 🍐 🥦 🍚 🥛

**衣物/鞋帽**：👕 👖 👟 🧦 🧢 🧥 🩳

**日常用品**：🪥 🥄 🧸 📕 📖 🎒 🎈 🎨 ⚽ 🎁 ⚽

**交通工具**：🚗 🚲 🚌 ✈️ 🚂 🚀

**自然/天气**：🌙 ☀️ 🌞 🌧️ ☁️ 🌳 🌻 🌼 ⭐ ✨ 🌱 🌿 🌈 🔥 🍀 💫 🌤️ 🏝️ 🗺️ 🏠

**表情**：😊 😢 😠 😴 😮 😄 🙈

**手势/人物**：👏 🙌 🙋 👋 👉 💢 🙅 🤝 🧑 🧒 🕺 🏃

**颜色色块**：🔴 🟡 🔵 🟢 🟠 🟣

**几何符号（非 emoji）**：● ■ ▲ ★ ◆

**功能/状态图标**：✅ ❌ ⚠️ ⛔ 🛑 🛡️ 🔒 🔎 💡 🔊 🔉 🗣️ ⏸️ ▶ ➜ 🔁 🎮 🎉 🎵 🎧 🧩 🧠 🧺 👀 👂 💬 🤖 ⚙️ 🏆 🏢 📋 📄 📈 📚 🗓️ 🖼️ 🧘 ❓ ↔️ ⬇️ ✓ ⏳ 🔢 🚻 🚰

**星星三兄弟（易混淆）**：⭐（emoji） / ★☆（dingbat） / 🌟（glowing star）

---

## 五、跨平台迁移风险清单（重点）

### 5.1 需要 VS16（emoji 变体选择符 `U+FE0F`）的符号 —— 最易"变成黑白/豆腐块"
这些字符本身是"文字符号"，源码里靠 `U+FE0F` 转成彩色 emoji。**如果迁移时丢掉了 VS16，会显示成黑白小符号**：

☀️ ☁️ ⚠️ ✅ ❌ ❓ ⏸️ 🛡️ 🛑 ⛔ 🗓️ 🗺️ 🏝️ 🖼️ ⏳ ⬇️ ↔️ ✈️ ⚙️ 🌤️ ▶️

> 源码里部分已显式带 VS16（如 `☀️`、`🗺️`、`🏝️`、`🛏️`、`🪥`），部分省略（如 `⚠️` 写作 `⚠`）。迁移时建议统一显式补上 VS16。

### 5.2 ZWJ 组合序列 —— 老字体/老系统可能拆开显示
- 🧑🤝🧑（安全门里"线下活动"图标，`safety-governance.js:65`）—— 由 🧑 + ZWJ + 🤝 + ZWJ + 🧑 组成
- 👏 ↔️ 👏 之类是"字符+空格+字符"，不是 ZWJ，风险低

### 5.3 Unicode 13/14 较新 emoji —— 旧 Android 显示为豆腐块 □（最高风险）
这些字符出现于 Unicode 13.0（2020）之后，**Android 10 及更早、旧版 Noto Color Emoji 会缺字形**：

| Emoji | Unicode 版本 | 出现处 |
|---|---|---|
| 🪥 牙刷 | 13.0 | 评估题、M01、E04 |
| 🫐 蓝莓 | 13.0 | 评估"颜色小侦探" |
| 🫧 泡泡 | 14.0 | 评估庆祝动画 |
| 🩳 短裤 | 12.0 | E01"天冷了穿什么" |
| 🧦 袜子 | 11.0 | 评估、M01 |
| 🥦 西兰花 | 11.0 | 评估"颜色小侦探" |
| 🧥 外套 | 11.0 | E01 |

> 若目标系统（如安卓平板）系统版本较低，**🪥 🫐 🫧** 是最可能显示失败、直接影响答题的三个。

### 5.4 几何符号 —— 最安全
P02 形状题用 ● ■ ▲ ★ ◆，是普通 Unicode 符号（非 emoji），任何系统、任何字体都能正确渲染，**建议迁移时优先保留**。

### 5.5 人物/手势 —— 样式差异大，但不会"显示失败"
👏 🙌 🙋 👋 🧑 👉 在不同平台皮肤色、画风不同（Apple 拟真 vs Google 扁平 vs Microsoft 扁平），但都能显示。若追求儿童端观感统一，可考虑替换为自绘 SVG（与旧三大模块的 `utils.js ICONS` 一致）。

---

## 六、替换/迁移建议

1. **保留 emoji 方案**：目标系统 Android 8.0+ 且自带较新 Noto Color Emoji 即可；旧版本需打包字体（Noto Color Emoji，约 10–20MB）或用系统 WebView 的字体回退。
2. **按风险分级处理**：
   - 低风险（可直接保留）：几何符号、色块 🔴🟡🔵🟢🟠🟣、常见动物/水果。
   - 中风险（补 VS16）：5.1 列表，统一显式加 `U+FE0F`。
   - 高风险（替换或捆绑字体）：🪥 🫐 🫧（Unicode 13/14）。
3. **如需完全摆脱 emoji 依赖**：把题目素材从"emoji 字符"换成自绘 SVG/PNG，需要改 3 个文件的数据结构——`onboarding-assessment.js` 的 `BASELINE_GAMES`、`question-logic-v2.js` 的 `MODULE_ACTIVITY_BANK` 和 `LEGACY_ACTIVITY_BANK`（字段从字符串改成图片 key 即可，渲染函数 `qChoice/qMemory/qAudio/qObserved` 统一在 `question-logic-v2.js` 顶部）。
4. **一致性核对**：本清单可作为与"素材版权/风格统一"的对照底稿——emoji 本身版权归各字体厂商（Apple/Google/Microsoft），用于商业产品需确认字体授权。

---

*扫描方法：`\p{Extended_Pictographic}` + `\p{Emoji_Presentation}` 全量匹配 js/index/css 三个目录，逐行核对；日期 2026-09-27。*
