package io.droidevs.counterapp.domain.services


enum class ExportFormat(val extension: String, val mimeType: String) {
    CSV(".csv", "text/csv"),
    JSON(".json", "application/json"),
    XML(".xml", "application/xml"),
    TXT(".txt", "text/plain")
}