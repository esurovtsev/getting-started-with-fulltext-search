package com.grabduck.searchengine

import org.springframework.stereotype.Component

@Component
class TokenAnalyzer {
    fun tokenize(input: String): List<String> =
            WhitespaceTokenizer.tokenize(input)

    fun analyze(input: String): List<String> =
            BetterTokenizer
                .tokenize(input)
                .flatMap { LowerCaseFilter.filter(it) }
                .toSortedSet()
                .toList()

}

object WhitespaceTokenizer {
    private val whitespace = Regex("\\s+")

    fun tokenize(input: String): List<String> =
            input.split(whitespace).toSet().toList()
}

object BetterTokenizer {
    private val nonAlpha = Regex("[^a-zA-Z]")

    fun tokenize(input: String): List<String> =
            WhitespaceTokenizer
                .tokenize(nonAlpha.replace(input, " "))
                .filter { it.length > 1 }
}

object LowerCaseFilter {
    fun filter(input: String): List<String> =
            listOf(input.toLowerCase())
}