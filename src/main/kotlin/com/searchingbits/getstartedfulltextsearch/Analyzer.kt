package com.searchingbits.getstartedfulltextsearch

import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class TokenAnalyzer(
        private val whitespaceTokenizer: WhitespaceTokenizer,
        private val betterTokenizer: BetterTokenizer,
        private val lowerCaseFilter: LowerCaseFilter,
        private val stopwordsFilter: StopwordsFilter,
        private val stemmingFilter: StemmingFilter,
        private val synonymsFilter: SynonymsFilter
) {
    fun analyze_whitespaceTokenizing(input: String): Collection<String> =
            whitespaceTokenizer
                .tokenize(input)
                .toSet()

    fun analyze_betterTokenizing(input: String): Collection<String> =
            betterTokenizer
                .tokenize(input)
                .toSet()
                .flatMap { lowerCaseFilter.filter(it) }
                .flatMap { stopwordsFilter.filter(it) }
                .flatMap { stemmingFilter.filter(it) }
                .flatMap { synonymsFilter.filter(it) }
                .toSortedSet()

    fun analyze_betterTokenizingWithDuplicates(input: String): Collection<String> =
            betterTokenizer
                .tokenize(input)
                .flatMap { lowerCaseFilter.filter(it) }
                .flatMap { stopwordsFilter.filter(it) }
                .flatMap { stemmingFilter.filter(it) }
                .flatMap { synonymsFilter.filter(it) }
                .sorted()
}

@Component
class WhitespaceTokenizer {
    private val whitespace = Regex("\\s+")

    fun tokenize(input: String): Collection<String> =
            input.split(whitespace)
}

@Component
class BetterTokenizer(private val whitespaceTokenizer: WhitespaceTokenizer) {
    private val nonAlpha = Regex("[^a-zA-Z]")

    fun tokenize(input: String): Collection<String> =
            whitespaceTokenizer
                .tokenize(nonAlpha.replace(input, " "))
                .filter { it.length > 1 }
}

interface Filter {
    fun filter(input: String): Collection<String>
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

    override fun filter(input: String): Collection<String> =
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
            SuffixRule("e"),
            SuffixRule("es")
    )

    override fun filter(input: String): Collection<String> =
            listOf(rules
                .mapNotNull { it.stemming(input) }
                .fold(input) { result, element -> if (result.length < element.length) result else element })

}

@Component
class SynonymsFilter : Filter {
    val synonyms = mapOf(
            "mountain" to listOf("hill", "bluff", "cliff", "elevation", "peak", "pile", "ridge", "sierra", "volcano")
    )

    override fun filter(input: String): Collection<String> =
            synonyms[input]?.plus(input) ?: listOf(input)
}