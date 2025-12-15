package io.droidevs.counterapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
@TypeConverters()
abstract class AppDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao
}
