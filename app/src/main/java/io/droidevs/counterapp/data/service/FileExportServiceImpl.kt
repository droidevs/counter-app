package io.droidevs.counterapp.data.service

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.BuildConfig
import io.droidevs.counterapp.domain.model.Backup
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.domain.services.BackupExport
import io.droidevs.counterapp.domain.services.CategoryExport
import io.droidevs.counterapp.domain.services.CounterExport
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportSuccessResult
import io.droidevs.counterapp.domain.services.FileExportService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
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
        private const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"
        private const val FILE_PREFIX = "counters_export"
    }

    override suspend fun export(counters: List<Counter>, categories: List<Category>, format: ExportFormat): Result<ExportSuccessResult, FileError> {
        return withContext(Dispatchers.IO) {
            runCatchingFileResult {
                val backup = Backup(counters, categories)
                val backupExport = backup.toBackupExport()
                val tempFile = createTempExportFile(backupExport, format)
                val fileUri = getUriForFile(tempFile)
                ExportSuccessResult(fileUri, tempFile.name)
            }
        }
    }

    override fun getAvailableExportFormats(): List<ExportFormat> {
        return ExportFormat.entries.toList()
    }

    private fun createTempExportFile(
        backup: BackupExport,
        format: ExportFormat
    ): File {
        val tempDir = File(context.cacheDir, "temp_exports").apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Failed to create temp export directory")
            }
        }

        val fileName = generateFileName(format)
        val tempFile = File(tempDir, fileName)

        // Avoid partial/stale data if the same name is generated twice
        if (tempFile.exists()) {
            tempFile.delete()
        }

        when (format) {
            ExportFormat.CSV -> exportToCsv(backup, tempFile)
            ExportFormat.JSON -> exportToJson(backup, tempFile)
            ExportFormat.XML -> exportToXml(backup, tempFile)
            ExportFormat.TXT -> exportToTxt(backup, tempFile)
        }

        return tempFile
    }

    private fun exportToJson(backup: BackupExport, file: File) {
        file.bufferedWriter().use { writer ->
            gson.toJson(backup, writer)
        }
    }

    private fun exportToCsv(backup: BackupExport, file: File) {
        file.bufferedWriter().use { writer ->
            // Stable schema v2:
            // Type,ID,Name,Value,Category,Created At,Updated At,Order Anchor At,Can Increase,Can Decrease,Is System,Color
            writer.write("Type,ID,Name,Value,Category,Created At,Updated At,Order Anchor At,Can Increase,Can Decrease,Is System,Color\n")

            backup.counters.forEach { counter ->
                writer.write(
                    "Counter,${escapeCsv(counter.id)}," +
                            "${escapeCsv(counter.name)}," +
                            "${counter.value}," +
                            "${escapeCsv(counter.category ?: "")}," +
                            "${counter.createdAt}," +
                            "${counter.updatedAt ?: ""}," +
                            "${counter.orderAnchorAt ?: ""}," +
                            "${counter.canIncrease}," +
                            "${counter.canDecrease}," +
                            "${counter.isSystem}," +
                            ",\n" // Color column reserved for Category rows
                )
            }

            backup.categories.forEach { category ->
                writer.write(
                    "Category,${escapeCsv(category.id)}," +
                            "${escapeCsv(category.name)}," +
                            ",,," +
                            ",,," +
                            ",," +
                            "${category.isSystem}," +
                            "${escapeCsv(category.color)}\n"
                )
            }
        }
    }

    private fun exportToXml(backup: BackupExport, file: File) {
        file.bufferedWriter().use { writer ->
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<backup>\n")
            writer.write("  <counters>\n")
            backup.counters.forEach { counter ->
                writer.write("    <counter>\n")
                writer.write("      <id>${escapeXml(counter.id)}</id>\n")
                writer.write("      <name>${escapeXml(counter.name)}</name>\n")
                writer.write("      <value>${counter.value}</value>\n")
                writer.write("      <category>${escapeXml(counter.category ?: "")}</category>\n")
                writer.write("      <createdAt>${counter.createdAt}</createdAt>\n")
                writer.write("      <updatedAt>${counter.updatedAt ?: ""}</updatedAt>\n")
                writer.write("      <orderAnchorAt>${counter.orderAnchorAt ?: ""}</orderAnchorAt>\n")
                writer.write("      <canIncrease>${counter.canIncrease}</canIncrease>\n")
                writer.write("      <canDecrease>${counter.canDecrease}</canDecrease>\n")
                writer.write("      <isSystem>${counter.isSystem}</isSystem>\n")
                writer.write("    </counter>\n")
            }
            writer.write("  </counters>\n")
            writer.write("  <categories>\n")
            backup.categories.forEach { category ->
                writer.write("    <category>\n")
                writer.write("      <id>${escapeXml(category.id)}</id>\n")
                writer.write("      <name>${escapeXml(category.name)}</name>\n")
                writer.write("      <color>${escapeXml(category.color)}</color>\n")
                writer.write("      <isSystem>${category.isSystem}</isSystem>\n")
                writer.write("    </category>\n")
            }
            writer.write("  </categories>\n")
            writer.write("</backup>")
        }
    }

    private fun exportToTxt(backup: BackupExport, file: File) {
        file.bufferedWriter().use { writer ->
            writer.write("Backup - ${formatDateForExport(Instant.now())}\n")
            writer.write("=".repeat(50) + "\n\n")

            writer.write("Counters (${backup.counters.size}):\n")
            writer.write("-".repeat(50) + "\n")
            backup.counters.forEachIndexed { index, counter ->
                writer.write("Counter #${index + 1}:\n")
                writer.write("  ID: ${counter.id}\n")
                writer.write("  Name: ${counter.name}\n")
                writer.write("  Value: ${counter.value}\n")
                writer.write("  Is System: ${counter.isSystem}\n")
                counter.category?.let {
                    writer.write("  Category ID: $it\n")
                }
                writer.write("  Created: ${counter.createdAt}\n")
                writer.write("  Updated: ${counter.updatedAt}\n")
                writer.write("  Order Anchor: ${counter.orderAnchorAt}\n")
                writer.write("-".repeat(30) + "\n")
            }

            writer.write("\nCategories (${backup.categories.size}):\n")
            writer.write("-".repeat(50) + "\n")
            backup.categories.forEachIndexed { index, category ->
                writer.write("Category #${index + 1}:\n")
                writer.write("  ID: ${category.id}\n")
                writer.write("  Name: ${category.name}\n")
                writer.write("  Color: ${category.color}\n")
                writer.write("  Is System: ${category.isSystem}\n")
                writer.write("-".repeat(30) + "\n")
            }
        }
    }

    private fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
    }

    private fun generateFileName(format: ExportFormat): String {
        val timestamp = try {
            java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
        } catch (_: Exception) {
            SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
        }
        return "${FILE_PREFIX}_$timestamp${format.extension}"
    }

    private fun formatDateForExport(date: Instant): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
            formatter.format(date)
        } catch (_: Exception) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date.from(date))
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

    private fun Backup.toBackupExport(): BackupExport {
        val countersExport = counters.map { it.toCounterExport() }
        val categoriesExport = categories.map { it.toCategoryExport() }
        return BackupExport(countersExport, categoriesExport)
    }

    private fun Counter.toCounterExport(): CounterExport {
        return CounterExport(
            id = id,
            name = name,
            value = currentCount,
            category = categoryId,
            createdAt = createdAt,
            updatedAt = lastUpdatedAt,
            orderAnchorAt = orderAnchorAt,
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            isSystem = isSystem
        )
    }

    private fun Category.toCategoryExport(): CategoryExport {
        return CategoryExport(
            id = id,
            name = name,
            color = color.colorInt.toString(),
            isSystem = isSystem
        )
    }

}
