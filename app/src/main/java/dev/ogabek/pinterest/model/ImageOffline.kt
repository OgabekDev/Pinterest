package dev.ogabek.pinterest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ogabek.pinterest.model.sub_model.Links
import dev.ogabek.pinterest.model.sub_model.Urls
import dev.ogabek.pinterest.model.sub_model.User
import java.io.Serializable

@Entity(tableName = "image_list")
data class ImageOffline(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "likes")
    val likes: Long,

    @ColumnInfo(name = "color")
    val color: String

) : Serializable
