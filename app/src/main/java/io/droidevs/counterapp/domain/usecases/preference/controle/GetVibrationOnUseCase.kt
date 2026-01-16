package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetVibrationOnUseCase @Inject constructor(
    private val pref: VibrationOnPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Boolean> = pref.get().flowOn(dispatchers.io)
}
