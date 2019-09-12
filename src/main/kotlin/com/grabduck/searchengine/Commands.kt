package com.grabduck.searchengine

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Commands(
        private val config: Configuration,
        private val documentService: DocumentService,
        private val searchService: SearchService
) {
    @ShellMethod("Show current configuration")
    fun config(): List<String> =
            config.getAllProperties().entries.map { "${it.key}: ${it.value}" }

    @ShellMethod("List all available document ids")
    fun findAllIds(): List<String> =
            documentService.findAllIds()

    @ShellMethod("Show document content by document ID")
    fun findById(documentId: String): String =
            documentService.findById(documentId) ?: "document not found"

    @ShellMethod("Performs a brute force search across all documents using simple comparison")
    fun findUsingBruteForceSimple(request: String): List<String> =
            searchService.findUsingBruteForce_simple(request)

    @ShellMethod("Performs a brute force search across all documents using tokenization for search request")
    fun findUsingBruteForceTokenize(request: String): List<String> =
            searchService.findUsingBruteForce_tokenize(request)
}