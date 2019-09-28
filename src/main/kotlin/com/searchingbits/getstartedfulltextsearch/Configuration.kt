package com.searchingbits.getstartedfulltextsearch

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

const val DATA_DIR_KEY = "com.grabduck.searchengine.dataDir"
const val DIRECT_INDEX_DIR_KEY = "com.grabduck.searchengine.directIndexDir"
const val STOP_WORDS_FILE_KEY = "com.grabduck.searchengine.stopWords"
const val INVERTED_INDEX_FILE_KEY = "com.grabduck.searchengine.invertedIndexFile"

@Component
class Configuration {
    @Value("\${$DATA_DIR_KEY}")
    private lateinit var dataDir: String

    @Value("\${$DIRECT_INDEX_DIR_KEY}")
    private lateinit var directIndexDir: String

    @Value("\${$STOP_WORDS_FILE_KEY}")
    private lateinit var stopwordsFile: String

    @Value("\${$INVERTED_INDEX_FILE_KEY}")
    private lateinit var invertedIndexFile: String

    @PostConstruct
    fun init() {
        listOf(getDirectIndexDir())
            .forEach {
                val dir = File(it)
                if (!dir.exists() || !dir.isDirectory) {
                    dir.mkdirs()
                }
            }
    }

    fun getAllProperties(): Map<String, String> =
            mapOf(
                    DATA_DIR_KEY to File(dataDir).absolutePath,
                    DIRECT_INDEX_DIR_KEY to File(directIndexDir).absolutePath,
                    STOP_WORDS_FILE_KEY to File(stopwordsFile).absolutePath,
                    INVERTED_INDEX_FILE_KEY to File(invertedIndexFile).absolutePath
            )

    fun getDataDir(): String =
            getAllProperties()[DATA_DIR_KEY] ?: ".."

    fun getDirectIndexDir(): String =
            getAllProperties()[DIRECT_INDEX_DIR_KEY] ?: ".."

    fun getStopWordsFile(): String =
            getAllProperties()[STOP_WORDS_FILE_KEY] ?: "stopwords.txt"

    fun getInvertedIndexFile(): String =
            getAllProperties()[INVERTED_INDEX_FILE_KEY] ?: "index.txt"

}