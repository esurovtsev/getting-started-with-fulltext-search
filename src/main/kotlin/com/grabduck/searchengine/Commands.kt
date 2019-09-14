package com.grabduck.searchengine

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Commands(
        private val config: Configuration,
        private val documentService: DocumentService,
        private val searchService: SearchService,
        private val directIndexer: DirectIndexer,
        private val invertedIndexer: InvertedIndexer
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

    @ShellMethod("Generates direct Index")
    fun createDirectIndex(): String {
        directIndexer.createIndex()
        return "direct index created"
    }

    @ShellMethod("Generates better direct Index")
    fun createBetterDirectIndex(): String {
        directIndexer.createBetterIndex()
        return "better direct index created"
    }

    @ShellMethod("Generates Inverted Index")
    fun createInvertedIndex(): String {
        invertedIndexer.createIndex()
        return "inverted index created"
    }

    @ShellMethod("Show direct index content by document ID")
    fun findDirectIndexById(documentId: String): List<String> =
            directIndexer.findById(documentId)


    @ShellMethod("Performs a search on Direct Index")
    fun findUsingDirectIndex(request: String): List<String> =
            searchService.findUsingDirectIndex(request)

    @ShellMethod("Performs a search on Direct Index with better analyzing")
    fun findUsingBetterDirectIndex(request: String): List<String> =
            searchService.findUsingBetterDirectIndex(request)

    @ShellMethod("Performs a search on Inverted Index")
    fun findUsingInvertedIndex(request: String): List<String> =
            searchService.findUsingInvertedIndex(request)
}