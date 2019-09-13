package com.grabduck.searchengine

import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class TokenAnalyzer(
        private val whitespaceTokenizer: WhitespaceTokenizer,
        private val betterTokenizer: BetterTokenizer,
        private val lowerCaseFilter: LowerCaseFilter,
        private val stopwordsFilter: StopwordsFilter,
        private val stemmingFilter: StemmingFilter
) {
    fun tokenize(input: String): List<String> =
            whitespaceTokenizer.tokenize(input)

    fun analyze(input: String): List<String> =
            betterTokenizer
                .tokenize(input)
                .flatMap { lowerCaseFilter.filter(it) }
                .flatMap { stopwordsFilter.filter(it) }
                .flatMap { stemmingFilter.filter(it) }
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

@Component
class StemmingFilter : Filter {
    class SuffixRule(private val suffix: String) {
        fun stemming(input: String): String? =
                if (input.endsWith(suffix)) input.substring(0, input.length - suffix.length).ifEmpty { null } else null
    }

    private val rules: List<SuffixRule> = listOf(
            SuffixRule("ed"),
            SuffixRule("s"),
            SuffixRule("ing"),
            SuffixRule("e"),
            SuffixRule("ly"),
            SuffixRule("ty"),
            SuffixRule("ability"),
            SuffixRule("ness"),
            SuffixRule("er"),
            SuffixRule("e")
    )

    override fun filter(input: String): List<String> =
            listOf(rules
                .mapNotNull { it.stemming(input) }
                .fold(input) { result, element -> if (result.length < element.length) result else element })

}