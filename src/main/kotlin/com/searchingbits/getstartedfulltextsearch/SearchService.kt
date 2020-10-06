package com.searchingbits.getstartedfulltextsearch

import org.springframework.stereotype.Service

fun String.containsAllTerms(terms: Collection<String>): Boolean =
        terms.map { contains(it) }.fold(true) { result, element -> result && element }

@Service
class SearchService(
    private val documentService: DocumentService,
    private val directIndexer: DirectIndexer,
    private val analyzer: TokenAnalyzer
) {
    fun findUsingBruteForce_simple(request: String): List<String> =
            documentService
                .findAllIds()
                .filter { docId -> documentService.findById(docId)?.let { docContent -> docContent.contains(request) } ?: false }

    fun findUsingBruteForce_tokenize(request: String): List<String> {
        val terms = analyzer.analyze_whitespaceTokenizing(request)
        return documentService
            .findAllIds()
            .filter { docId -> documentService.findById(docId)?.let { it.containsAllTerms(terms) } ?: false }
    }

    fun findUsingDirectIndex(request: String): List<String> {
        val terms = analyzer.analyze_whitespaceTokenizing(request)
        return documentService
            .findAllIds()
            .filter { docId -> directIndexer.findById(docId)?.let { docTokens -> docTokens.containsAll(terms) } }
    }
}