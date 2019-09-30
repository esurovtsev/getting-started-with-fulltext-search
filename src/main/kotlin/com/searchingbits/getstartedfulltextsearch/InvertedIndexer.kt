package com.searchingbits.getstartedfulltextsearch

import com.google.gson.GsonBuilder
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class InvertedIndexer(
        private val analyzer: TokenAnalyzer,
        private val documentService: DocumentService,
        private val config: Configuration
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val index: MutableMap<String, MutableList<String>> = mutableMapOf()

    @PostConstruct
    fun loadIndex() {
        val invertedIndex = File(config.getInvertedIndexFile())

        if (invertedIndex.exists() && invertedIndex.isFile && invertedIndex.canRead()) {
            index.putAll(gson.fromJson(invertedIndex.readText(), MutableMap::class.java) as MutableMap<String, ArrayList<String>>)
        }
    }

    fun createIndex() {
        // delete old index
        File(config.getInvertedIndexFile()).delete()
        index.clear()

        // generate new index
        documentService.findAllIds().forEach { docId ->
            documentService.findById(docId)?.let { doc ->
                analyzer
                    .analyze_betterTokenizing(doc)
                    .groupBy { it }
                    .entries
                    .map { it.key to docId }
                    .forEach { index[it.first]?.add(it.second) ?: index.put(it.first, mutableListOf(it.second)) }
            }
        }
        File(config.getInvertedIndexFile()).writeText(gson.toJson(index))
    }

    fun getPostingListByToken(token: String): List<String> =
            index[token] ?: listOf()
}