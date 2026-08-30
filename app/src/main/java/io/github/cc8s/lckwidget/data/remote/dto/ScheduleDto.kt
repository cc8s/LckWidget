package io.github.cc8s.lckwidget.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(val data: ScheduleData)

@Serializable
data class ScheduleData(val schedule: ScheduleDto)

@Serializable
data class ScheduleDto(
    val pages: PagesDto,
    val events: List<EventDto>
)

@Serializable
data class PagesDto(
    val older: String? = null,
    val newer: String? = null
)

@Serializable
data class EventDto(
    val startTime: String,
    val state: String,
    val type: String,
    val blockName: String? = null,
    val league: LeagueDto,
    val match: MatchDto? = null
)

@Serializable
data class LeagueDto(
    val name: String,
    val slug: String
)

@Serializable
data class MatchDto(
    val id: String,
    val flags: List<String> = emptyList(),
    val teams: List<TeamDto>,
    val strategy: StrategyDto
)

@Serializable
data class TeamDto(
    val name: String,
    val code: String,
    val image: String,
    val result: ResultDto? = null,
    val record: RecordDto? = null
)

@Serializable
data class ResultDto(
    val outcome: String? = null,
    val gameWins: Int = 0
)

@Serializable
data class RecordDto(
    val wins: Int,
    val losses: Int
)

@Serializable
data class StrategyDto(
    val type: String,
    val count: Int
)