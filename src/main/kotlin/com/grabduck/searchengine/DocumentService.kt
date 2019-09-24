package com.grabduck.searchengine

import org.springframework.stereotype.Service
import java.io.File

@Service
class DocumentService(private val config: Configuration) {
    fun findAllIds(): List<String> =
            File(config.getDataDir()).listFiles().map { it.name }

    fun findById(documentId: String): String? =
            this
                .findAllIds()
                .findLast { it == documentId }
                ?.let { File("${config.getDataDir()}/$it") }
                ?.takeIf { it.exists() && it.isFile && it.canRead() }
                ?.let { it.readText() }
}