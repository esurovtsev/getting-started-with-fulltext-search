package com.searchingbits.getstartedfulltextsearch

import org.springframework.stereotype.Component
import java.io.File

@Component
class DirectIndexer(
        private val analyzer: TokenAnalyzer,
        private val documentService: DocumentService,
        private val config: Configuration
) {
    fun createIndex() {
        // delete old index
        File(config.getDirectIndexDir()).listFiles()?.forEach { it.delete() }

        // generate new index
        documentService.findAllIds().forEach { docId ->
            documentService.findById(docId)?.let {
                File("${config.getDirectIndexDir()}/$docId").writeText(analyzer.analyze_whitespaceTokenizing(it).joinToString("\n"))
            }
        }
    }

    fun createBetterIndex() {
        // delete old index
        File(config.getDirectIndexDir()).listFiles()?.forEach { it.delete() }

        // generate new index
        documentService.findAllIds().forEach { docId ->
            documentService.findById(docId)?.let {
                File("${config.getDirectIndexDir()}/$docId").writeText(analyzer.analyze_betterTokenizing(it).joinToString("\n"))
            }
        }
    }

    fun findAllIds(): List<String> =
            File(config.getDirectIndexDir()).listFiles()?.map { it.name } ?: listOf()

    fun findById(documentId: String): List<String> =
            this
                .findAllIds()
                .findLast { it == documentId }
                ?.let { File("${config.getDirectIndexDir()}/$it") }
                ?.takeIf { it.exists() && it.isFile && it.canRead() }
                ?.readLines()
            ?: listOf()
}