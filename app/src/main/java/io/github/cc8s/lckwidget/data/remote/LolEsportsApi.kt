package io.github.cc8s.lckwidget.data.remote

import io.github.cc8s.lckwidget.data.remote.dto.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

object LckConstants {
    const val LCK_LEAGUE_ID = "98767991310872058"

    const val API_KEY = "0TvQnueqKa5mxJntVWt0w4LpLfEkrV1Ta8rQBb9Z"

    const val BASE_URL = "https://esports-api.lolesports.com/persisted/gw/"
}

interface LolEsportsApi {

    @GET("getSchedule")
    suspend fun getSchedule(
        @Query("leagueId") leagueId: String = LckConstants.LCK_LEAGUE_ID,
        @Query("hl") hl: String = "ko-KR"
    ): ScheduleResponse
}