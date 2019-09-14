package com.grabduck.searchengine

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

data class PostingListItem(val docId: String, val count: Int)

typealias PostingList = MutableList<PostingListItem>

@Component
class InvertedIndexer(
        private val analyzer: TokenAnalyzer,
        private val documentService: DocumentService,
        private val config: Configuration
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val index: MutableMap<String, PostingList> = mutableMapOf()

    @PostConstruct
    fun loadIndex() {
        val invertedIndex = File(config.getInvertedIndexFile())

        if (invertedIndex.exists() && invertedIndex.isFile && invertedIndex.canRead()) {
            val input = gson.fromJson(invertedIndex.readText(), MutableMap::class.java)
                    as MutableMap<String, ArrayList<LinkedTreeMap<String, Double>>>

            input.entries.forEach {
                index[it.key] = it.value.map { v -> PostingListItem(v["docId"].toString(), v["count"]!!.toInt()) }.toMutableList()
            }
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
                    .index(doc)
                    .groupBy { it }
                    .entries
                    .map { it.key to PostingListItem(docId, it.value.size) }
                    .forEach {
                        index[it.first]?.add(it.second) ?: index.put(it.first, mutableListOf(it.second))
                    }
            }
        }
        File(config.getInvertedIndexFile()).writeText(gson.toJson(index))
    }

    fun getDocumentIdsByToken(token: String): List<String> =
            index[token]?.let { postingList -> postingList.map { it.docId } } ?: listOf()
}