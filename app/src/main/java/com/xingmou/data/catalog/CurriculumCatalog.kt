package com.xingmou.data.catalog

/**
 * 20 关彩虹冒险课程（对应 Web 端 js/level-curriculum.js）。
 *
 * 结构分两部分：
 *  - [levels]：20 个关卡的静态元数据（标题/图标/难度/通过规则），与儿童画像无关，直接写死。
 *  - [buildCurriculumLevels]：按儿童能力画像运行时生成每关的 5 个活动。
 *
 * 活动绑定一个模块（moduleId），题目正文复用 [QuestionCatalog.moduleQuestionBank]，
 * 按 questionVariant 轮换抽取该模块的 3 题，因此关卡层不是 100 道独立新题。
 */
object CurriculumCatalog {

    /** 关卡活动类型：仅用于儿童地图 UI 的图标/标签分类，与 QuestionType（评分/渲染类型）无关。 */
    data class ActivityTypeDefinition(val id: String, val emoji: String, val label: String)

    /** 关卡静态元数据；每关的 5 个活动由 [buildCurriculumLevels] 动态生成。 */
    data class CurriculumLevelDefinition(
        val levelId: String,
        val order: Int,
        val title: String,
        val icon: String,
        val difficulty: Int,
        val minCompleted: Int = 5,
        val minAccuracy: Double = 0.6
    )

    /** 关内单个活动：绑定一个模块，题目从该模块的 3 题里按 questionVariant 轮换抽取。 */
    data class CurriculumActivityDefinition(
        val activityId: String,
        val moduleId: String,
        val domain: String,
        val type: String,
        val label: String,
        val questionVariant: Int
    )

    /** 生成后的完整关卡：静态元数据 + 按画像生成的 5 个活动。 */
    data class GeneratedCurriculumLevel(
        val levelId: String,
        val order: Int,
        val title: String,
        val icon: String,
        val theme: String,
        val difficulty: Int,
        val focusDomains: List<String>,
        val activities: List<CurriculumActivityDefinition>,
        val minCompleted: Int = 5,
        val minAccuracy: Double = 0.6
    )

    /** 11 种活动类型（CURRICULUM_ACTIVITY_TYPES）。 */
    val activityTypes: List<ActivityTypeDefinition> = listOf(
        ActivityTypeDefinition("color", "🎨", "颜色"),
        ActivityTypeDefinition("shape", "🔷", "图形"),
        ActivityTypeDefinition("choice", "👆", "选择"),
        ActivityTypeDefinition("audio", "🎧", "听声音"),
        ActivityTypeDefinition("matching", "🧩", "配对"),
        ActivityTypeDefinition("memory", "🧠", "记忆"),
        ActivityTypeDefinition("sequence", "🚂", "排顺序"),
        ActivityTypeDefinition("sorting", "🧺", "分类"),
        ActivityTypeDefinition("spoken", "💬", "说一说"),
        ActivityTypeDefinition("guided", "🤝", "跟着做"),
        ActivityTypeDefinition("tap", "⭐", "点一点")
    )

    /** 模块 → 活动类型（CURRICULUM_MODULE_TYPES，22 条）。 */
    val moduleToActivityType: Map<String, String> = mapOf(
        "P01" to "color", "P02" to "shape", "P03" to "choice", "P04" to "audio",
        "M01" to "matching", "M02" to "memory", "M03" to "sequence", "M04" to "memory",
        "E01" to "choice", "E02" to "sorting", "E03" to "choice", "E04" to "sequence",
        "L01" to "spoken", "L02" to "spoken", "L03" to "audio", "L04" to "choice",
        "S01" to "choice", "S02" to "guided", "S03" to "choice",
        "D01" to "sequence", "D02" to "tap", "D03" to "guided"
    )

    private val titles = listOf(
        "出发啦", "眼睛小侦探", "记忆宝盒", "听听看", "第一座彩虹桥",
        "配对高手", "顺序小火车", "生活小帮手", "表情朋友", "第二座彩虹桥",
        "分类探险", "指令挑战", "说说看", "轮流合作", "第三座彩虹桥",
        "计划小达人", "工作记忆站", "生活闯关", "综合大冒险", "彩虹岛庆典"
    )

    private val icons = listOf(
        "🌱", "👀", "🎁", "🎧", "🌈", "🧩", "🚂", "🏠", "😊", "🌈",
        "🧺", "👂", "💬", "🤝", "🌈", "🗺️", "🧠", "🏆", "🚀", "🏝️"
    )

    /** 20 关静态元数据；难度 = min(5, floor((order-1)/5)+1)，即 LV01-05=1 … LV16-20=4。 */
    val levels: List<CurriculumLevelDefinition> = titles.indices.map { index ->
        val order = index + 1
        CurriculumLevelDefinition(
            levelId = "LV" + order.toString().padStart(2, '0'),
            order = order,
            title = titles[index],
            icon = icons[index],
            difficulty = minOf(5, index / 5 + 1)
        )
    }

