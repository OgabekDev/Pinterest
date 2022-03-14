package dev.ogabek.pinterest.database

import android.app.Application
import android.util.Log
import dev.ogabek.pinterest.meneger.RoomManager
import dev.ogabek.pinterest.model.ImageOffline

class PictureRepository(application: Application) {

    private val TAG: String = PictureRepository::class.java.simpleName

    private val db = RoomManager.getDatabase(application)
    private val pictureDao: ImageDao = db!!.imageDao()

    fun getImages(): List<ImageOffline> {
        return pictureDao.getNotes()
    }

    fun saveImage(image: ImageOffline) {
        Log.d(TAG, "saveNote: saved")
        pictureDao.saveImage(image)
    }

    fun clearDataBase() {
        pictureDao.clearDataBase()
    }

}