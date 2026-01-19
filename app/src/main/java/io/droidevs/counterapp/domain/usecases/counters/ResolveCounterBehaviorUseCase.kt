package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.counter.CounterBehavior
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMinimumCounterValueUseCase
import javax.inject.Inject

/**
 * Resolves the effective behavior for a counter:
 * - Prefer per-counter overrides when provided.
 * - If [Counter.useDefaultBehavior] is true, fall back to global prefs.
 * - If [Counter.useDefaultBehavior] is false, and override is null: no restriction for min/max; steps/default have safe fallbacks.
 */
class ResolveCounterBehaviorUseCase @Inject constructor(
    private val getCounterIncrementStep: GetCounterIncrementStepUseCase,
    private val getCounterDecrementStep: GetCounterDecrementStepUseCase,
    private val getDefaultCounterValue: GetDefaultCounterValueUseCase,
    private val getMinimumCounterValue: GetMinimumCounterValueUseCase,
    private val getMaximumCounterValue: GetMaximumCounterValueUseCase,
) {

    suspend operator fun invoke(counter: Counter): Result<CounterBehavior, PreferenceError> {
        if (!counter.useDefaultBehavior) {
            return CounterBehavior(
                incrementStep = counter.incrementStep ?: 1,
                decrementStep = counter.decrementStep ?: 1,
                minValue = counter.minValue,
                maxValue = counter.maxValue,
                defaultValue = counter.defaultValue ?: 0,
            ).asSuccess()
        }

        return resultSuspend {
            val incRes = resultSuspendFromFlow { getCounterIncrementStep() }
            val decRes = resultSuspendFromFlow { getCounterDecrementStep() }
            val defRes = resultSuspendFromFlow { getDefaultCounterValue() }
            val minRes = resultSuspendFromFlow { getMinimumCounterValue() }
            val maxRes = resultSuspendFromFlow { getMaximumCounterValue() }

            when {
                incRes is Result.Failure -> Result.Failure(incRes.error)
                decRes is Result.Failure -> Result.Failure(decRes.error)
                defRes is Result.Failure -> Result.Failure(defRes.error)
                minRes is Result.Failure -> Result.Failure(minRes.error)
                maxRes is Result.Failure -> Result.Failure(maxRes.error)
                else -> {
                    val globalInc = (incRes as Result.Success).data
                    val globalDec = (decRes as Result.Success).data
                    val globalDefault = (defRes as Result.Success).data
                    val globalMin = (minRes as Result.Success).data
                    val globalMax = (maxRes as Result.Success).data

                    Result.Success(
                        CounterBehavior(
                            incrementStep = counter.incrementStep ?: globalInc,
                            decrementStep = counter.decrementStep ?: globalDec,
                            minValue = counter.minValue ?: globalMin,
                            maxValue = counter.maxValue ?: globalMax,
                            defaultValue = counter.defaultValue ?: globalDefault,
                        )
                    )
                }
            }
        }
    }
}
