package io.droidevs.counterapp.domain.services

import android.net.Uri
import io.droidevs.counterapp.domain.model.Backup

interface FileImportService {
    suspend fun import(fileUri: Uri): ImportResult<Backup>
}
