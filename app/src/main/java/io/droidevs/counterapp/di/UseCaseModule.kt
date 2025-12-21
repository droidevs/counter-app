package io.droidevs.counterapp.di

import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.category.CreateCategoryUseCase
import io.droidevs.counterapp.domain.usecases.category.DeleteCategoryUseCase
import io.droidevs.counterapp.domain.usecases.category.GetAllCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetCategoryWithCountersUseCase
import io.droidevs.counterapp.domain.usecases.category.GetExistingCategoryColorsUseCase
import io.droidevs.counterapp.domain.usecases.category.GetSystemCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetTopCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetTotalCategoriesCountUseCase
import io.droidevs.counterapp.domain.usecases.counters.CreateCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.DeleteCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetAllCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetCountersWithCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetLimitCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetLimitCountersWithCategoryUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetSystemCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetTotalNumberOfCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.IncrementSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateSystemCounterUseCase
import kotlin.getValue

class UseCaseModule(
    private val categoryRepository: CategoryRepository,
    private val counterRepository: CounterRepository
) {

    val categoryUseCases by lazy { CategoryUseCases(categoryRepository) }
    val counterUseCases by lazy { CounterUseCases(counterRepository) }

    // ===== Category Use Cases =====
    class CategoryUseCases(categoryRepository: CategoryRepository) {
        val getTopCategoriesUseCase by lazy { GetTopCategoriesUseCase(categoryRepository) }
        val getTotalCategoriesCountUseCase by lazy { GetTotalCategoriesCountUseCase(categoryRepository) }
        val getCategoryWithCountersUseCase by lazy { GetCategoryWithCountersUseCase(categoryRepository) }
        val getAllCategoriesUseCase by lazy { GetAllCategoriesUseCase(categoryRepository) }
        val getSystemCategoriesUseCase by lazy { GetSystemCategoriesUseCase(categoryRepository) }
        val createCategoryUseCase by lazy { CreateCategoryUseCase(categoryRepository) }
        val deleteCategoryUseCase by lazy { DeleteCategoryUseCase(categoryRepository) }
        val getExistingCategoryColorsUseCase by lazy { GetExistingCategoryColorsUseCase(categoryRepository) }
    }
    // ===== Counter Use Cases =====
    class CounterUseCases(counterRepository: CounterRepository) {
        val getAllCountersUseCase by lazy { GetAllCountersUseCase(counterRepository) }
        val getLimitCountersUseCase by lazy { GetLimitCountersUseCase(counterRepository) }
        val getTotalNumberOfCountersUseCase by lazy { GetTotalNumberOfCountersUseCase(counterRepository) }
        val getCountersWithCategoriesUseCase by lazy { GetCountersWithCategoriesUseCase(counterRepository) }
        val getLimitCountersWithCategoryUseCase by lazy { GetLimitCountersWithCategoryUseCase(counterRepository) }
        val getSystemCountersUseCase by lazy { GetSystemCountersUseCase(counterRepository) }
        val createCounterUseCase by lazy { CreateCounterUseCase(counterRepository) }
        val deleteCounterUseCase by lazy { DeleteCounterUseCase(counterRepository) }
        val updateCounterUseCase by lazy { UpdateCounterUseCase(counterRepository) }
        val incrementSystemCounterUseCase by lazy { IncrementSystemCounterUseCase(counterRepository) }
        val updateSystemCounterUseCase by lazy { UpdateSystemCounterUseCase(counterRepository) }
    }
}