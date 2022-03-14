package dev.ogabek.pinterest.network

import dev.ogabek.pinterest.network.service.PhotoService
import dev.ogabek.pinterest.network.service.TopicService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHttp {

    companion object {

        private val TAG: String = RetrofitHttp::class.java.simpleName

        const val IS_TESTER = true;

        private const val SERVER_DEVELOPMENT = "https://api.unsplash.com/"
        private const val SERVER_PRODUCTION = "https://api.unsplash.com/"

        private fun server(): String {
            return if (IS_TESTER) {
                SERVER_DEVELOPMENT
            } else {
                SERVER_PRODUCTION
            }
        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(server())
                .build()
        }

        val topicService: TopicService = getRetrofit().create(TopicService::class.java)
        val photoService: PhotoService = getRetrofit().create(PhotoService::class.java)

    }

}