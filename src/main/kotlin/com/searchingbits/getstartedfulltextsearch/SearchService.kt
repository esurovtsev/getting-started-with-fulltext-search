package com.searchingbits.getstartedfulltextsearch

import org.springframework.stereotype.Service

fun String.containsAllTerms(terms: Collection<String>): Boolean =
        terms.map { contains(it) }.fold(true) { result, element -> result && element }

@Service
class SearchService(
        private val documentService: DocumentService,
        private val directIndexer: DirectIndexer,
        private val analyzer: TokenAnalyzer,
        private val invertedIndexer: InvertedIndexer
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

    fun findUsingBetterDirectIndex(request: String): List<String> {
        val terms = analyzer.analyze_betterTokenizing(request)
        return documentService
            .findAllIds()
            .filter { docId -> directIndexer.findById(docId)?.let { docTokens -> docTokens.containsAll(terms) } }
    }

    fun findUsingInvertedIndex(request: String): Collection<String> =
            analyzer
                .analyze_betterTokenizing(request)
                .map { invertedIndexer.getPostingListByToken(it).toSet() }
                .reduce { a, b -> a.intersect(b) }

    fun findUsingGreedyInvertedIndex(request: String): Collection<String> =
            analyzer
                .analyze_betterTokenizing(request)
                .map { invertedIndexer.getPostingListByToken(it).toSet() }
                .reduce { a, b -> a.plus(b) }

    fun findWithScoring(request: String): Collection<String> =
            analyzer
                .analyze_betterTokenizing(request)
                .asSequence()
                .map { invertedIndexer.getPostingListByToken(it) }
                .reduce { a, b -> a.plus(b) }
                .asSequence()
                .groupBy { it }
                .map { it.key to it.value.size }
                .sortedByDescending { it.second }
                .map { it.first }
                .toList()
}