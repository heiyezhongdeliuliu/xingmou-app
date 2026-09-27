package com.xingmou.core.domain

import com.xingmou.data.catalog.QuestionDefinition
import com.xingmou.data.catalog.QuestionType

/**
 * 统一处理儿童端题目选择结果。
 *
 * UI 只负责传递用户选择的选项索引，正确性必须由题目定义决定。
 * 观察题不以“正确答案”评分，而以是否完成一次有效选择作为完成结果。
 */
data class QuestionEvaluation(
    val isValidSelection: Boolean,
    val correct: Boolean?,
    val completed: Boolean,
    val selectedOption: Int?,
    val reason: String? = null
)

object QuestionEvaluator {
    fun evaluate(question: QuestionDefinition, selectedOption: Int): QuestionEvaluation {
        if (selectedOption !in question.options.indices) {
            return QuestionEvaluation(
                isValidSelection = false,
                correct = null,
                completed = false,
                selectedOption = selectedOption,
                reason = "选项索引超出题目范围"
            )
        }

        return when (question.type) {
            QuestionType.OBSERVED -> {
                val completed = !question.options[selectedOption].contains("还没完成")
                QuestionEvaluation(
                    isValidSelection = true,
                    correct = null,
                    completed = completed,
                    selectedOption = selectedOption,
                    reason = if (completed) null else "观察任务尚未完成"
                )
            }
            QuestionType.CHOICE,
            QuestionType.MEMORY,
            QuestionType.SEQUENCE,
            QuestionType.SORTING,
            QuestionType.AUDIO -> QuestionEvaluation(
                isValidSelection = true,
                correct = question.correctOption?.let { it == selectedOption } ?: false,
                completed = true,
                selectedOption = selectedOption
            )
        }
    }
}
