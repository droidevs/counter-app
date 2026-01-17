package io.droidevs.counterapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import io.droidevs.counterapp.domain.coroutines.ApplicationCoroutineScope
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import javax.inject.Inject

@HiltAndroidApp
class CounterApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appScopeHolder: ApplicationCoroutineScope

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(appScopeHolder)

        scheduleSystemCounterSync(this)
    }
}