package io.droidevs.counterapp.domain.usecases.counters

data class CounterUseCases(
    val createCounter: CreateCounterUseCase,
    val deleteCounter: DeleteCounterUseCase,
    val getAllCounters: GetAllCountersUseCase,
    val getCountersWithCategories: GetCountersWithCategoriesUseCase,
    val getLimitCounters: GetLimitCountersUseCase,
    val getLimitCountersWithCategory: GetLimitCountersWithCategoryUseCase,
    val getSystemCounters: GetSystemCountersUseCase,
    val getTotalNumberOfCounters: GetTotalNumberOfCountersUseCase,
    val incrementSystemCounter: IncrementSystemCounterUseCase,
    val updateCounter: UpdateCounterUseCase,
    val updateSystemCounter: UpdateSystemCounterUseCase
)
