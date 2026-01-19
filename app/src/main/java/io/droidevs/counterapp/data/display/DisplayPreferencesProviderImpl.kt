package io.droidevs.counterapp.data.display

import io.droidevs.counterapp.domain.display.DisplayPreferences
import io.droidevs.counterapp.domain.display.DisplayPreferencesProvider
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.resultFlow
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideCounterCategoryLabelUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisplayPreferencesProviderImpl @Inject constructor(
    private val getHideControls: GetHideControlsUseCase,
    private val getHideLastUpdate: GetHideLastUpdateUseCase,
    private val getHideCounterCategoryLabel: GetHideCounterCategoryLabelUseCase,
) : DisplayPreferencesProvider {

    override fun preferences(): Flow<Result<DisplayPreferences, PreferenceError>> = resultFlow {
        // We want a single stream that emits whenever any preference emits.
        // Since the app already models each preference as a Flow<Result<Boolean, PreferenceError>>,
        // we can combine them by using Flow.combine at the call site, but to keep this domain-ish,
        // we do it here via resultFlow + combine on the underlying flows.

        kotlinx.coroutines.flow.combine(
            getHideControls(),
            getHideLastUpdate(),
            getHideCounterCategoryLabel(),
        ) { hideControlsRes, hideLastUpdateRes, hideLabelRes ->
            when {
                hideControlsRes is Result.Failure -> Result.Failure(hideControlsRes.error)
                hideLastUpdateRes is Result.Failure -> Result.Failure(hideLastUpdateRes.error)
                hideLabelRes is Result.Failure -> Result.Failure(hideLabelRes.error)
                else -> Result.Success(
                    DisplayPreferences(
                        hideControls = (hideControlsRes as Result.Success).data,
                        hideLastUpdate = (hideLastUpdateRes as Result.Success).data,
                        hideCounterCategoryLabel = (hideLabelRes as Result.Success).data,
                    )
                )
            }
        }
    }
}
