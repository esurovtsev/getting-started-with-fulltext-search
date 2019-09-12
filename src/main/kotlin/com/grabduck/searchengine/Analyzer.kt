package com.grabduck.searchengine

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class TokenAnalyzer {
    fun tokenize(input: String): List<String> =
            WhitespaceTokenizer.tokenize(input)
}

object WhitespaceTokenizer {
    private val whitespace = Pattern.compile("\\s+")

    fun tokenize(input: String): List<String> =
            input.split(whitespace).toSet().toList()
}