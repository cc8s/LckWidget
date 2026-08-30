package io.github.cc8s.lckwidget.model

enum class MatchGroup {
    LEGEND,
    RISE;

    companion object {
        fun fromSectionName(name: String): MatchGroup? = when {
            name.contains("레전드") -> LEGEND
            name.contains("라이즈") -> RISE
            else -> null          // 플레이-인, 지역별 챔피언십 등
        }
    }
}