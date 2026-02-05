package com.example.recipeviewer.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
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
}
