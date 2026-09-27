package com.xingmou.core.domain

import com.xingmou.data.catalog.QuestionDefinition
import com.xingmou.data.catalog.QuestionType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionEvaluatorTest {
    @Test
    fun correctOptionIsEvaluatedFromQuestionDefinition() {
        val question = question(QuestionType.CHOICE, correctOption = 1)

        val result = QuestionEvaluator.evaluate(question, selectedOption = 1)

        assertTrue(result.isValidSelection)
        assertTrue(result.correct == true)
        assertTrue(result.completed)
    }

    @Test
    fun wrongOptionIsEvaluatedAsIncorrect() {
        val question = question(QuestionType.MEMORY, correctOption = 1)

        val result = QuestionEvaluator.evaluate(question, selectedOption = 0)

        assertTrue(result.isValidSelection)
        assertFalse(result.correct == true)
        assertTrue(result.correct == false)
        assertTrue(result.completed)
    }

    @Test
    fun sequenceUsesTheDefinedCorrectOption() {
        val question = question(QuestionType.SEQUENCE, correctOption = 0)

        val result = QuestionEvaluator.evaluate(question, selectedOption = 0)

        assertTrue(result.isValidSelection)
        assertTrue(result.correct == true)
        assertTrue(result.completed)
    }

    @Test
    fun observedQuestionRecordsCompletionWithoutAnswerScoring() {
        val question = question(QuestionType.OBSERVED, correctOption = null)

        val result = QuestionEvaluator.evaluate(question, selectedOption = 0)

        assertTrue(result.isValidSelection)
        assertNull(result.correct)
        assertTrue(result.completed)
    }

    @Test
    fun observedIncompleteOptionDoesNotCountAsCompleted() {
        val question = QuestionDefinition(
            id = "OBSERVED-TEST",
            version = 1,
            moduleId = "TEST",
            domain = "A",
            type = QuestionType.OBSERVED,
            prompt = "观察完成情况",
            options = listOf("👏 跟着做", "⏳ 还没完成"),
            correctOption = null
        )

        val result = QuestionEvaluator.evaluate(question, selectedOption = 1)

        assertTrue(result.isValidSelection)
        assertNull(result.correct)
        assertFalse(result.completed)
    }

    @Test
    fun invalidSelectionIsRejected() {
        val question = question(QuestionType.SEQUENCE, correctOption = 0)

        val result = QuestionEvaluator.evaluate(question, selectedOption = 9)

        assertFalse(result.isValidSelection)
        assertNull(result.correct)
        assertFalse(result.completed)
    }

    private fun question(type: QuestionType, correctOption: Int?): QuestionDefinition =
        QuestionDefinition(
            id = "TEST-QUESTION",
            version = 1,
            moduleId = "TEST",
            domain = "A",
            type = type,
            prompt = "测试题",
            options = listOf("⭐", "🌙"),
            correctOption = correctOption
        )
}
