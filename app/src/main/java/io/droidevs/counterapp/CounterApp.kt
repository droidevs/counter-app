package io.droidevs.counterapp

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.droidevs.counterapp.domain.usecases.category.*
import io.droidevs.counterapp.domain.usecases.counters.*

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