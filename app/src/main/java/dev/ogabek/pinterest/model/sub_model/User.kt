package dev.ogabek.pinterest.model.sub_model

import java.io.Serializable

data class User (
    val id: String,
    val username: String,
    val name: String,
    val bio: String? = null,
    val links: Links,
    val profile_image: ProfileImage,
    val social: Social
): Serializable
