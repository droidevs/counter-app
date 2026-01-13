package io.droidevs.counterapp.di.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.data.AppDatabase
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.repository.DummyData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        dataInitializer: Provider<DataInitializer>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "counter_app_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    dataInitializer.get().init()
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideDummyData(): DummyData? {
        if (BuildConfig.DEBUG) {
            return DummyData()
        }
        return null
    }
}
