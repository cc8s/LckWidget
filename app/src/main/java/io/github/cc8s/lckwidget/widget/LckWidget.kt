package io.github.cc8s.lckwidget.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import io.github.cc8s.lckwidget.R
import io.github.cc8s.lckwidget.data.ScheduleRepository
import io.github.cc8s.lckwidget.model.LckMatch
import io.github.cc8s.lckwidget.widget.component.EmptyState
import io.github.cc8s.lckwidget.widget.component.MatchRow

class LckWidget : GlanceAppWidget() {

    companion object {
        private val SMALL = DpSize(180.dp, 70.dp)
        private val MEDIUM = DpSize(180.dp, 180.dp)
        private val LARGE = DpSize(180.dp, 290.dp)
    }

    override val sizeMode = SizeMode.Responsive(setOf(SMALL, MEDIUM, LARGE))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = ScheduleRepository(context)
        val matches = runCatching { repo.upcoming(3) }.getOrDefault(emptyList())

        provideContent {
            GlanceTheme { WidgetContent(matches) }
        }
    }

    @Composable
    private fun WidgetContent(matches: List<LckMatch>) {
        val height = LocalSize.current.height
        val count = when {
            height >= 280.dp -> 3
            height >= 150.dp -> 2
            else -> 1
        }

        Box(
            modifier = GlanceModifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(ImageProvider(R.drawable.widget_border_gradient))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_widget_mark),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(GlanceTheme.colors.primary),
                        modifier = GlanceModifier.size(13.dp)
                    )
                    Spacer(GlanceModifier.width(5.dp))
                    Text(
                        text = "LCK",
                        maxLines = 1,
                        style = TextStyle(
                            color = GlanceTheme.colors.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(GlanceModifier.height(2.dp))

                if (matches.isEmpty()) {
                    EmptyState()
                } else {
                    for (match in matches.take(count)) {
                        MatchRow(match)
                    }
                }
            }
        }
    }
}