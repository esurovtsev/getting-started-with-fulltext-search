package com.grabduck.searchengine

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class BetterTokenizerTest {
    private lateinit var underTest: BetterTokenizer

    @Before
    fun init() {
        underTest = BetterTokenizer(WhitespaceTokenizer())
    }

    @Test
    fun tokenize() {
        listOf(
                "‘Take" to arrayOf("Take"),
                "tight!’" to arrayOf("tight"),
                "‘Don’t" to arrayOf("Don"),
                "right!’" to arrayOf("right"),
                "Darning-needle." to arrayOf("Darning", "needle"),
                "Fingers;" to arrayOf("Fingers"),
                "breast-pin!’" to arrayOf("breast", "pin"),
                "sealing-wax.’" to arrayOf("sealing", "wax"),
                "sha’n’t" to arrayOf("sha"),
                "her--shavings," to arrayOf("her", "shavings"),
                "‘’Fingers.’’" to arrayOf("Fingers"),
                "Dip-into-everything," to arrayOf("Dip", "into", "everything"),
                "can’t--it" to arrayOf("can", "it"),
                "egg-shell." to arrayOf("egg", "shell"),
                "lie." to arrayOf("lie")

        ).forEach {
            assertThat(underTest.tokenize(it.first)).containsExactlyInAnyOrder(*it.second)
        }
    }
}