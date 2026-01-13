package io.droidevs.counterapp

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.HiltAndroidApp
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import kotlinx.coroutines.DelicateCoroutinesApi
import io.droidevs.counterapp.domain.usecases.category.*
import io.droidevs.counterapp.domain.usecases.counters.*


@HiltAndroidApp
class CounterApp : Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        scheduleSystemCounterSync(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}