package dev.ogabek.pinterest.model.sub_model

import java.io.Serializable

data class Links(
    val download: String,
    val download_location: String,
    val html: String,
    val self: String
): Serializable