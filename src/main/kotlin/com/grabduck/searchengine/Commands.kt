package com.grabduck.searchengine

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Commands(private val documentService: DocumentService) {
    @ShellMethod("List all available document ids")
    fun findAllIds(): List<String> =
            documentService.findAllIds()

    @ShellMethod("Show document content by document ID")
    fun findById(documentId: String): String =
            documentService.findById(documentId) ?: "document not found"
}