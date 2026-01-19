package io.droidevs.counterapp.domain.history

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Controls how history entries are merged.
 *
 * If the same counter is updated multiple times within [mergeWindow],
 * we update the latest history row instead of inserting a new one.
 */
data class HistoryMergeConfig(
    val mergeWindow: Duration = 5.seconds
)

