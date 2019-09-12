package com.grabduck.searchengine

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class BetterTokenizerTest {
    @Test
    fun tokenize01() {
        assertThat(BetterTokenizer.tokenize("‘Take"))
            .containsExactlyInAnyOrder("Take")
    }

    @Test
    fun tokenize02() {
        assertThat(BetterTokenizer.tokenize("tight!’"))
            .containsExactlyInAnyOrder("tight")
    }

    @Test
    fun tokenize03() {
        assertThat(BetterTokenizer.tokenize("‘Don’t"))
            .containsExactlyInAnyOrder("Don")
    }

    @Test
    fun tokenize04() {
        assertThat(BetterTokenizer.tokenize("right!’"))
            .containsExactlyInAnyOrder("right")
    }

    @Test
    fun tokenize05() {
        assertThat(BetterTokenizer.tokenize("Darning-needle."))
            .containsExactlyInAnyOrder("Darning", "needle")
    }

    @Test
    fun tokenize06() {
        assertThat(BetterTokenizer.tokenize("Fingers;"))
            .containsExactlyInAnyOrder("Fingers")
    }

    @Test
    fun tokenize07() {
        assertThat(BetterTokenizer.tokenize("breast-pin!’"))
            .containsExactlyInAnyOrder("breast", "pin")
    }

    @Test
    fun tokenize08() {
        assertThat(BetterTokenizer.tokenize("sealing-wax.’"))
            .containsExactlyInAnyOrder("sealing", "wax")
    }

    @Test
    fun tokenize09() {
        assertThat(BetterTokenizer.tokenize("sha’n’t"))
            .containsExactlyInAnyOrder("sha")
    }

    @Test
    fun tokenize10() {
        assertThat(BetterTokenizer.tokenize("her--shavings,"))
            .containsExactlyInAnyOrder("her", "shavings")
    }

    @Test
    fun tokenize11() {
        assertThat(BetterTokenizer.tokenize("‘’Fingers.’’"))
            .containsExactlyInAnyOrder("Fingers")
    }

    @Test
    fun tokenize12() {
        assertThat(BetterTokenizer.tokenize("Dip-into-everything,"))
            .containsExactlyInAnyOrder("Dip", "into", "everything")
    }

    @Test
    fun tokenize13() {
        assertThat(BetterTokenizer.tokenize("can’t--it"))
            .containsExactlyInAnyOrder("can", "it")
    }

    @Test
    fun tokenize14() {
        assertThat(BetterTokenizer.tokenize("egg-shell."))
            .containsExactlyInAnyOrder("egg", "shell")
    }

    @Test
    fun tokenize15() {
        assertThat(BetterTokenizer.tokenize("lie."))
            .containsExactlyInAnyOrder("lie")
    }
}