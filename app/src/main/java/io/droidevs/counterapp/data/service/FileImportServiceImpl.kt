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
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.FileImportService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Instant
import javax.inject.Inject

class FileImportServiceImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : FileImportService {

    override fun getAvailableImportFormats(): List<ExportFormat> = listOf(
        ExportFormat.JSON,
        ExportFormat.CSV,
        ExportFormat.XML
    )

    override suspend fun import(fileUri: Uri): Result<Backup, FileError> {
        return runCatchingFileResult {
            val text = readText(fileUri)
            val format = detectFormat(fileUri, text)

            val backupExport = when (format) {
                ExportFormat.JSON -> parseJson(text)
                ExportFormat.CSV -> parseCsv(text)
                ExportFormat.XML -> parseXml(text)
                ExportFormat.TXT -> throw IllegalArgumentException("TXT import is not supported. Please import JSON/CSV/XML.")
            }

            backupExport.toBackup().sanitize()
        }
    }

    private fun readText(fileUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(fileUri)
            ?: throw IllegalArgumentException("Failed to open file")
        return inputStream.use { stream ->
            BufferedReader(InputStreamReader(stream)).use { it.readText() }
        }
    }

    private fun detectFormat(fileUri: Uri, content: String): ExportFormat {
        val name = runCatching {
            context.contentResolver.query(fileUri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0 && cursor.moveToFirst()) cursor.getString(idx) else null
                }
        }.getOrNull()

