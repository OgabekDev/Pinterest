package dev.ogabek.pinterest.model.sub_model

import java.io.Serializable

data class Urls (
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    val small_s3: String
): Serializable