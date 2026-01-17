package io.droidevs.counterapp.repository

import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeCounterRepository(
    private val dummyData: DummyData
) : CounterRepository {

    private val countersFlow: Flow<Result<List<Counter>, DatabaseError>> =
        dummyData.countersFlow.asStateFlow()
            .map { counters ->
                Result.Success(counters.map { it.toDomain() })
            }

    override fun getLastEdited(limit: Int): Flow<Result<List<Counter>, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    result.data.filter { !it.isSystem }
                        .sortedByDescending { it.orderAnchorAt }
                        .take(limit)
                )

                is Result.Failure -> result
            }
        }
    }

    override fun getTotalCounters(): Flow<Result<Int, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.filter { !it.isSystem }.size)
                is Result.Failure -> result
            }
        }
    }

    override suspend fun saveCounter(counter: Counter): Result<Unit, DatabaseError> {
        val index = dummyData.counters.indexOfFirst { it.id == counter.id }
        return if (index != -1) {
            dummyData.counters[index] = counter.toEntity()
            dummyData.emitCounterUpdate()
            Result.Success(Unit)
        } else {
            Result.Failure(DatabaseError.NotFound)
        }
    }

    override suspend fun createCounter(counter: Counter): Result<Unit, DatabaseError> {
        dummyData.counters.add(counter.toEntity())
        val indexCategory = dummyData.categories.indexOfFirst { it.id == counter.categoryId }
        if (indexCategory != -1) {
            val category = dummyData.categories[indexCategory]
            val newCategory = category.copy(
                countersCount = category.countersCount + 1
            )
            dummyData.categories[indexCategory] = newCategory
            dummyData.emitCategoryUpdate()
            dummyData.emitCounterUpdate()
        }
        return Result.Success(Unit)
    }

    override suspend fun deleteCounter(counter: Counter): Result<Unit, DatabaseError> {
        dummyData.counters.removeIf { it.id == counter.id }
        val indexCategory = dummyData.categories.indexOfFirst { it.id == counter.categoryId }
        if (indexCategory != -1) {
            val category = dummyData.categories[indexCategory]
            val newCategory = category.copy(
                countersCount = category.countersCount - 1
            )
            dummyData.categories[indexCategory] = newCategory
            dummyData.emitCategoryUpdate()
            dummyData.emitCounterUpdate()
        }
        return Result.Success(Unit)
    }

    override fun getCountersWithCategories(): Flow<Result<List<CounterWithCategory>, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> {
                    val countersWithCategories = result.data.filter { !it.isSystem }
                        .sortedByDescending { it.orderAnchorAt }
                        .map { counter ->
                            val categoryEntity = dummyData.categories.find { category ->
                                category.id == counter.categoryId
                            }
                            CounterWithCategory(
                                counter = counter,
                                category = categoryEntity?.toDomain()
                            )
                        }
                    Result.Success(countersWithCategories)
                }

                is Result.Failure -> result
            }
        }
    }

    override fun getLastEditedWithCategory(limit: Int): Flow<Result<List<CounterWithCategory>, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> {
                    val counters = result.data.filter { !it.isSystem }
                        .sortedByDescending { it.orderAnchorAt }
                        .take(limit)
                    val categoriesMap = dummyData.categories.filter { !it.isSystem }
                        .associateBy { it.id }
                    val countersWithCategories = counters.map { counter ->
                        val categoryEntity = categoriesMap[counter.categoryId]
                        CounterWithCategory(
                            counter = counter,
                            category = categoryEntity?.toDomain()
                        )
                    }
                    Result.Success(countersWithCategories)
                }

                is Result.Failure -> result
            }
        }
    }

    override fun getSystemCounters(): Flow<Result<List<Counter>, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.filter { it.isSystem })
                is Result.Failure -> result
            }
        }
    }

    override suspend fun incrementSystemCounter(counterKey: String): Result<Unit, DatabaseError> {
        val index = dummyData.counters.indexOfFirst { it.kay == counterKey }
        if (index != -1) {
            val counter = dummyData.counters[index]
            val newCounter = counter.copy(
                currentCount = counter.currentCount + 1
            )
            dummyData.counters[index] = newCounter
            dummyData.emitCounterUpdate()
        }
        return Result.Success(Unit)
    }

    override suspend fun updateSystemCounter(counterKey: String, count: Int): Result<Unit, DatabaseError> {
        val index = dummyData.counters.indexOfFirst { it.kay == counterKey }
        if (index != -1) {
            val counter = dummyData.counters[index]
            val newCounter = counter.copy(
                currentCount = count
            )
            dummyData.counters[index] = newCounter
            dummyData.emitCounterUpdate()
        }
        return Result.Success(Unit)
    }

    override suspend fun deleteAllCounters(): Result<Unit, DatabaseError> {
        dummyData.counters.clear()
        dummyData.emitCounterUpdate()
        return Result.Success(Unit)
    }

    override fun getCounter(id: String): Flow<Result<Counter, DatabaseError>> {
        return countersFlow.map { result ->
            when (result) {
                is Result.Success -> {
                    val counter = result.data.firstOrNull { it.id == id }
                    if (counter != null) {
                        Result.Success(counter)
                    } else {
                        Result.Failure(DatabaseError.NotFound)
                    }
                }

                is Result.Failure -> result
            }
        }
    }

    override fun getAllCounters(): Flow<Result<List<Counter>, DatabaseError>> = countersFlow

    override suspend fun exportCounters(): Result<List<Counter>, DatabaseError> {
        return Result.Success(dummyData.counters.map { it.toDomain() })
    }

    override suspend fun importCounters(counters: List<Counter>): Result<Unit, DatabaseError> {
        dummyData.counters.addAll(counters.map { it.toEntity() })
        dummyData.emitCounterUpdate()
        return Result.Success(Unit)
    }
}
