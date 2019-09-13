package com.grabduck.searchengine

import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class TokenAnalyzer(
        private val whitespaceTokenizer: WhitespaceTokenizer,
        private val betterTokenizer: BetterTokenizer,
        private val lowerCaseFilter: LowerCaseFilter,
        private val stopwordsFilter: StopwordsFilter
) {
    fun tokenize(input: String): List<String> =
            whitespaceTokenizer.tokenize(input)

    fun analyze(input: String): List<String> =
            betterTokenizer
                .tokenize(input)
                .flatMap { lowerCaseFilter.filter(it) }
                .flatMap { stopwordsFilter.filter(it) }
                .toSortedSet()
                .toList()

}

@Component
class WhitespaceTokenizer {
    private val whitespace = Regex("\\s+")

    fun tokenize(input: String): List<String> =
            input.split(whitespace).toSet().toList()
}

@Component
class BetterTokenizer(private val whitespaceTokenizer: WhitespaceTokenizer) {
    private val nonAlpha = Regex("[^a-zA-Z]")

    fun tokenize(input: String): List<String> =
            whitespaceTokenizer
                .tokenize(nonAlpha.replace(input, " "))
                .filter { it.length > 1 }
}

interface Filter {
    fun filter(input: String): List<String>
}

@Component
class LowerCaseFilter : Filter {
    override fun filter(input: String): List<String> =
            listOf(input.toLowerCase())
}

@Component
class StopwordsFilter(val config: Configuration) : Filter {
    private lateinit var stopWords: Set<String>

    @PostConstruct
    fun init() {
        stopWords = File(config.getStopWordsFile()).readLines().toSet()
    }

    override fun filter(input: String): List<String> =
            if (stopWords.contains(input)) listOf() else listOf(input)
}