        val lowerName = name?.lowercase().orEmpty()
        return when {
            lowerName.endsWith(ExportFormat.JSON.extension) -> ExportFormat.JSON
            lowerName.endsWith(ExportFormat.CSV.extension) -> ExportFormat.CSV
            lowerName.endsWith(ExportFormat.XML.extension) -> ExportFormat.XML
            lowerName.endsWith(ExportFormat.TXT.extension) -> ExportFormat.TXT
            // Fallback to content sniffing
            content.trimStart().startsWith("{") -> ExportFormat.JSON
            content.trimStart().startsWith("<") -> ExportFormat.XML
            content.lines().firstOrNull()?.startsWith("Type,", ignoreCase = true) == true -> ExportFormat.CSV
            else -> ExportFormat.JSON // default for backward compatibility
        }
    }

    private fun parseJson(text: String): BackupExport {
        return gson.fromJson(text, BackupExport::class.java)
            ?: throw IllegalArgumentException("Invalid JSON")
    }

    private fun parseCsv(text: String): BackupExport {
        val lines = text.lineSequence().filter { it.isNotBlank() }.toList()
        if (lines.isEmpty()) throw IllegalArgumentException("Empty CSV")

        val header = lines.firstOrNull().orEmpty()
        val expectedHeader = "Type,ID,Name,Value,Category,Created At,Updated At,Order Anchor At,Can Increase,Can Decrease,Is System,Color"
        val hasV2Header = header.trim() == expectedHeader
        if (!hasV2Header) {
            throw IllegalArgumentException("Unsupported CSV schema. Please export again with the latest app version.")
        }

        val dataLines = lines.drop(1)

        val counters = mutableListOf<CounterExport>()
        val categories = mutableListOf<CategoryExport>()

        dataLines.forEach { line ->
            val cols = splitCsvLine(line)
            if (cols.isEmpty()) return@forEach

            val type = cols.getOrNull(0).orEmpty()
            if (type.equals("Counter", ignoreCase = true)) {
                val id = cols.getOrNull(1).orEmpty()
                val name = cols.getOrNull(2).orEmpty()
                val value = cols.getOrNull(3)?.toIntOrNull() ?: 0
                val category = cols.getOrNull(4)?.trim().orEmpty().ifBlank { null }

                val createdAt = cols.getOrNull(5)?.let { runCatching { Instant.parse(it) }.getOrNull() } ?: Instant.now()
                val updatedAt = cols.getOrNull(6)?.let { runCatching { Instant.parse(it) }.getOrNull() }
                val orderAnchorAt = cols.getOrNull(7)?.let { runCatching { Instant.parse(it) }.getOrNull() }

                val canIncrease = cols.getOrNull(8)?.toBooleanStrictOrNull() ?: true
                val canDecrease = cols.getOrNull(9)?.toBooleanStrictOrNull() ?: false
                val isSystem = cols.getOrNull(10)?.toBooleanStrictOrNull() ?: false

                counters += CounterExport(
                    id = id,
                    name = name,
                    value = value,
                    category = category,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    orderAnchorAt = orderAnchorAt,
                    canIncrease = canIncrease,
                    canDecrease = canDecrease,
                    isSystem = isSystem
                )
            } else if (type.equals("Category", ignoreCase = true)) {
                val id = cols.getOrNull(1).orEmpty()
                val name = cols.getOrNull(2).orEmpty()
                val isSystem = cols.getOrNull(10)?.toBooleanStrictOrNull() ?: false
                val color = cols.getOrNull(11).orEmpty()

                categories += CategoryExport(id = id, name = name, color = color, isSystem = isSystem)
            }
        }

        return BackupExport(counters = counters, categories = categories)
    }

    private fun parseXml(text: String): BackupExport {
        // Parse categories
        val categoryBlocks = Regex("<category>.*?</category>", setOf(RegexOption.DOT_MATCHES_ALL))
            .findAll(text)
            .map { it.value }
            .toList()
        val categories = categoryBlocks.mapNotNull { block ->
            val id = Regex("<id>(.*?)</id>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?: return@mapNotNull null
            val name = Regex("<name>(.*?)</name>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim().orEmpty()
            val color = Regex("<color>(.*?)</color>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim().orEmpty()
            val isSystem = Regex("<isSystem>(.*?)</isSystem>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()?.toBooleanStrictOrNull() ?: false
            CategoryExport(id = id, name = name, color = color, isSystem = isSystem)
        }

        // Parse counters
        val counterBlocks = Regex("<counter>.*?</counter>", setOf(RegexOption.DOT_MATCHES_ALL))
            .findAll(text)
            .map { it.value }
            .toList()
        val counters = counterBlocks.mapNotNull { block ->
            val id = Regex("<id>(.*?)</id>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?: return@mapNotNull null
            val name = Regex("<name>(.*?)</name>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim().orEmpty()
            val value = Regex("<value>(.*?)</value>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()?.toIntOrNull() ?: 0
            val category = Regex("<category>(.*?)</category>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?.ifBlank { null }
            val createdAt = Regex("<createdAt>(.*?)</createdAt>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?.let { runCatching { Instant.parse(it) }.getOrNull() } ?: Instant.now()
            val updatedAt = Regex("<updatedAt>(.*?)</updatedAt>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?.let { runCatching { Instant.parse(it) }.getOrNull() }
            val orderAnchorAt = Regex("<orderAnchorAt>(.*?)</orderAnchorAt>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()
                ?.let { runCatching { Instant.parse(it) }.getOrNull() }
            val canIncrease = Regex("<canIncrease>(.*?)</canIncrease>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()?.toBooleanStrictOrNull() ?: true
            val canDecrease = Regex("<canDecrease>(.*?)</canDecrease>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()?.toBooleanStrictOrNull() ?: false
            val isSystem = Regex("<isSystem>(.*?)</isSystem>", setOf(RegexOption.DOT_MATCHES_ALL))
                .find(block)?.groupValues?.get(1)?.trim()?.toBooleanStrictOrNull() ?: false

            CounterExport(
                id = id,
                name = name,
                value = value,
                category = category,
                createdAt = createdAt,
                updatedAt = updatedAt,
                orderAnchorAt = orderAnchorAt,
                canIncrease = canIncrease,
                canDecrease = canDecrease,
                isSystem = isSystem
            )
        }

        return BackupExport(counters = counters, categories = categories)
    }

    private fun splitCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        sb.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                c == ',' && !inQuotes -> {
                    result += sb.toString()
                    sb.setLength(0)
                }
                else -> sb.append(c)
            }
            i++
        }
        result += sb.toString()
        return result
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
            isSystem = isSystem,
            createdAt = createdAt,
            lastUpdatedAt = updatedAt,
            orderAnchorAt = orderAnchorAt,
            canIncrease = canIncrease,
            canDecrease = canDecrease
        )
    }

    private fun CategoryExport.toCategory(): Category {
        val colorInt = color.trim().toIntOrNull() ?: CategoryColor.default().colorInt
        return Category(
            id = id,
            name = name,
            color = CategoryColor.of(colorInt),
            countersCount = 0,
            isSystem = isSystem
        )
    }

    private fun Backup.sanitize(): Backup {
        // Drop invalid rows & dedupe by id
        val dedupedCategories = categories
            .asSequence()
            .filter { it.id.isNotBlank() && it.name.isNotBlank() }
            .distinctBy { it.id }
            .toList()

        val validCategoryIds = dedupedCategories.mapTo(mutableSetOf()) { it.id }

        val dedupedCounters = counters
            .asSequence()
            .filter { it.id.isNotBlank() && it.name.isNotBlank() }
            .map { counter ->
                val normalizedCategory = counter.categoryId?.trim().orEmpty().ifBlank { null }
                // If category doesn't exist in imported categories, drop relation
                counter.apply {
                    categoryId = normalizedCategory?.takeIf { validCategoryIds.contains(it) }
                }
            }
            .distinctBy { it.id }
            .toList()

        return Backup(
            counters = dedupedCounters,
            categories = dedupedCategories
        )
    }
}
