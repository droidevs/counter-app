package io.droidevs.counterapp.domain.services

import android.net.Uri


sealed class ExportResult {
    data class Success(val fileUri: Uri, val fileName: String) : ExportResult()
    data class Error(val message: String, val throwable: Throwable? = null) : ExportResult()
    data object Cancelled : ExportResult()
}