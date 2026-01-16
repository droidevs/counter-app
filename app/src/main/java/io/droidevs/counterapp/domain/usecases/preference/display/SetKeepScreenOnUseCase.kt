package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetKeepScreenOnUseCase @Inject constructor(
    private val pref: KeepScreenOnPreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Boolean) = withContext(dispatchers.io) {
        pref.set(value)
    }
}
