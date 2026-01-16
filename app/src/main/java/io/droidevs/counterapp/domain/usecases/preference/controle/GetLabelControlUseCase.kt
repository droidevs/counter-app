package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLabelControlUseCase @Inject constructor(
    private val pref: LabelControlPreference,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<Boolean, PreferenceError>> = pref.get().flowOn(dispatchers.io)
}
