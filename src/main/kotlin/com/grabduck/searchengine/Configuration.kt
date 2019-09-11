package com.grabduck.searchengine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

const val DATA_DIR_KEY = "com.grabduck.searchengine.dataDir"

@Component
class Configuration {
    @Value("\${$DATA_DIR_KEY}")
    private lateinit var dataDir: String

    fun getAllProperties(): Map<String, String> =
            mapOf(
                    DATA_DIR_KEY to File(dataDir).absolutePath
            )

    fun getDataDir(): String =
            getAllProperties()[DATA_DIR_KEY] ?: ""

}