package io.droidevs.counterapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CategoryWithCountersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("UPDATE categories SET counters_count = counters_count + 1 WHERE id = :categoryId")
    suspend fun incrementCounterCount(categoryId: String)

    @Query("UPDATE categories SET counters_count = counters_count - 1 WHERE id = :categoryId")
    suspend fun decrementCounterCount(categoryId: String)

    @Query("UPDATE categories SET counters_count = 0 WHERE is_system = 0")
    suspend fun resetAllUserCategoryCounts()

    @Transaction
    suspend fun recalculateAllCategoryCounts() {
        resetAllUserCategoryCounts()
        val counts = getCategoryCounterCounts()
        counts.forEach { count ->
            setCategoryCount(count.categoryId, count.count)
        }
    }

    @Query("""
        SELECT categoryId, COUNT(*) as count FROM counters
        WHERE categoryId IS NOT NULL AND is_system = 0
        GROUP BY categoryId
    """)
    fun getCategoryCounterCounts(): List<CategoryCounterCount>

    @Query("UPDATE categories SET counters_count = :count WHERE id = :categoryId")
    suspend fun setCategoryCount(categoryId: String, count: Int)

    @Query("SELECT * FROM categories WHERE is_system = 0 ORDER BY counters_count DESC LIMIT :limit")
    fun getTopCategories(limit : Int): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories WHERE is_system = 0")
    fun getTotalCategoriesCount() : Flow<Int>

    @Query("SELECT * FROM categories WHERE is_system = 0")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE is_system = 1")
    fun getSystemCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Insert
    suspend fun insert(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE kay = :key LIMIT 1")
    suspend fun getByKey(key: String): CategoryEntity?

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId AND is_system = 0")
    fun getCategoryWithCounters(categoryId: String): Flow<CategoryWithCountersEntity>

    @Update
    fun updateCategory(category : CategoryEntity)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategory(categoryId : String) : Flow<CategoryEntity>

    @Query("DELETE FROM categories WHERE id = :categoryId")
    fun deleteCategory(categoryId : String)

    @Query("SELECT color FROM categories WHERE color != 0") // Exclude defaults
    suspend fun getExistingCategoryColors(): List<Int>

}

data class CategoryCounterCount(
    val categoryId: String,
    val count: Int
)