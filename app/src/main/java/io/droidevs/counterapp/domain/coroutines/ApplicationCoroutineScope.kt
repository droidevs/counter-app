package io.droidevs.counterapp.domain.coroutines

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCoroutineScope @Inject constructor(
    dispatchers: DispatcherProvider
) : DefaultLifecycleObserver {

    private val job = SupervisorJob()

    val scope: CoroutineScope =
        CoroutineScope(job + dispatchers.default)

    override fun onDestroy(owner: LifecycleOwner) {
        job.cancel()
    }
}
