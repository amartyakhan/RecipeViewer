package com.example.recipeviewer.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class JsoupRecipeScraper @Inject constructor() : RecipeScraper {
    override suspend fun scrapeText(url: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val doc = Jsoup.connect(url).get()
            val article = doc.select("article").first() // Prioritize <article>
            val body = doc.select("body").first() // Fallback to <body>

            val content = article?.text() ?: body?.text() ?:
                throw IllegalStateException("Could not extract meaningful content from the URL.")
            content
        }
    }

    override suspend fun extractImageUrl(url: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val doc = Jsoup.connect(url).get()
            
            // 1. OpenGraph image tag
            val ogImage = doc.select("meta[property=og:image]").attr("content")
            if (ogImage.isNotEmpty()) return@runCatching ogImage

            // 2. Schema.org Recipe image (usually in ld+json, but might be in meta)
            val schemaImage = doc.select("meta[itemprop=image]").attr("content")
            if (schemaImage.isNotEmpty()) return@runCatching schemaImage

            // 3. First large image within <article> if available
            val articleImage = doc.select("article img[src]").firstOrNull { 
                val width = it.attr("width").toIntOrNull() ?: 0
                width > 200 // Simple heuristic for a "main" image
            }?.attr("abs:src")
            if (articleImage != null && articleImage.isNotEmpty()) return@runCatching articleImage

            // 4. Fallback to any prominent image in the body
            val bodyImage = doc.select("body img[src]").firstOrNull {
                val width = it.attr("width").toIntOrNull() ?: 0
                width > 300
            }?.attr("abs:src")
            
            bodyImage?.takeIf { it.isNotEmpty() }
        }.getOrNull()
    }
}
