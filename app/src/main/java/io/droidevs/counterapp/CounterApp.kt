package io.droidevs.counterapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import io.droidevs.counterapp.domain.coroutines.ApplicationCoroutineScope
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import io.sentry.EventProcessor
import io.sentry.Hint
import io.sentry.Sentry
import io.sentry.SentryOptions.BeforeBreadcrumbCallback
import io.sentry.UserFeedback
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import io.sentry.android.navigation.SentryNavigationListener
import io.sentry.protocol.SentryTransaction
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
        SentryAndroid.init(this) { options ->
            options.setDsn("https://88caacfc49cecb1859f4422b08c1e982@o4510739550502912.ingest.us.sentry.io/4510739602997248")

            options.isEnableAutoActivityLifecycleTracing = false

            // Enable automatic user interaction tracing (clicks, scrolls)
            options.isEnableUserInteractionTracing = true

            // Attach screenshot on crashes
            options.isAttachScreenshot = true

            // Attach view hierarchy on crashes
            options.isAttachViewHierarchy = true

            // To set a uniform sample rate
            options.setTracesSampleRate(1.0)

            // OR if you prefer, determine traces sample rate based on the sampling context
//            options.setTracesSampler(
//                { context -> })
            options.addIntegration(
                FragmentLifecycleIntegration(
                    this,
                    enableFragmentLifecycleBreadcrumbs = true, // enabled by default
                    enableAutoFragmentLifecycleTracing = true  // disabled by default
                )
            )

            options.beforeBreadcrumb = BeforeBreadcrumbCallback { breadcrumb, hint ->
                if (SentryNavigationListener.NAVIGATION_OP == breadcrumb.category) {
                    breadcrumb.data.remove("from_arguments")
                    breadcrumb.data.remove("to_arguments")
                }
                breadcrumb
            }
            options.addEventProcessor(object : EventProcessor {
                override fun process(transaction: SentryTransaction, hint: Hint): SentryTransaction? {
                    if (SentryNavigationListener.NAVIGATION_OP == transaction.contexts.trace?.operation) {
                        transaction.removeExtra("arguments")
                    }
                    return transaction
                }
            })
        }
        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(appScopeHolder)

        scheduleSystemCounterSync(this)
    }
}