package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val pref: ThemePreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Theme> = pref.get().flowOn(dispatchers.io)
}
