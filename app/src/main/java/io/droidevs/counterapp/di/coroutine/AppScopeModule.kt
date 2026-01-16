package io.droidevs.counterapp.di.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.ApplicationCoroutineScope
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.coroutines.annotation.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppScopeModule {

    @Provides
    @Singleton
    fun provideApplicationCoroutineScope(
        dispatcherProvider: DispatcherProvider
    ): ApplicationCoroutineScope = ApplicationCoroutineScope(dispatcherProvider)


    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(
        holder: ApplicationCoroutineScope
    ): CoroutineScope = holder.scope

}
