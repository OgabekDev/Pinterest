package dev.ogabek.pinterest.model

import java.io.Serializable

data class SearchImages (
    val total: Long,
    val total_pages: Long,
    val results: ArrayList<Image>
): Serializable