package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.SettingsRepository
import io.droidevs.counterapp.ui.vm.SettingsViewModel

class SettingsViewModelFactory(
    private val repo: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
            return SettingsViewModel(repo) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
