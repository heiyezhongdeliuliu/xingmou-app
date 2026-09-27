package com.xingmou.data.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogTest {
    @Test fun domainCatalogContainsSixDomains() {
        assertEquals(listOf("A", "B", "C", "D", "E", "F"), DomainCatalog.all.map { it.id })
    }

    @Test fun taskCatalogContainsTwentyTwoUniqueModules() {
        assertEquals(22, TaskCatalog.all.size)
        assertEquals(22, TaskCatalog.all.map { it.id }.toSet().size)
        assertTrue(TaskCatalog.all.all { DomainCatalog.find(it.domain) != null })
    }

    @Test fun professionalCatalogsAreAvailableForSeedMigration() {
        assertEquals(15, AssessmentCatalog.all.size)
        assertEquals(12, RehabilitationMethods.all.size)
        assertTrue(RehabilitationMethods.all.any { it.id == "FAMILY" })
    }

    @Test fun webQuestionBankContainsNinetyQuestionsAcrossTwentyTwoModules() {
        assertEquals(24, QuestionCatalog.baselineQuestions.size)
        assertEquals(66, QuestionCatalog.moduleQuestionBank.size)
        assertEquals(66, QuestionCatalog.fullCourseQuestions.size)
        assertEquals(22, QuestionCatalog.moduleQuestionBank.map { it.moduleId }.toSet().size)
        assertEquals(90, QuestionCatalog.baselineQuestions.size + QuestionCatalog.moduleQuestionBank.size)
        assertTrue(QuestionCatalog.moduleQuestionBank.all { it.version == 1 && it.sourceRef == "WEB_BANK_V1" })
        assertTrue(QuestionCatalog.baselineQuestions.all { it.version == 1 && it.sourceRef == "WEB_BANK_V1" })
    }

    @Test fun webQuestionBankKeepsFourBaselineAndThreeModuleQuestionsPerUnit() {
        assertEquals(mapOf("A" to 4, "B" to 4, "C" to 4, "D" to 4, "E" to 4, "F" to 4), QuestionCatalog.baselineQuestions.groupingBy { it.domain }.eachCount())
        assertTrue(QuestionCatalog.moduleQuestionBank.groupingBy { it.moduleId }.eachCount().values.all { it == 3 })
    }

    @Test fun firstBaselineQuestionIncludesItsWebTargetCue() {
        assertEquals("🐶", QuestionCatalog.baselineQuestions.first { it.id == "BL-A-01" }.stimulus)
    }

    @Test fun courseQuestionsHaveCoherentStimulusAndTwoToFourOptions() {
        assertTrue(QuestionCatalog.fullCourseQuestions.all { it.options.size in 2..4 })
        assertTrue(QuestionCatalog.fullCourseQuestions.all { question ->
            question.correctOption == null || question.correctOption in question.options.indices
        })
        assertTrue(QuestionCatalog.fullCourseQuestions.filter { it.type == QuestionType.OBSERVED }.all { it.correctOption == null })
    }

    @Test fun memoryModeQuestionsHaveThreeSecondPreview() {
        assertTrue(QuestionCatalog.fullCourseQuestions.filter { it.type == QuestionType.MEMORY }.all { it.previewMs == 3_000L })
        assertEquals(3_000L, QuestionCatalog.fullCourseQuestions.filter { it.id.startsWith("M03-") }.first().previewMs)
        assertTrue(QuestionCatalog.fullCourseQuestions.all { it.previewMs == 0L || it.previewMs == 3_000L })
    }
}
