package io.github.cc8s.lckwidget.data

import android.content.Context
import io.github.cc8s.lckwidget.data.local.CachedMatch
import io.github.cc8s.lckwidget.data.local.ScheduleCache
import io.github.cc8s.lckwidget.data.local.toCached
import io.github.cc8s.lckwidget.data.local.toDomain
import io.github.cc8s.lckwidget.data.remote.ApiClient
import io.github.cc8s.lckwidget.data.remote.LolEsportsApi
import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.model.MatchState
import java.time.Instant

class ScheduleRepository(
    private val api: LolEsportsApi = ApiClient.api,
    private val cache: ScheduleCache
) {

    constructor(context: Context) : this(
        api = ApiClient.api,
        cache = ScheduleCache(context.applicationContext)
    )

    suspend fun refresh() {
        val response = api.getSchedule()

        val toCache: List<CachedMatch> = response.data.schedule.events
            .mapNotNull { it.toDomainOrNull() }
            .filter { it.state != MatchState.COMPLETED }
            .sortedBy { it.startsAt }
            .map { it.toCached() }

        cache.save(toCache)
    }

    suspend fun upcoming(limit: Int): List<LckMatch> {
        val now = Instant.now()
        return cache.load()
            .map { it.toDomain() }
            .filter { it.startsAt.isAfter(now) }
            .sortedBy { it.startsAt }
            .take(limit)
    }

    suspend fun lastUpdatedAt(): Long? = cache.updatedAt()
}