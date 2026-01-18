package io.droidevs.counterapp.di.permissions

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.permissions.AndroidPermissionGateway
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionSingletonModule {

    @Provides
    @Singleton
    fun providePermissionGateway(
        @ApplicationContext appContext : Context
    ): io.droidevs.counterapp.domain.permissions.PermissionGateway = AndroidPermissionGateway(
        appContext = appContext
    )
}