    fun find(id: String): CurriculumLevelDefinition? = levels.firstOrNull { it.levelId == id }

    /**
     * 复刻 Web 端 buildLocalCurriculum：按儿童能力画像生成每关 5 个活动。
     *
     * @param domainScoreOrder 6 个能力域 id，按该儿童得分升序（最弱在前）；无档案时默认 A,B,C,D,E,F。
     * @param excludedTypes 用户排除的活动类型 id 集合（对应 Web 的 curriculumExclusions）。
     */
    fun buildCurriculumLevels(
        domainScoreOrder: List<String> = listOf("A", "B", "C", "D", "E", "F"),
        excludedTypes: Set<String> = emptySet()
    ): List<GeneratedCurriculumLevel> {
        val allModules = TaskCatalog.all
        fun typeOf(module: TaskDefinition): String = moduleToActivityType[module.id] ?: "choice"
        fun allowed(module: TaskDefinition): Boolean = typeOf(module) !in excludedTypes

        return levels.map { level ->
            val index = level.order - 1
            val focus = listOf(
                domainScoreOrder[index % 6],
                domainScoreOrder[(index + 1) % 6],
                domainScoreOrder[(index + 3) % 6]
            )

            // 1) 聚焦域内的模块，剔除用户排除的活动类型。
            var pool = allModules.filter { it.domain in focus && allowed(it) }.toMutableList()
            // 2) 不足 5 个时，补入其他域的允许模块。
            if (pool.size < 5) {
                allModules.filter { it !in pool && allowed(it) }.forEach { pool.add(it) }
            }
            // 3) 仍不足 3 个时，退化为 choice/tap 模块（与 Web 一致，此处不再按 excluded 过滤）。
            if (pool.size < 3) {
                pool = allModules.filter { typeOf(it) == "choice" || typeOf(it) == "tap" }.toMutableList()
            }

            val activities = mutableListOf<CurriculumActivityDefinition>()
            val usedTypes = mutableSetOf<String>()
            if (pool.isNotEmpty()) {
                val start = index * 3
                var turn = 0
                while (activities.size < 5 && turn < pool.size * 4) {
                    val module = pool[(start + turn) % pool.size]
                    turn++
                    val type = typeOf(module)
                    if (activities.any { it.moduleId == module.id }) continue
                    // 优先类型多样性：类型未用过，或已凑满 3 个后允许重复。
                    if (type !in usedTypes || activities.size >= 3) {
                        activities.add(
                            CurriculumActivityDefinition(
                                activityId = level.levelId + "-A" + (activities.size + 1),
                                moduleId = module.id,
                                domain = module.domain,
                                type = type,
                                label = module.name,
                                questionVariant = (index + activities.size) % 3
                            )
                        )
                        usedTypes.add(type)
                    }
                }
            }
            // 4) 兜底补齐到 5 个不重复模块（Web 的 pool.forEach 回填）。
            pool.forEach { module ->
                if (activities.size < 5 && activities.none { it.moduleId == module.id }) {
                    activities.add(
                        CurriculumActivityDefinition(
                            activityId = level.levelId + "-A" + (activities.size + 1),
                            moduleId = module.id,
                            domain = module.domain,
                            type = typeOf(module),
                            label = module.name,
                            questionVariant = (index + activities.size) % 3
                        )
                    )
                }
            }

            GeneratedCurriculumLevel(
                levelId = level.levelId,
                order = level.order,
                title = level.title,
                icon = level.icon,
                theme = (DomainCatalog.find(focus[0])?.name ?: focus[0]) + "岛",
                difficulty = level.difficulty,
                focusDomains = focus,
                activities = activities.take(5),
                minCompleted = level.minCompleted,
                minAccuracy = level.minAccuracy
            )
        }
    }

    /** 按活动取题：题目正文复用底层题库，variant 决定抽该模块 3 题中的哪一题。 */
    fun resolveQuestion(activity: CurriculumActivityDefinition): QuestionDefinition? {
        val moduleQuestions = QuestionCatalog.moduleQuestionBank.filter { it.moduleId == activity.moduleId }
        if (moduleQuestions.isEmpty()) return null
        return moduleQuestions[activity.questionVariant % moduleQuestions.size]
    }

    /**
     * 关卡通过判定，对应 Web 端 getCurriculumLevelState：
     * 最近一次闯关中 完成数 ≥ minCompleted 且 正确数/总题数 ≥ minAccuracy。
     */
    fun isPassed(level: GeneratedCurriculumLevel, runCompleted: Int, runCorrect: Int, runTotal: Int): Boolean {
        val accuracy = if (runTotal == 0) 0.0 else runCorrect.toDouble() / runTotal
        return runCompleted >= level.minCompleted && accuracy >= level.minAccuracy
    }
}
