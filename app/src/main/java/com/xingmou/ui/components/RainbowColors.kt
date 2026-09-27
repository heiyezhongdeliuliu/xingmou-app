package com.xingmou.ui.components

import androidx.compose.ui.graphics.Color

/** DomainCatalog 的 displayColor 键 → Compose 颜色（儿童端与家长端画像共用）。 */
fun domainBarColor(colorKey: String): Color = when (colorKey) {
    "coral" -> Color(0xFFFF6F61)
    "sky" -> Color(0xFF4FC3F7)
    "amber" -> Color(0xFFFFB74D)
    "violet" -> Color(0xFFBA68C8)
    "mint" -> Color(0xFF4DB6AC)
    "blue" -> Color(0xFF64B5F6)
    else -> Color(0xFF90A4AE)
}
