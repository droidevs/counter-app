package io.droidevs.counterapp

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.CounterDao
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.model.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class CounterApp : Application() {

    private val isTest = false

    val testCounters = listOf(
        Counter(
            name = "Morning Routine",
            currentCount = 3,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(3600),
            lastUpdatedAt = Instant.now().minusSeconds(1800)
        ),
        Counter(
            name = "Water Intake",
            currentCount = 5,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(7200),
            lastUpdatedAt = Instant.now().minusSeconds(600)
        ),
        Counter(
            name = "Exercise Reps",
            currentCount = 12,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(10800),
            lastUpdatedAt = Instant.now().minusSeconds(3600)
        ),
        Counter(
            name = "Reading Pages",
            currentCount = 20,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(14400),
            lastUpdatedAt = Instant.now().minusSeconds(7200)
        ),
        Counter(
            name = "Meditation Minutes",
            currentCount = 15,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(18000),
            lastUpdatedAt = Instant.now().minusSeconds(9000)
        ),
        Counter(
            name = "Steps Walked",
            currentCount = 8000,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(21600),
            lastUpdatedAt = Instant.now().minusSeconds(1800)
        ),
        Counter(
            name = "Sleep Hours",
            currentCount = 6,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(25000),
            lastUpdatedAt = Instant.now().minusSeconds(12000)
        ),
        Counter(
            name = "Push-ups",
            currentCount = 30,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(27000),
            lastUpdatedAt = Instant.now().minusSeconds(15000)
        ),
        Counter(
            name = "Books Completed",
            currentCount = 2,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(30000),
            lastUpdatedAt = Instant.now().minusSeconds(5000)
        ),
        Counter(
            name = "Meals Tracked",
            currentCount = 7,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(33000),
            lastUpdatedAt = Instant.now().minusSeconds(1000)
        )
    )


    lateinit var database: AppDatabase
        private set

    lateinit var counterDao: CounterDao
        private set

    lateinit var counterRepository: CounterRepository
        private set



    override fun onCreate() {
        super.onCreate()

        if (isTest) {
            database = Room.inMemoryDatabaseBuilder(
                context = applicationContext,
                AppDatabase::class.java
            ).allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        runBlocking {
                            database.counterDao().insertAll(testCounters.map { it.toEntity() })
                        }

                    }
                })
                .build()
        } else {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "counter_db"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        database.counterDao().insertAll(testCounters.map { it.toEntity() })
                    }
                }
            }).build()
        }

        counterDao = database.counterDao()

        counterRepository = CounterRepository(counterDao)


    }
}