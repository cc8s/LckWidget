package io.github.cc8s.lckwidget.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import io.github.cc8s.lckwidget.work.ScheduleSyncWorker

class LckWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LckWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        ScheduleSyncWorker.schedule(context)
    }
}