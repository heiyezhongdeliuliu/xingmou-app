package com.xingmou.data.catalog

data class TaskDefinition(
    val id: String,
    val name: String,
    val domain: String,
    val engine: String,
    val goal: String,
    val materialType: String = "图片"
)

/** Web 端 22 个训练模块目录，名称、领域和目标以 WEB_BANK_V1 为准。 */
object TaskCatalog {
    val all: List<TaskDefinition> = listOf(
        TaskDefinition("P01", "颜色识别", "A", "choice", "在干扰项中指认目标颜色", "图片"),
        TaskDefinition("P02", "形状辨认", "A", "choice", "辨认基础形状与大小", "图片"),
        TaskDefinition("P03", "视觉搜索/找不同", "A", "choice", "训练选择性注意与视觉扫描", "图片"),
        TaskDefinition("P04", "听觉注意", "A", "audio", "辨认目标声音并抗干扰", "语音"),
        TaskDefinition("M01", "物品配对", "B", "choice", "匹配相同或关联物品", "图片"),
        TaskDefinition("M02", "翻牌记忆", "B", "memory", "记住图片位置并配对", "记忆"),
        TaskDefinition("M03", "序列回忆", "B", "sequence", "按顺序回忆颜色、数字或图形", "记忆"),
        TaskDefinition("M04", "工作记忆", "B", "memory", "短时保持并操作信息", "记忆"),
        TaskDefinition("L01", "图片命名", "D", "observed", "看图说词并提取词汇", "观察"),
        TaskDefinition("L02", "句子表达", "D", "observed", "用完整句描述图片", "观察"),
        TaskDefinition("L03", "指令理解", "D", "audio", "理解一步或多步指令", "语音"),
        TaskDefinition("L04", "AAC/图片选择", "D", "choice", "用图片表达需要与选择", "图片"),
        TaskDefinition("E01", "因果关系", "C", "choice", "理解动作、事件与结果", "图片"),
        TaskDefinition("E02", "分类整理", "C", "sorting", "按类别、功能或属性分类", "图片"),
        TaskDefinition("E03", "数量认知", "C", "sorting", "点数、数字匹配与简单比较", "图片"),
        TaskDefinition("E04", "计划与顺序", "C", "sequence", "排列生活事件步骤", "图片"),
        TaskDefinition("S01", "情绪识别", "E", "choice", "识别基本表情与情境", "图片"),
        TaskDefinition("S02", "轮流/共同注意", "E", "observed", "练习等待、轮流和共同关注", "观察"),
        TaskDefinition("S03", "社交规则", "E", "choice", "理解问候、回应和社交边界", "图片"),
        TaskDefinition("D01", "生活步骤训练", "F", "sequence", "按顺序完成生活自理步骤", "图片"),
        TaskDefinition("D02", "精细动作/点选", "F", "choice", "训练点选与手眼协调", "图片"),
        TaskDefinition("D03", "模仿/节律动作", "F", "observed", "按节律模仿动作并记录完成度", "观察")
    )

    fun find(id: String): TaskDefinition? = all.firstOrNull { it.id == id }
}

