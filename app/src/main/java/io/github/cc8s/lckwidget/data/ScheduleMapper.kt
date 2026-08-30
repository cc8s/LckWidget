package io.github.cc8s.lckwidget.data

import io.github.cc8s.lckwidget.data.remote.dto.EventDto
import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.model.MatchState
import java.time.Instant

private const val TBD_CODE = "TBD"

fun EventDto.toDomainOrNull(): LckMatch? {
    if (type != "match") return null
    val m = match ?: return null
    if (m.teams.size != 2) return null

    val left = m.teams[0]
    val right = m.teams[1]

    val instant = runCatching { Instant.parse(startTime) }.getOrNull() ?: return null

    return LckMatch(
        id = m.id,
        startsAt = instant,
        state = MatchState.from(state),
        blockName = blockName.orEmpty(),
        leftCode = left.code,
        rightCode = right.code,
        leftLogoUrl = left.image.toHttps(),
        rightLogoUrl = right.image.toHttps(),
        bestOf = m.strategy.count,
        isTbd = left.code == TBD_CODE || right.code == TBD_CODE
    )
}

internal fun String.toHttps(): String =
    if (startsWith("http://")) "https://" + removePrefix("http://") else this