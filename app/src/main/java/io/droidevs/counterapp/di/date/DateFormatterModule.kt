package io.droidevs.counterapp.di.date

import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.date.TimeAgoDateFormatter


@Module
@InstallIn(SingletonComponent::class)
class DateFormatterModule {


    fun provideDateFormatter(
        @ApplicationContext context: Context
    ): DateFormatter {
        return TimeAgoDateFormatter(
            context = context
        )
    }
}