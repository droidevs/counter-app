package io.droidevs.counterapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.droidevs.counterapp.data.converters.InstantConverters
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CounterEntity

@Database(
    entities = [CounterEntity::class,
                CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao

    abstract fun categoryDao(): CategoryDao

}
