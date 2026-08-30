package io.github.cc8s.lckwidget.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import io.github.cc8s.lckwidget.R
import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.model.MatchGroup
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val KST: ZoneId = ZoneId.of("Asia/Seoul")
private val DATE_FMT = DateTimeFormatter.ofPattern("M/d(E)", Locale.KOREAN)
private val TIME_FMT = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN)

@Composable
fun MatchRow(match: LckMatch) {
    val kst = match.startsAt.atZone(KST)

    val teamStyle = TextStyle(
        color = GlanceTheme.colors.onSurface,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )
    val subStyle = TextStyle(
        color = GlanceTheme.colors.onSurfaceVariant,
        fontSize = 11.sp
    )

    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = GlanceModifier.width(62.dp)) {
            Text(text = kst.format(DATE_FMT), style = subStyle)
            Text(
                text = kst.format(TIME_FMT),
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (match.isTbd) {
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = match.blockName, maxLines = 1, style = teamStyle)
            }
        } else {
            val groupIcon = when (match.group) {
                MatchGroup.LEGEND -> R.drawable.ic_group_legend
                MatchGroup.RISE -> R.drawable.ic_group_rise
                null -> null
            }

            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = match.leftCode, maxLines = 1, style = teamStyle)
            }

            Box(
                modifier = GlanceModifier.width(26.dp),
                contentAlignment = Alignment.Center
            ) {
                if (groupIcon != null) {
                    Image(
                        provider = ImageProvider(groupIcon),
                        contentDescription = null,
                        modifier = GlanceModifier.size(13.dp)
                    )
                } else {
                    Text(
                        text = "vs",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = match.rightCode, maxLines = 1, style = teamStyle)
            }
        }

        Text(
            text = "Bo${match.bestOf}",
            modifier = GlanceModifier.width(40.dp),
            style = subStyle.copy(textAlign = TextAlign.End)
        )
    }
}