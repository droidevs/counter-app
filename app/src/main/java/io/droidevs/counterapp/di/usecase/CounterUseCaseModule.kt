package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.counters.CreateCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.DecrementCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.DeleteCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetAllCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetCountersWithCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetLimitCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetLimitCountersWithCategoryUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetSystemCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.GetTotalNumberOfCountersUseCase
import io.droidevs.counterapp.domain.usecases.counters.IncrementCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.IncrementSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.history.GetHistoryUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateCounterUseCase(repository: CounterRepository): CreateCounterUseCase =
        CreateCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCounterUseCase(repository: CounterRepository): DeleteCounterUseCase =
        DeleteCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllCountersUseCase(repository: CounterRepository): GetAllCountersUseCase =
        GetAllCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCounterUseCase(repository: CounterRepository): GetCounterUseCase =
        GetCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCountersWithCategoriesUseCase(repository: CounterRepository): GetCountersWithCategoriesUseCase =
        GetCountersWithCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLimitCountersUseCase(repository: CounterRepository): GetLimitCountersUseCase =
        GetLimitCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLimitCountersWithCategoryUseCase(repository: CounterRepository): GetLimitCountersWithCategoryUseCase =
        GetLimitCountersWithCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSystemCountersUseCase(repository: CounterRepository): GetSystemCountersUseCase =
        GetSystemCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTotalNumberOfCountersUseCase(repository: CounterRepository): GetTotalNumberOfCountersUseCase =
        GetTotalNumberOfCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideIncrementSystemCounterUseCase(repository: CounterRepository): IncrementSystemCounterUseCase =
        IncrementSystemCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCounterUseCase(repository: CounterRepository): UpdateCounterUseCase =
        UpdateCounterUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateSystemCounterUseCase(repository: CounterRepository): UpdateSystemCounterUseCase =
        UpdateSystemCounterUseCase(repository)


    fun provideIncrementCounterUseCase(
        updateCounterUseCase: UpdateCounterUseCase,
        getCounterIncrementStepUseCase: GetCounterIncrementStepUseCase,
        addHistoryUseCase: AddHistoryEventUseCase
    ): IncrementCounterUseCase =
        IncrementCounterUseCase(
            updateCounterUseCase = updateCounterUseCase,
            getCounterIncrementStepUseCase =getCounterIncrementStepUseCase,
            addHistoryEventUseCase = addHistoryUseCase
        )

    fun provideDecrementCounterUseCase(
        updateCounterUseCase: UpdateCounterUseCase,
        getCounterDecrementStepUseCase: GetCounterDecrementStepUseCase,
        addHistoryUseCase: AddHistoryEventUseCase
    ): DecrementCounterUseCase =
        DecrementCounterUseCase(
            updateCounterUseCase = updateCounterUseCase,
            getCounterDecrementStepUseCase =getCounterDecrementStepUseCase,
            addHistoryEventUseCase = addHistoryUseCase
        )

    @Provides
    @Singleton
    fun provideCounterUseCases(
        createCounter: CreateCounterUseCase,
        deleteCounter: DeleteCounterUseCase,
        getAllCounters: GetAllCountersUseCase,
        getCounter: GetCounterUseCase,
        getCountersWithCategories: GetCountersWithCategoriesUseCase,
        getLimitCounters: GetLimitCountersUseCase,
        getLimitCountersWithCategory: GetLimitCountersWithCategoryUseCase,
        getSystemCounters: GetSystemCountersUseCase,
        getTotalNumberOfCounters: GetTotalNumberOfCountersUseCase,
        incrementSystemCounter: IncrementSystemCounterUseCase,
        updateCounter: UpdateCounterUseCase,
        updateSystemCounter: UpdateSystemCounterUseCase,
        incrementCounterUseCase: IncrementCounterUseCase,
        decrementCounterUseCase: DecrementCounterUseCase
    ): CounterUseCases = CounterUseCases(
        createCounter,
        deleteCounter,
        getAllCounters,
        getCounter,
        getCountersWithCategories,
        getLimitCounters,
        getLimitCountersWithCategory,
        getSystemCounters,
        getTotalNumberOfCounters,
        incrementSystemCounter,
        updateCounter,
        updateSystemCounter,
        incrementCounterUseCase,
        decrementCounterUseCase
    )
}
