package io.droidevs.counterapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.droidevs.counterapp.data.converters.InstantConverters
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.dao.HistoryEventDao
import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.data.entities.HistoryEventEntity

/**
 * The main database class for the application.
 *
 * It declares the entities, the database version, and provides access to the DAOs.
 * The schema is exported to a file, which is a best practice for version control
 * and for validating migrations during tests.
 *
 * @see DatabaseConfig for versioning.
 * @see Migrations for schema upgrade logic.
 */
@Database(
    entities = [
        CounterEntity::class,
        CategoryEntity::class,
        HistoryEventEntity::class
    ],
    version = DatabaseConfig.DATABASE_VERSION, // Use the version from DatabaseConfig
    exportSchema = true
)
@TypeConverters(InstantConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao

    abstract fun categoryDao(): CategoryDao

    abstract fun historyEventDao(): HistoryEventDao
}
