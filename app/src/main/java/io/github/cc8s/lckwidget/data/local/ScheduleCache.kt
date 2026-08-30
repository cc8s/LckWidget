package io.github.cc8s.lckwidget.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.cc8s.lckwidget.data.remote.ApiJson
import kotlinx.coroutines.flow.first

private val Context.scheduleDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "lck_schedule"
)

class ScheduleCache(private val context: Context) {

    private val keyMatches = stringPreferencesKey("matches_json")
    private val keyUpdatedAt = longPreferencesKey("updated_at_epoch")

    suspend fun save(matches: List<CachedMatch>) {
        val json = ApiJson.encodeToString(matches)
        context.scheduleDataStore.edit { prefs ->
            prefs[keyMatches] = json
            prefs[keyUpdatedAt] = System.currentTimeMillis()
        }
    }

    suspend fun load(): List<CachedMatch> {
        val prefs = context.scheduleDataStore.data.first()
        val json = prefs[keyMatches] ?: return emptyList()
        return runCatching {
            ApiJson.decodeFromString<List<CachedMatch>>(json)
        }.getOrElse { emptyList() }
    }

    suspend fun updatedAt(): Long? =
        context.scheduleDataStore.data.first()[keyUpdatedAt]
}
