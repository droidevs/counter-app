package io.droidevs.counterapp

import android.app.Application
import androidx.room.Room
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.CounterDao
import io.droidevs.counterapp.data.CounterRepository

class CounterApp : Application() {

    private val isTest = true

    lateinit var database: AppDatabase
        private set




    override fun onCreate() {
        super.onCreate()

        if (isTest) {
            Room.inMemoryDatabaseBuilder(
                context = applicationContext,
                AppDatabase::class.java
            ).allowMainThreadQueries()
                .build()
        } else {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "counter_db"
            ).build()
        }

    }
}