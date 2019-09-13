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
    fun tokenize01() {
        assertThat(underTest.tokenize("‘Take"))
            .containsExactlyInAnyOrder("Take")
    }

    @Test
    fun tokenize02() {
        assertThat(underTest.tokenize("tight!’"))
            .containsExactlyInAnyOrder("tight")
    }

    @Test
    fun tokenize03() {
        assertThat(underTest.tokenize("‘Don’t"))
            .containsExactlyInAnyOrder("Don")
    }

    @Test
    fun tokenize04() {
        assertThat(underTest.tokenize("right!’"))
            .containsExactlyInAnyOrder("right")
    }

    @Test
    fun tokenize05() {
        assertThat(underTest.tokenize("Darning-needle."))
            .containsExactlyInAnyOrder("Darning", "needle")
    }

    @Test
    fun tokenize06() {
        assertThat(underTest.tokenize("Fingers;"))
            .containsExactlyInAnyOrder("Fingers")
    }

    @Test
    fun tokenize07() {
        assertThat(underTest.tokenize("breast-pin!’"))
            .containsExactlyInAnyOrder("breast", "pin")
    }

    @Test
    fun tokenize08() {
        assertThat(underTest.tokenize("sealing-wax.’"))
            .containsExactlyInAnyOrder("sealing", "wax")
    }

    @Test
    fun tokenize09() {
        assertThat(underTest.tokenize("sha’n’t"))
            .containsExactlyInAnyOrder("sha")
    }

    @Test
    fun tokenize10() {
        assertThat(underTest.tokenize("her--shavings,"))
            .containsExactlyInAnyOrder("her", "shavings")
    }

    @Test
    fun tokenize11() {
        assertThat(underTest.tokenize("‘’Fingers.’’"))
            .containsExactlyInAnyOrder("Fingers")
    }

    @Test
    fun tokenize12() {
        assertThat(underTest.tokenize("Dip-into-everything,"))
            .containsExactlyInAnyOrder("Dip", "into", "everything")
    }

    @Test
    fun tokenize13() {
        assertThat(underTest.tokenize("can’t--it"))
            .containsExactlyInAnyOrder("can", "it")
    }

    @Test
    fun tokenize14() {
        assertThat(underTest.tokenize("egg-shell."))
            .containsExactlyInAnyOrder("egg", "shell")
    }

    @Test
    fun tokenize15() {
        assertThat(underTest.tokenize("lie."))
            .containsExactlyInAnyOrder("lie")
    }
}