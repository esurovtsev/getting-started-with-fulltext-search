package com.searchingbits.getstartedfulltextsearch

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

const val DATA_DIR_KEY = "com.searching-bits.get-started-fulltext-search.data-dir"
const val DIRECT_INDEX_DIR_KEY = "com.searching-bits.get-started-fulltext-search.direct-index-dir"

@Component
class Configuration {
    @Value("\${$DATA_DIR_KEY}")
    private lateinit var dataDir: String

    @Value("\${$DIRECT_INDEX_DIR_KEY}")
    private lateinit var directIndexDir: String

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
                DIRECT_INDEX_DIR_KEY to File(directIndexDir).absolutePath
            )

    fun getDataDir(): String =
            getAllProperties()[DATA_DIR_KEY] ?: ".."

    fun getDirectIndexDir(): String =
            getAllProperties()[DIRECT_INDEX_DIR_KEY] ?: ".."
}