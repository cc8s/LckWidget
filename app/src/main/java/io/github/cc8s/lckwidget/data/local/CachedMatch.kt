package io.github.cc8s.lckwidget.data.local

import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.model.MatchGroup
import io.github.cc8s.lckwidget.model.MatchState
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CachedMatch(
    val id: String,
    val startsAtEpoch: Long,
    val state: String,
    val blockName: String,
    val leftCode: String,
    val rightCode: String,
    val leftLogoUrl: String,
    val rightLogoUrl: String,
    val bestOf: Int,
    val isTbd: Boolean,
    val group: String? = null
)

fun LckMatch.toCached() = CachedMatch(
    id = id,
    startsAtEpoch = startsAt.toEpochMilli(),
    state = state.name,
    blockName = blockName,
    leftCode = leftCode,
    rightCode = rightCode,
    leftLogoUrl = leftLogoUrl,
    rightLogoUrl = rightLogoUrl,
    bestOf = bestOf,
    isTbd = isTbd,
    group = group?.name
)

fun CachedMatch.toDomain() = LckMatch(
    id = id,
    startsAt = Instant.ofEpochMilli(startsAtEpoch),
    state = runCatching { MatchState.valueOf(state) }.getOrDefault(MatchState.UNKNOWN),
    blockName = blockName,
    leftCode = leftCode,
    rightCode = rightCode,
    leftLogoUrl = leftLogoUrl,
    rightLogoUrl = rightLogoUrl,
    bestOf = bestOf,
    isTbd = isTbd,
    group = group?.let { name -> runCatching { MatchGroup.valueOf(name) }.getOrNull() }
)