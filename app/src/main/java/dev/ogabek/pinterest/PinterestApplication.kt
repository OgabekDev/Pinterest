package dev.ogabek.pinterest

import android.app.Application

class PinterestApplication: Application() {

    override fun onCreate() {
        instanse = this
        super.onCreate()
    }

    companion object {
        lateinit var instanse: Application
    }

}