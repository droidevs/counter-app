package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
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
import io.droidevs.counterapp.domain.usecases.counters.ResetCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateCounterUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateSystemCounterUseCase
import io.droidevs.counterapp.domain.usecases.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMinimumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.counters.ResolveCounterBehaviorUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): CreateCounterUseCase =
        CreateCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideDeleteCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): DeleteCounterUseCase =
        DeleteCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetAllCountersUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetAllCountersUseCase =
        GetAllCountersUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetCounterUseCase =
        GetCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetCountersWithCategoriesUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetCountersWithCategoriesUseCase =
        GetCountersWithCategoriesUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetLimitCountersUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetLimitCountersUseCase =
        GetLimitCountersUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetLimitCountersWithCategoryUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetLimitCountersWithCategoryUseCase =
        GetLimitCountersWithCategoryUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetSystemCountersUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetSystemCountersUseCase =
        GetSystemCountersUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetTotalNumberOfCountersUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): GetTotalNumberOfCountersUseCase =
        GetTotalNumberOfCountersUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideIncrementSystemCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): IncrementSystemCounterUseCase =
        IncrementSystemCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideUpdateCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): UpdateCounterUseCase =
        UpdateCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideUpdateSystemCounterUseCase(repository: CounterRepository, dispatchers: DispatcherProvider): UpdateSystemCounterUseCase =
        UpdateSystemCounterUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideResolveCounterBehaviorUseCase(
        getCounterIncrementStep: GetCounterIncrementStepUseCase,
        getCounterDecrementStep: GetCounterDecrementStepUseCase,
        getDefaultCounterValue: GetDefaultCounterValueUseCase,
        getMinimumCounterValue: GetMinimumCounterValueUseCase,
        getMaximumCounterValue: GetMaximumCounterValueUseCase,
    ): ResolveCounterBehaviorUseCase =
        ResolveCounterBehaviorUseCase(
            getCounterIncrementStep = getCounterIncrementStep,
            getCounterDecrementStep = getCounterDecrementStep,
            getDefaultCounterValue = getDefaultCounterValue,
            getMinimumCounterValue = getMinimumCounterValue,
            getMaximumCounterValue = getMaximumCounterValue,
        )

    @Provides
    @Singleton
    fun provideIncrementCounterUseCase(
        resolveBehavior: ResolveCounterBehaviorUseCase,
        updateCounterUseCase: UpdateCounterUseCase,
        addHistoryUseCase: AddHistoryEventUseCase,
        dispatchers: DispatcherProvider
    ): IncrementCounterUseCase =
        IncrementCounterUseCase(
            resolveBehavior = resolveBehavior,
            updateCounterUseCase = updateCounterUseCase,
            addHistoryEventUseCase = addHistoryUseCase,
            dispatchers = dispatchers
        )

    @Provides
    @Singleton
    fun provideDecrementCounterUseCase(
        resolveBehavior: ResolveCounterBehaviorUseCase,
        updateCounterUseCase: UpdateCounterUseCase,
        addHistoryUseCase: AddHistoryEventUseCase,
        dispatchers: DispatcherProvider
    ): DecrementCounterUseCase =
        DecrementCounterUseCase(
            resolveBehavior = resolveBehavior,
            updateCounterUseCase = updateCounterUseCase,
            addHistoryEventUseCase = addHistoryUseCase,
            dispatchers = dispatchers
        )

    @Provides
    @Singleton
    fun provideResetCounterUseCase(
        resolveBehavior: ResolveCounterBehaviorUseCase,
        updateCounterUseCase: UpdateCounterUseCase,
        dispatchers: DispatcherProvider,
    ): ResetCounterUseCase =
        ResetCounterUseCase(
            resolveBehavior = resolveBehavior,
            updateCounterUseCase = updateCounterUseCase,
            dispatchers = dispatchers
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
        decrementCounterUseCase: DecrementCounterUseCase,
        resetCounterUseCase: ResetCounterUseCase,
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
        decrementCounterUseCase,
        resetCounterUseCase,
    )
}
