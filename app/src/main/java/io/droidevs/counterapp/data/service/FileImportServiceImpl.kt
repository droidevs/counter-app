package io.droidevs.counterapp.data.service

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.services.ImportResult
import java.io.BufferedReader
import java.io.InputStreamReader

class FileImportServiceImpl(private val context: Context, private val gson: Gson) : FileImportService {
    override suspend fun import(fileUri: Uri): ImportResult {
        return try {
            val inputStream = context.contentResolver.openInputStream(fileUri) ?: return ImportResult.Error("Failed to open file")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val type = object : TypeToken<List<Counter>>() {}.type
            val counters: List<Counter> = gson.fromJson(reader, type)
            reader.close()
            inputStream.close()
            ImportResult.Success(counters)
        } catch (e: Exception) {
            e.printStackTrace()
            ImportResult.Error("Failed to import counters: ${e.message}")
        }
    }
}
