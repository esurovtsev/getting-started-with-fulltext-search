package com.grabduck.searchengine

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

interface DocumentService {
    fun findAllIds(): List<String>
}

@Service
class DocumentServiceImpl : DocumentService {
    @Value("\${com.grabduck.searchengine.dataDir}")
    private lateinit var dataDir: String

    override fun findAllIds(): List<String> =
            File(dataDir).listFiles().map { it.name }
}