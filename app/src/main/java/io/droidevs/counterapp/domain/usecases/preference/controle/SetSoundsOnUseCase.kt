package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetSoundsOnUseCase @Inject constructor(
    private val pref: SoundsOnPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
