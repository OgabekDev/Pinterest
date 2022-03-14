package dev.ogabek.pinterest.model

import java.io.Serializable

data class Topic(
    val title: String,
    var isSelected: Boolean = false,
    val id: String = "",
    val slug: String = "",
    val total_photos: Long = 0,
    var page: Int = 1,
    val per_page: Int = 20
): Serializable