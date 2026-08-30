package io.github.cc8s.lckwidget.data.remote

import kotlinx.serialization.json.Json
// 테스트 이후 APICClient 로 넘기기
val ApiJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    coerceInputValues = true
}