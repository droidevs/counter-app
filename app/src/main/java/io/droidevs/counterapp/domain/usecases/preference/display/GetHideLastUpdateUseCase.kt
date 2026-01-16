package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHideLastUpdateUseCase @Inject constructor(
    private val pref: HideLastUpdatePreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Boolean> = pref.get().flowOn(dispatchers.io)
}
