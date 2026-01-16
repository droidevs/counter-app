package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetKeepScreenOnUseCase @Inject constructor(
    private val pref: KeepScreenOnPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Boolean> = pref.get().flowOn(dispatchers.io)
}
