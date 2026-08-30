package io.github.cc8s.lckwidget.widget

import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.model.MatchGroup
import io.github.cc8s.lckwidget.model.MatchState
import java.time.Instant

// 실제 API 응답에서 뽑은 값. UI 확인용이며 v1 완성 시 삭제.
val sampleMatches = listOf(
    LckMatch(
        id = "115548147900619045",
        startsAt = Instant.parse("2026-08-19T08:00:00Z"),
        state = MatchState.UNSTARTED,
        blockName = "13주 차",
        leftCode = "GEN", rightCode = "KT",
        leftLogoUrl = "", rightLogoUrl = "",
        bestOf = 3, isTbd = false,
        group = MatchGroup.LEGEND
    ),
    LckMatch(
        id = "115548147900684685",
        startsAt = Instant.parse("2026-08-19T10:00:00Z"),
        state = MatchState.UNSTARTED,
        blockName = "13주 차",
        leftCode = "BRO", rightCode = "DNS",
        leftLogoUrl = "", rightLogoUrl = "",
        bestOf = 3, isTbd = false,
        group = MatchGroup.RISE
    ),
    LckMatch(
        id = "117030752644841571",
        startsAt = Instant.parse("2026-08-26T08:00:00Z"),
        state = MatchState.UNSTARTED,
        blockName = "플레이-인",
        leftCode = "TBD", rightCode = "TBD",
        leftLogoUrl = "", rightLogoUrl = "",
        bestOf = 5, isTbd = true,
    )
)