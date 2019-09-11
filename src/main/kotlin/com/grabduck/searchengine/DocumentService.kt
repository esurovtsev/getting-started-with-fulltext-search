package com.grabduck.searchengine

import org.springframework.stereotype.Service
import java.io.File

interface DocumentService {
    fun findAllIds(): List<String>

    fun findById(documentId: String): String?
}

@Service
class DocumentServiceImpl(private val config: Configuration) : DocumentService {
    override fun findAllIds(): List<String> =
            File(config.getDataDir()).listFiles().map { it.name }

    override fun findById(documentId: String): String? =
            this
                .findAllIds()
                .findLast { it == documentId }
                ?.let { File("${config.getDataDir()}/$it") }
                ?.takeIf { it.exists() && it.isFile && it.canRead() }
                ?.let { it.readText() }
}