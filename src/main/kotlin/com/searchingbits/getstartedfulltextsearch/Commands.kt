package com.searchingbits.getstartedfulltextsearch

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

enum class SearchType {
    BRUTE_FORCE, BRUTE_FORCE_TOKEN, DIRECT_INDEX, BETTER_DIRECT_INDEX, INVERTED_INDEX, GREEDY_INVERTED_INDEX
}

enum class IndexType {
    DIRECT, BETTER_DIRECT, INVERTED, INVERTED_DUPLICATED
}

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

    @ShellMethod("Lists documents or document content")
    fun document(
            @ShellOption(help = "Document ID or 'all' for getting list of all documents")
            id: String
    ): List<String> =
            if (id == "all") {
                documentService.findAllIds()
            } else {
                listOf(documentService.findById(id) ?: "document not found")
            }

    @ShellMethod("Performs a search request on the documents")
    fun search(
            @ShellOption(help = "Defines a search type") type: SearchType,
            @ShellOption(help = "Defines a search query") query: String
    ): List<String> =
            when (type) {
                SearchType.BRUTE_FORCE -> addHeader(searchService.findUsingBruteForce_simple(query))
                SearchType.BRUTE_FORCE_TOKEN -> addHeader(searchService.findUsingBruteForce_tokenize(query))
                SearchType.DIRECT_INDEX -> addHeader(searchService.findUsingDirectIndex(query))
                SearchType.BETTER_DIRECT_INDEX -> addHeader(searchService.findUsingBetterDirectIndex(query))
                SearchType.INVERTED_INDEX -> addHeader(searchService.findUsingInvertedIndex(query))
                SearchType.GREEDY_INVERTED_INDEX -> addHeader(searchService.findUsingGreedyInvertedIndex(query))
            }

    @ShellMethod("Generates a search Index")
    fun createIndex(
        @ShellOption(help = "Defines an index type") type: IndexType
    ): String {
        when (type) {
            IndexType.DIRECT -> directIndexer.createIndex()
            IndexType.BETTER_DIRECT -> directIndexer.createBetterIndex()
            IndexType.INVERTED -> invertedIndexer.createIndex()
            IndexType.INVERTED_DUPLICATED -> invertedIndexer.createIndexWithDuplicates()
        }
        return "Index created: $type"
    }

    private fun addHeader(result: Collection<String>): List<String> =
            listOf("Results found: ${result.size}")
                .plus("---------------------------------------")
                .plus(result)
                .plus("---------------------------------------")
}