package io.droidevs.counterapp.ui.vm.actions

import android.net.Uri

sealed interface ImportAction {
    data object RequestImport : ImportAction
    data class Import(val fileUri: Uri) : ImportAction
}
