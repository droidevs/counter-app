package io.droidevs.counterapp.data

/**
 * Centralized configuration for the application's database.
 * This object holds constants related to the database schema, ensuring consistency
 * across the application, including in-app logic, dependency injection, and tests.
 */
object DatabaseConfig {
    /**
     * The name of the Room database file.
     */
    const val DATABASE_NAME = "counter_app.db"

    /**
     * The current version of the database schema.
     *
     * IMPORTANT: Increment this version number every time you make a schema change.
     * Each new version MUST have a corresponding Migration object defined in [Migrations].
     */
    const val DATABASE_VERSION = 5 // Set to the final version after all creation migrations
}
