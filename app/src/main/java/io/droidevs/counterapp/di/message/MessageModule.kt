package io.droidevs.counterapp.di.message

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.message.actions.UiActionHandler
import io.droidevs.counterapp.ui.message.actions.handler.UiActionHandlerImpl
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.message.dispatcher.impl.UiMessageDispatcherImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiMessageModule {

    @Provides
    @Singleton
    fun provideUiMessageDispatcher(): UiMessageDispatcher =
        UiMessageDispatcherImpl()

    @Provides
    @Singleton
    fun provideUiActionHandler(): UiActionHandler =
        UiActionHandlerImpl()
}
