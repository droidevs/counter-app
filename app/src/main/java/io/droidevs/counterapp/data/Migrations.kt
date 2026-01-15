package io.droidevs.counterapp.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * A central repository for all database migrations.
 *
 * This migration sequence builds the entire database schema from scratch,
 * ensuring the schema is fully version-controlled.
 */
object Migrations {

    /**
     * Migration from version 1 to 2.
     *
     * Creates the initial 'categories' table.
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `categories` (
                    `id` TEXT NOT NULL, 
                    `kay` TEXT, 
                    `name` TEXT NOT NULL, 
                    `color` INTEGER NOT NULL, 
                    `counters_count` INTEGER NOT NULL, 
                    `is_system` INTEGER NOT NULL, 
                    `created_at` INTEGER NOT NULL, 
                    `last_updated_at` INTEGER,
                    PRIMARY KEY(`id`)
                )
            """.trimIndent())
        }
    }

    /**
     * Migration from version 2 to 3.
     *
     * Creates the 'counters' table and establishes the foreign key relationship
     * to the 'categories' table. Also creates necessary indices.
     */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `counters` (
                    `id` TEXT NOT NULL, 
                    `kay` TEXT, 
                    `name` TEXT NOT NULL, 
                    `current_count` INTEGER NOT NULL, 
                    `can_increment` INTEGER NOT NULL, 
                    `can_decrement` INTEGER NOT NULL, 
                    `category_id` TEXT, 
                    `is_system` INTEGER NOT NULL, 
                    `created_at` INTEGER NOT NULL, 
                    `last_updated_at` INTEGER, 
                    `order_anchor_at` INTEGER, 
                    PRIMARY KEY(`id`), 
                    FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_counters_category_id` ON `counters`(`category_id`)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_counters_kay` ON `counters`(`kay`)")
        }
    }

    /**
     * Migration from version 3 to 4.
     *
     * Creates the 'history_events' table with a foreign key to the 'counters' table.
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `history_events` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    `counter_id` TEXT NOT NULL, 
                    `old_value` INTEGER NOT NULL, 
                    `new_value` INTEGER NOT NULL, 
                    `change` INTEGER NOT NULL, 
                    `timestamp` INTEGER NOT NULL, 
                    FOREIGN KEY(`counter_id`) REFERENCES `counters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_history_events_counter_id` ON `history_events`(`counter_id`)")
        }
    }

    /**
     * A complete list of all migrations required to build the schema.
     */
    val ALL = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4
    )
}
