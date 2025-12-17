package io.droidevs.counterapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY counters_count DESC LIMIT :limit")
    fun getTopCategories(limit : Int): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories")
    fun getTotalCategoriesCount() : Flow<Int>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)
    @Insert
    suspend fun insert(category: CategoryEntity)


    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryWithCounters(
        categoryId: String
    ) : Flow<CategoryWithCountersEntity>

}
