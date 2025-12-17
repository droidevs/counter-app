package io.droidevs.counterapp

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.CategoryDao
import io.droidevs.counterapp.data.CategoryRepository
import io.droidevs.counterapp.data.CounterDao
import io.droidevs.counterapp.data.CounterRepositoryImpl
import io.droidevs.counterapp.data.fake.DummyData
import io.droidevs.counterapp.data.fake.FakeCounterRepository
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

class CounterApp : Application() {

    private val isTest = true

    val testCounters = DummyData.getCounters()

    val testCategories = DummyData.getCategories()


    lateinit var database: AppDatabase
        private set

    lateinit var counterDao: CounterDao
        private set

    lateinit var categoryDao: CategoryDao
        private set

    lateinit var counterRepository: CounterRepository
        private set

    lateinit var categoryRepository: CategoryRepository
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
                        CoroutineScope(Dispatchers.IO).launch {
                            //database.counterDao().insertAll(testCounters)
                            database.categoryDao().insertAll(testCategories)
                        }
                    }
                })
                .build()
        } else {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "counter_db"
            ).build()
        }

        counterDao = database.counterDao()
        categoryDao = database.categoryDao()

        if (isTest) {
            counterRepository = FakeCounterRepository()
        } else {
            counterRepository = CounterRepositoryImpl(counterDao)
        }
        categoryRepository = CategoryRepository(categoryDao)

    }
}