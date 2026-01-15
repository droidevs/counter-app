package io.droidevs.counterapp.ui.date

import java.time.Instant

interface DateFormatter {
    fun format(timestamp: Instant): String
}
