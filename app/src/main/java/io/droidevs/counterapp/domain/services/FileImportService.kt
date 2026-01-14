package io.droidevs.counterapp.domain.services

import android.net.Uri

interface FileImportService {
    suspend fun import(fileUri: Uri): ImportResult
}
