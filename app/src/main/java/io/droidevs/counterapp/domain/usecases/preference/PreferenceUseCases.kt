package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.NotificationPreferenceUseCases

// Grouped holder for all preference-related use cases
data class PreferenceUseCases(
    val counterUseCases: CounterPreferenceUseCases,
    val hardwareUseCases: HardwarePreferenceUseCases,
    val displayUseCases: DisplayPreferenceUseCases,
    val notificationUseCases: NotificationPreferenceUseCases,
    val backupUseCases: BackupPreferenceUseCases
)
