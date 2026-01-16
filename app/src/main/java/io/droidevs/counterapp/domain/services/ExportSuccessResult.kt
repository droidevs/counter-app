package io.droidevs.counterapp.domain.services

import android.net.Uri

data class ExportSuccessResult(
    val fileUri: Uri,
    val fileName: String
)
