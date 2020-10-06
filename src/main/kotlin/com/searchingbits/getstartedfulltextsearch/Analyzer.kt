package com.searchingbits.getstartedfulltextsearch

import org.springframework.stereotype.Component

@Component
class TokenAnalyzer(private val whitespaceTokenizer: WhitespaceTokenizer) {
    fun analyze_whitespaceTokenizing(input: String): Collection<String> =
        whitespaceTokenizer
            .tokenize(input)
            .toSet()
}

@Component
class WhitespaceTokenizer {
    private val whitespace = Regex("\\s+")

    fun tokenize(input: String): Collection<String> =
            input.split(whitespace)
}
