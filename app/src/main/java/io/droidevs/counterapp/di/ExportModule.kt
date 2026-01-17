package io.droidevs.counterapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.service.FileExportServiceImpl
import io.droidevs.counterapp.data.service.FileImportServiceImpl
import io.droidevs.counterapp.domain.services.FileExportService
import io.droidevs.counterapp.domain.services.FileImportService
import java.time.Instant
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {

    @Provides
    @Singleton
    fun provideFileExportService(
        @ApplicationContext context: Context,
        gson: Gson
    ): FileExportService {
        return FileExportServiceImpl(context, gson)
    }

    @Provides
    @Singleton
    fun provideFileImportService(
        @ApplicationContext context: Context,
        gson: Gson
    ): FileImportService {
        return FileImportServiceImpl(context, gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
            .setPrettyPrinting()
            .create()
    }
}

// Instant TypeAdapter for Gson
class InstantTypeAdapter : TypeAdapter<Instant>() {
    override fun write(out: JsonWriter, value: Instant?) {
        out.value(value?.toString())
    }

    override fun read(reader: JsonReader): Instant? {
        return reader.nextString()?.let { Instant.parse(it) }
    }
}
