package io.droidevs.counterapp

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.data.DefaultDataInitializer
import io.droidevs.counterapp.data.SettingKeys
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.repository.CategoryRepositoryImpl
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.repository.CounterRepositoryImpl
import io.droidevs.counterapp.data.repository.SettingsRepositoryImpl
import io.droidevs.counterapp.repository.DummyData
import io.droidevs.counterapp.repository.FakeCategoryRepository
import io.droidevs.counterapp.repository.FakeCounterRepository
import io.droidevs.counterapp.repository.FakeSettingsRepository
import io.droidevs.counterapp.di.AppUseCases
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.repository.SettingsRepository
import io.droidevs.counterapp.internal.scheduleSystemCounterSync
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.droidevs.counterapp.domain.usecases.category.*
import io.droidevs.counterapp.domain.usecases.counters.*

class CounterApp : Application() {

    lateinit var useCases: AppUseCases
        private set

    private val isTest = false

//    val testCounters = DummyData.counters
//
//    val testCategories = DummyData.categories


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

    lateinit var settingsRepository: SettingsRepository



    @OptIn(DelicateCoroutinesApi::class)
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
//                        CoroutineScope(Dispatchers.IO).launch {
//                            database.counterDao().insertAll(testCounters)
//                            database.categoryDao().insertAll(testCategories)
//                        }
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
            val dummyData = io.droidevs.counterapp.repository.DummyData()
            counterRepository = FakeCounterRepository(dummyData = dummyData)
            categoryRepository = FakeCategoryRepository(dummyData = dummyData)

            settingsRepository = FakeSettingsRepository().apply {
                seed(
                    mapOf(
                        SettingKeys.SOUNDS_ON to true,
                        SettingKeys.THEME to "dark",
                        SettingKeys.HIDE_CONTROLS to false
                    )
                )
            }
            GlobalScope.launch(Dispatchers.IO) {
                categoryRepository.seedDefaults()
                counterRepository.seedDefaults()
            }

        } else {
            counterRepository = CounterRepositoryImpl(
                dao = counterDao,
                categoryDao = categoryDao
            )
            categoryRepository = CategoryRepositoryImpl(categoryDao)

            settingsRepository = SettingsRepositoryImpl(
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            )

            GlobalScope.launch(Dispatchers.IO) {
                DefaultDataInitializer.init(
                    categoryDao = categoryDao,
                    counterDao = counterDao
                )
            }
        }

        // Build grouped usecases similar to Dagger module providers so fragments can access them
        val createCategory = CreateCategoryUseCase(categoryRepository)
        val deleteCategory = DeleteCategoryUseCase(categoryRepository)
        val getAllCategories = GetAllCategoriesUseCase(categoryRepository)
        val getCategoryWithCounters = GetCategoryWithCountersUseCase(categoryRepository)
        val getExistingCategoryColors = GetExistingCategoryColorsUseCase(categoryRepository)
        val getSystemCategories = GetSystemCategoriesUseCase(categoryRepository)
        val getTopCategories = GetTopCategoriesUseCase(categoryRepository)
        val getTotalCategoriesCount = GetTotalCategoriesCountUseCase(categoryRepository)

        val categoryUseCases = io.droidevs.counterapp.domain.usecases.category.CategoryUseCases(
            createCategory,
            deleteCategory,
            getAllCategories,
            getCategoryWithCounters,
            getExistingCategoryColors,
            getSystemCategories,
            getTopCategories,
            getTotalCategoriesCount
        )

        val createCounter = CreateCounterUseCase(counterRepository)
        val deleteCounter = DeleteCounterUseCase(counterRepository)
        val getAllCounters = GetAllCountersUseCase(counterRepository)
        val getCountersWithCategories = GetCountersWithCategoriesUseCase(counterRepository)
        val getLimitCounters = GetLimitCountersUseCase(counterRepository)
        val getLimitCountersWithCategory = GetLimitCountersWithCategoryUseCase(counterRepository)
        val getSystemCounters = GetSystemCountersUseCase(counterRepository)
        val getTotalNumberOfCounters = GetTotalNumberOfCountersUseCase(counterRepository)
        val incrementSystemCounter = IncrementSystemCounterUseCase(counterRepository)
        val updateCounter = UpdateCounterUseCase(counterRepository)
        val updateSystemCounter = UpdateSystemCounterUseCase(counterRepository)

        val counterUseCases = io.droidevs.counterapp.domain.usecases.counters.CounterUseCases(
            createCounter,
            deleteCounter,
            getAllCounters,
            getCountersWithCategories,
            getLimitCounters,
            getLimitCountersWithCategory,
            getSystemCounters,
            getTotalNumberOfCounters,
            incrementSystemCounter,
            updateCounter,
            updateSystemCounter
        )

        useCases = AppUseCases(counterUseCases = counterUseCases, categoryUseCases = categoryUseCases)

        // todo: register system counters receivers
        scheduleSystemCounterSync(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        // unregister recievers
    }
}