package com.grabduck.searchengine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

interface DocumentService {
    fun findAllIds(): List<String>

    fun findById(documentId: String): String?
}

@Service
class DocumentServiceImpl : DocumentService {
    @Value("\${com.grabduck.searchengine.dataDir}")
    private lateinit var dataDir: String

    override fun findAllIds(): List<String> =
            File(dataDir).listFiles().map { it.name }

    override fun findById(documentId: String): String? =
            this
                .findAllIds()
                .findLast { it == documentId }
                ?.let { File("$dataDir/$it") }
                ?.takeIf { it.exists() && it.isFile && it.canRead() }
                ?.let { it.readText() }
}