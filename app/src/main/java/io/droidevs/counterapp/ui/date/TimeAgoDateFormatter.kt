package io.droidevs.counterapp.ui.date

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

class TimeAgoDateFormatter @Inject constructor(
    private val context: Context
) : DateFormatter {

    override fun format(instant: Instant): String {
        return getRelativeTime(instant)
    }

    private fun getRelativeTime(instant: Instant): String {
        return getRelativeTime(instant.toEpochMilli())
    }

    private fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "Just now"
            seconds < 120 -> "1 minute ago"
            minutes < 60 -> "$minutes minutes ago"
            minutes < 120 -> "1 hour ago"
            hours < 24 -> "$hours hours ago"
            hours < 48 -> "Yesterday"
            days < 7 -> "$days days ago"
            days < 14 -> "Last week"
            weeks < 4 -> "$weeks weeks ago"
            months < 2 -> "Last month"
            months < 12 -> "$months months ago"
            years < 2 -> "Last year"
            else -> "$years years ago"
        }
    }
}
