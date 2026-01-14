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
import io.droidevs.counterapp.data.DatabaseConfig
import io.droidevs.counterapp.data.Migrations
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
        val builder = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DatabaseConfig.DATABASE_NAME
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                GlobalScope.launch {
                    dataInitializer.get().init()
                }
            }
        })
        // Add all registered migrations from the Migrations object
        .addMigrations(*Migrations.ALL)

        // For debug builds, allow falling back to a destructive migration.
        // For release builds, this is disabled. If a migration is missing,
        // the app will crash, preventing potential data loss.
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration()
        }

        return builder.build()
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
