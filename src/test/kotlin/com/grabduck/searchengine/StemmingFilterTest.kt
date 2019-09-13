package com.grabduck.searchengine

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class StemmingFilterTest {
    @Test
    fun suffixRule() {
        val underTest = StemmingFilter.SuffixRule("ed")
        assertThat(underTest.stemming("allowed")).isEqualTo("allow")
        assertThat(underTest.stemming("other")).isNull()
    }
}