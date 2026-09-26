package com.xingmou

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IdentityLoginInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreenSelectsSingleFixedPortAndCanLogout() {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("选择登录身份").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("儿童").performClick()
        composeRule.onNodeWithText("本机账号标识").performTextInput("child")
        composeRule.onNodeWithText("进入儿童端").performClick()
        composeRule.onNodeWithText("和小星一起练习").assertIsDisplayed()
        composeRule.onNodeWithText("退出当前端").performClick()
        composeRule.onNodeWithText("选择登录身份").assertIsDisplayed()
    }
}
