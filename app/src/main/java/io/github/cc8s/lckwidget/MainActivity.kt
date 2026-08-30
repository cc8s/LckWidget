package io.github.cc8s.lckwidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import io.github.cc8s.lckwidget.data.ScheduleRepository
import io.github.cc8s.lckwidget.ui.theme.LCKWidgetTheme
import io.github.cc8s.lckwidget.widget.LckWidget
import io.github.cc8s.lckwidget.work.ScheduleSyncWorker
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val KST: ZoneId = ZoneId.of("Asia/Seoul")
private val SYNC_FMT = DateTimeFormatter.ofPattern("M월 d일 HH:mm", Locale.KOREAN)

private val HOW_TO = listOf(
    "홈 화면 빈 공간을 길게 누릅니다.",
    "위젯 목록에서 LCK 위젯을 찾습니다.",
    "원하는 위치에 놓고 크기를 조절합니다. 높이에 따라 1~3경기가 표시됩니다."
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LCKWidgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        ScheduleSyncWorker.schedule(this)
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { ScheduleRepository(context) }

    var lastSyncedAt by remember { mutableStateOf<Long?>(null) }
    var syncing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { lastSyncedAt = repo.lastUpdatedAt() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(text = "LCK 위젯", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "다음 경기 일정을 홈 화면에서 바로 확인하세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(28.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "위젯 추가하기", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                HOW_TO.forEachIndexed { index, step ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = step, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lastSyncedAt.toSyncLabel(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                enabled = !syncing,
                onClick = {
                    syncing = true
                    error = null
                    scope.launch {
                        runCatching {
                            repo.refresh()
                            LckWidget().updateAll(context)
                            repo.lastUpdatedAt()
                        }.onSuccess {
                            lastSyncedAt = it
                        }.onFailure {
                            error = "동기화에 실패했습니다. 네트워크를 확인해 주세요."
                        }
                        syncing = false
                    }
                }
            ) {
                Text(text = if (syncing) "동기화 중" else "지금 동기화")
            }
        }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "일정은 6시간마다 자동으로 동기화되며, 오프라인일 때는 " +
                "마지막으로 받아온 일정을 보여줍니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "이 앱은 Riot Games가 승인한 프로젝트가 아닙니다. LCK 및 " +
                "League of Legends 관련 상표의 권리는 Riot Games, Inc.에 있습니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun Long?.toSyncLabel(): String = when (this) {
    null -> "아직 동기화하지 않았습니다"
    else -> "마지막 동기화 " + Instant.ofEpochMilli(this).atZone(KST).format(SYNC_FMT)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LCKWidgetTheme { HomeScreen() }
}
