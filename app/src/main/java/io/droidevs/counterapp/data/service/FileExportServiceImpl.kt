package io.droidevs.counterapp.data.service

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.services.CounterExport
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileExportServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : FileExportService {

    private companion object {
        private const val EXPORT_DIRECTORY = "exports"
        private const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"
        private const val FILE_PREFIX = "counters_export"
        private const val BUFFER_SIZE = 8192
    }

    override suspend fun exportCounters(counters: List<Counter>): ExportResult {
        return try {
            val exportData = counters.map { it.toExportModel() }
            val fileUri = createExportFile(exportData, ExportFormat.JSON)
            ExportResult.Success(fileUri, generateFileName(ExportFormat.JSON))
        } catch (e: Exception) {
            ExportResult.Error("Failed to export counters: ${e.message}", e)
        }
    }

    override suspend fun exportToFile(counters: List<Counter>): Uri {
        val exportData = counters.map { it.toExportModel() }
        return createExportFile(exportData, ExportFormat.JSON)
    }

    override suspend fun exportToShare(counters: List<Counter>): ExportResult {
        return try {
            val exportData = counters.map { it.toExportModel() }
            val tempFile = createTempExportFile(exportData, ExportFormat.JSON)
            val fileUri = getUriForFile(tempFile)
            ExportResult.Success(fileUri, tempFile.name)
        } catch (e: Exception) {
            ExportResult.Error("Failed to create share file: ${e.message}", e)
        }
    }

    override fun getExportDirectory(): File {
        return File(context.getExternalFilesDir(null), EXPORT_DIRECTORY).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun getAvailableExportFormats(): List<ExportFormat> {
        return ExportFormat.values().toList()
    }

    // Private helper methods

    private suspend fun createExportFile(
        counters: List<CounterExport>,
        format: ExportFormat
    ): Uri {
        return withContext(Dispatchers.IO) {
            val exportDir = getExportDirectory()
            val fileName = generateFileName(format)
            val exportFile = File(exportDir, fileName)

            when (format) {
                ExportFormat.CSV -> exportToCsv(counters, exportFile)
                ExportFormat.JSON -> exportToJson(counters, exportFile)
                ExportFormat.XML -> exportToXml(counters, exportFile)
                ExportFormat.TXT -> exportToTxt(counters, exportFile)
            }

            getUriForFile(exportFile)
        }
    }

    private fun createTempExportFile(
        counters: List<CounterExport>,
        format: ExportFormat
    ): File {
        val tempDir = File(context.cacheDir, "temp_exports").apply {
            if (!exists()) mkdirs()
        }

        val tempFile = File.createTempFile(
            "${FILE_PREFIX}_${System.currentTimeMillis()}",
            format.extension,
            tempDir
        )

        when (format) {
            ExportFormat.CSV -> exportToCsv(counters, tempFile)
            ExportFormat.JSON -> exportToJson(counters, tempFile)
            ExportFormat.XML -> exportToXml(counters, tempFile)
            ExportFormat.TXT -> exportToTxt(counters, tempFile)
        }

        return tempFile
    }

    private fun exportToJson(counters: List<CounterExport>, file: File) {
        file.bufferedWriter().use { writer ->
            gson.toJson(counters, writer)
        }
    }

    private fun exportToCsv(counters: List<CounterExport>, file: File) {
        file.bufferedWriter().use { writer ->
            // Write CSV header
            writer.write("ID,Name,Value,Category,Created At,Updated At,Can Increase,Can Decrease\n")

            // Write data rows
            counters.forEach { counter ->
                writer.write(
                    "${escapeCsv(counter.id)}," +
                            "${escapeCsv(counter.name)}," +
                            "${counter.value}," +
                            "${escapeCsv(counter.category ?: "")}," +
                            "${counter.createdAt}," +
                            "${counter.updatedAt}," +
                            "${counter.canIncrease}," +
                            "${counter.canDecrease}\n"
                )
            }
        }
    }

    private fun exportToXml(counters: List<CounterExport>, file: File) {
        file.bufferedWriter().use { writer ->
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<counters>\n")

            counters.forEach { counter ->
                writer.write("  <counter>\n")
                writer.write("    <id>${escapeXml(counter.id)}</id>\n")
                writer.write("    <name>${escapeXml(counter.name)}</name>\n")
                writer.write("    <value>${counter.value}</value>\n")
                counter.category?.let {
                    writer.write("    <category>${escapeXml(it)}</category>\n")
                }
                writer.write("    <createdAt>${counter.createdAt}</createdAt>\n")
                writer.write("    <updatedAt>${counter.updatedAt}</updatedAt>\n")
                writer.write("    <canIncrease>${counter.canIncrease}</canIncrease>\n")
                writer.write("    <canDecrease>${counter.canDecrease}</canDecrease>\n")
                writer.write("  </counter>\n")
            }

            writer.write("</counters>")
        }
    }

    private fun exportToTxt(counters: List<CounterExport>, file: File) {
        file.bufferedWriter().use { writer ->
            writer.write("Counters Export - ${formatDateForExport(Instant.now())}\n")
            writer.write("=".repeat(50) + "\n\n")

            counters.forEachIndexed { index, counter ->
                writer.write("Counter #${index + 1}:\n")
                writer.write("  ID: ${counter.id}\n")
                writer.write("  Name: ${counter.name}\n")
                writer.write("  Value: ${counter.value}\n")
                counter.category?.let {
                    writer.write("  Category: $it\n")
                }
                writer.write("  Created: ${counter.createdAt}\n")
                writer.write("  Updated: ${counter.updatedAt}\n")
                writer.write("  Can Increase: ${counter.canIncrease}\n")
                writer.write("  Can Decrease: ${counter.canDecrease}\n")
                writer.write("-".repeat(30) + "\n")
            }

            writer.write("\nTotal Counters: ${counters.size}")
        }
    }

    private fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
    }

    private fun generateFileName(format: ExportFormat): String {
        return try {
            // Try with java.time (works with desugaring)
            val timestamp = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            "counters_export_$timestamp${format.extension}"
        } catch (e: Exception) {
            // Fallback to SimpleDateFormat
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
                .format(Date())
            "counters_export_$timestamp${format.extension}"
        }
    }

    private fun formatDateForExport(date: Instant): String {
        return try {
            // Try with java.time
            val formatter = java.time.format.DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
            formatter.format(date)
        } catch (e: Exception) {
            // Fallback
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date.from(date))
        }
    }

    private fun escapeCsv(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }

    private fun escapeXml(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}

// Extension function to convert Counter to CounterExport
private fun Counter.toExportModel(): CounterExport {
    return CounterExport(
        id = id,
        name = name,
        value = currentCount,
        category = categoryId,
        createdAt = createdAt,
        updatedAt = lastUpdatedAt,
        canIncrease = canIncrease,
        canDecrease = canDecrease
    )
}