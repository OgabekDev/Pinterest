package dev.ogabek.pinterest.network.service

import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.SearchImages
import retrofit2.Call
import retrofit2.http.*

interface PhotoService {

    @Headers("Authorization: Client-ID 5GhEYVZz8Fpt9z0dCoxbNWBq1VTHeEQC5nRxgrgrSIg")
    @GET("/photos")
    fun getPhotos(@Query("page") page: Int, @Query("per_page") per_page: Int): Call<ArrayList<Image>>

    @Headers("Authorization: Client-ID 5GhEYVZz8Fpt9z0dCoxbNWBq1VTHeEQC5nRxgrgrSIg")
    @GET("/search/photos")
    fun searchPhotos(@Query("query") query: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Call<SearchImages>



}