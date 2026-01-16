package io.droidevs.counterapp

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.HiltAndroidApp
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.domain.coroutines.ApplicationCoroutineScope
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import kotlinx.coroutines.DelicateCoroutinesApi
import io.droidevs.counterapp.domain.usecases.category.*
import io.droidevs.counterapp.domain.usecases.counters.*
import javax.inject.Inject


@HiltAndroidApp
class CounterApp : Application() {

    @Inject
    lateinit var appScopeHolder: ApplicationCoroutineScope

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(appScopeHolder)

        scheduleSystemCounterSync(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}