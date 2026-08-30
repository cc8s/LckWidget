package io.github.cc8s.lckwidget.model

import java.time.Instant

data class LckMatch(
    val id: String,
    val startsAt: Instant,
    val state: MatchState,
    val blockName: String,       // "13주 차", "플레이오프"
    val group: MatchGroup? = null, // 레전드 라이즈 그룹
    val leftCode: String,        // "GEN"
    val rightCode: String,       // "KT"
    val leftLogoUrl: String,
    val rightLogoUrl: String,
    val bestOf: Int,
    val isTbd: Boolean
)