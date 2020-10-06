package com.searchingbits.getstartedfulltextsearch

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent
class Commands(
    private val config: Configuration,
    private val documentService: DocumentService
) {
    @ShellMethod("Show current configuration")
    fun config(): List<String> =
            config.getAllProperties().entries.map { "${it.key}: ${it.value}" }

    @ShellMethod("Lists documents or document content")
    fun document(
            @ShellOption(help = "Document ID or 'all' for getting list of all documents")
            id: String
    ): List<String> =
            if (id == "all") {
                documentService.findAllIds()
            } else {
                listOf(documentService.findById(id) ?: "document not found")
            }
}