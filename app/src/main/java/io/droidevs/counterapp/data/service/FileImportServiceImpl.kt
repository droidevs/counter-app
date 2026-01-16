package io.droidevs.counterapp.data.service

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import io.droidevs.counterapp.domain.model.Backup
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.domain.services.BackupExport
import io.droidevs.counterapp.domain.services.CategoryExport
import io.droidevs.counterapp.domain.services.CounterExport
import io.droidevs.counterapp.domain.services.FileImportService
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class FileImportServiceImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : FileImportService {
    override suspend fun import(fileUri: Uri): Result<Backup, FileError> {
        return runCatchingFileResult {
            val inputStream = context.contentResolver.openInputStream(fileUri) ?: throw Exception("Failed to open file")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val backupExport: BackupExport = gson.fromJson(reader, BackupExport::class.java)
            reader.close()
            inputStream.close()
            backupExport.toBackup()
        }
    }

    private fun BackupExport.toBackup(): Backup {
        return Backup(
            counters = counters.map { it.toCounter() },
            categories = categories.map { it.toCategory() }
        )
    }

    private fun CounterExport.toCounter(): Counter {
        return Counter(
            id = id,
            name = name,
            currentCount = value,
            categoryId = category,
            createdAt = createdAt,
            lastUpdatedAt = updatedAt,
            canIncrease = canIncrease,
            canDecrease = canDecrease
        )
    }

    private fun CategoryExport.toCategory(): Category {
        return Category(
            id = id,
            name = name,
            color = CategoryColor.of(Integer.parseInt(color)),
            countersCount = 0
        )
    }
}
