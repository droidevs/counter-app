package io.droidevs.counterapp.di.permissions

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.data.permissions.AndroidPermissionGateway
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import javax.inject.Provider

@Module
@InstallIn(ActivityComponent::class)
object PermissionModule {

    @Provides
    fun providePermissionGateway(
        @ApplicationContext appContext: Context,
        @ActivityContext activityContext: Provider<Context>
    ): PermissionGateway = AndroidPermissionGateway(appContext = appContext, activityContextProvider = activityContext)
}
