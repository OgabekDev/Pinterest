package dev.ogabek.pinterest.database

import androidx.room.*
import dev.ogabek.pinterest.model.ImageOffline

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveImage(image: ImageOffline)

    @Query("SELECT * from image_list")
    fun getNotes(): List<ImageOffline>

    @Query("DELETE from image_list")
    fun clearDataBase()

    @Delete
    fun deleteFromDatabase(image: ImageOffline)

}