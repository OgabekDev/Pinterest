package dev.ogabek.pinterest.network.service

import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.Topic
import retrofit2.Call
import retrofit2.http.*

interface TopicService {

    @Headers("Authorization: Client-ID 5GhEYVZz8Fpt9z0dCoxbNWBq1VTHeEQC5nRxgrgrSIg")
    @GET("topics")
    fun getTopics(): Call<ArrayList<Topic>>

    @Headers("Authorization: Client-ID 5GhEYVZz8Fpt9z0dCoxbNWBq1VTHeEQC5nRxgrgrSIg")
    @GET("topics/{id}/photos")
    fun getTopicPhotos(@Path("id") id: String, @Query("page") page: Int, @Query("per_page") per_page: Int): Call<ArrayList<Image>>

}