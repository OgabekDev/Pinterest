package dev.ogabek.pinterest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ogabek.pinterest.model.ImageOffline

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveImage(image: ImageOffline)

    @Query("SELECT * from image_list")
    fun getNotes(): List<ImageOffline>

    @Query("delete from image_list")
    fun clearDataBase()

}