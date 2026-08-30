package io.github.cc8s.lckwidget.model

enum class MatchState {
    UNSTARTED, IN_PROGRESS, COMPLETED, UNKNOWN;

    companion object {
        fun from(raw: String) = when (raw) {
            "unstarted"  -> UNSTARTED
            "inProgress" -> IN_PROGRESS
            "completed"  -> COMPLETED
            else         -> UNKNOWN      // null 방어
        }
    }
}