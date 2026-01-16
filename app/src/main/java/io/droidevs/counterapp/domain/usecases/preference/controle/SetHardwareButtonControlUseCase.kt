package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetHardwareButtonControlUseCase @Inject constructor(
    private val pref: HardwareButtonControlPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
