package io.github.cc8s.lckwidget

import io.github.cc8s.lckwidget.data.remote.ApiJson
import io.github.cc8s.lckwidget.data.remote.dto.ScheduleResponse
import io.github.cc8s.lckwidget.data.toDomainOrNull
import io.github.cc8s.lckwidget.model.MatchState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val FIXTURE = "schedule_lck_20260817.json"
private val KST: ZoneId = ZoneId.of("Asia/Seoul")

class ScheduleParsingTest {

    private fun loadFixture(name: String): String =
        checkNotNull(javaClass.getResourceAsStream("/$name")) {
            "fixture not found: $name"
        }.bufferedReader().use { it.readText() }

    private fun parse() =
        ApiJson.decodeFromString<ScheduleResponse>(loadFixture(FIXTURE))

    @Test
    fun `전체 응답이 예외 없이 파싱된다`() {
        val events = parse().data.schedule.events
        assertTrue(events.isNotEmpty())
        println("총 이벤트: ${events.size}")
    }

    @Test
    fun `newer 커서가 없으므로 페이지네이션이 불필요하다`() {
        assertNull(parse().data.schedule.pages.newer)
    }

    @Test
    fun `모든 이벤트가 도메인 모델로 매핑된다`() {
        val events = parse().data.schedule.events
        val matches = events.mapNotNull { it.toDomainOrNull() }
        assertEquals(events.size, matches.size)

        val byState = matches.groupingBy { it.state }.eachCount()
        println("상태별: $byState")
        assertEquals(0, byState[MatchState.UNKNOWN] ?: 0)
    }

    @Test
    fun `첫 미래 경기는 8월 19일 17시 KST GEN vs KT다`() {
        val next = parse().data.schedule.events
            .mapNotNull { it.toDomainOrNull() }
            .filter { it.state == MatchState.UNSTARTED }
            .minByOrNull { it.startsAt }
            ?: error("미래 경기 없음")

        val kst = next.startsAt.atZone(KST)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        println("다음 경기: $kst ${next.leftCode} vs ${next.rightCode} (Bo${next.bestOf})")
        assertEquals("2026-08-19 17:00", kst)
        assertEquals("GEN", next.leftCode)
        assertEquals("KT", next.rightCode)
    }

    @Test
    fun `TBD 경기는 result와 record가 null이어도 매핑된다`() {
        val tbd = parse().data.schedule.events
            .mapNotNull { it.toDomainOrNull() }
            .filter { it.isTbd }

        println("TBD 경기: ${tbd.size}개, 첫 blockName=${tbd.firstOrNull()?.blockName}")
        assertTrue(tbd.isNotEmpty())
        assertTrue(tbd.all { it.bestOf == 5 })
    }

    @Test
    fun `로고 URL이 전부 https로 변환된다`() {
        val matches = parse().data.schedule.events.mapNotNull { it.toDomainOrNull() }
        assertTrue(matches.all {
            it.leftLogoUrl.startsWith("https://") && it.rightLogoUrl.startsWith("https://")
        })
    }
}