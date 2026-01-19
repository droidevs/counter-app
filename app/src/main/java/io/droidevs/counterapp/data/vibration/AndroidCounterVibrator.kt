package io.droidevs.counterapp.data.vibration

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.runCatchingResult
import io.droidevs.counterapp.domain.vibration.CounterVibrationAction
import io.droidevs.counterapp.domain.vibration.CounterVibrator
import io.droidevs.counterapp.domain.vibration.VibrationError
import javax.inject.Inject

class AndroidCounterVibrator @Inject constructor(
    @ApplicationContext private val context: Context,
) : CounterVibrator {

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    @SuppressLint("MissingPermission") // gated by manifest permission + user preference
    override suspend fun vibrate(action: CounterVibrationAction): Result<Unit, VibrationError> {
        val vibrator = getVibrator() ?: return Result.Failure(VibrationError.NotSupported)
        if (!vibrator.hasVibrator()) return Result.Failure(VibrationError.NotSupported)

        return runCatchingResult(
            errorTransform = { VibrationError.Failed(it) }
        ) {
            val durationMs = when (action) {
                CounterVibrationAction.INCREMENT -> 20L
                CounterVibrationAction.DECREMENT -> 35L
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            } else {
                // Safe compat path for API < 26.
                vibrator.vibrate(durationMs)
            }
        }
    }
}
