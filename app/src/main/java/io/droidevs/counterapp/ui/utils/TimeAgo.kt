package io.droidevs.counterapp.ui.utils

import java.time.Instant


fun getRelativeTime(instant: Instant): String {
    return getRelativeTime(instant.toEpochMilli())
}

fun getRelativeTime(timestamp: Long): String {
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
