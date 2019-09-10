package com.grabduck.searchengine

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Commands(private val documentService: DocumentService) {
    @ShellMethod("List all available document ids")
    fun findAllIds(): List<String> =
            documentService.findAllIds()
}