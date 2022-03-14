package dev.ogabek.pinterest.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.adapter.FeedAdapter
import dev.ogabek.pinterest.adapter.TopicAdapter
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.Topic
import dev.ogabek.pinterest.network.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rv_topics = view.findViewById(R.id.rv_topics)
        rv_home_feed = view.findViewById(R.id.rv_home_feed)
        refresh_home = view.findViewById(R.id.refresh_home)


        rv_home_feed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = StaggeredGridLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPositions = (Objects.requireNonNull(recyclerView.layoutManager) as StaggeredGridLayoutManager).findLastVisibleItemPositions(null).maxOrNull()
                val endHasBeenReached = lastVisibleItemPositions!! + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    getPhotos(false)
                }
            }
        })

        refresh_home.setOnRefreshListener(refreshListener)

        rv_topics.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        rv_home_feed.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        refreshTopicAdapter(requireContext(), rv_topics, topics)
        refreshFeedAdapter(requireContext(), rv_home_feed, feeds)

        getTopics()
        getPhotos(true)

        return view

    }

    companion object {

        var page: Int = 1
        private val per_page: Int = 20

        private val topics = ArrayList<Topic>()
        private val feeds = ArrayList<Image>()

        private lateinit var rv_topics: RecyclerView
        private lateinit var rv_home_feed: RecyclerView

        @SuppressLint("StaticFieldLeak")
        private lateinit var refresh_home: SwipeRefreshLayout

        private val TAG: String = HomeFragment::class.java.simpleName

        fun getTopics() {
            RetrofitHttp.topicService.getTopics().enqueue(object : Callback<ArrayList<Topic>>{
                override fun onResponse(call: Call<ArrayList<Topic>>, response: Response<ArrayList<Topic>>) {
                    if (!response.body().isNullOrEmpty()) {
                        Logger.d(TAG, response.body().toString())
                        topics.clear()
                        topics.add(Topic("For you", true))
                        topics.addAll(response.body()!!)
                        rv_topics.adapter!!.notifyDataSetChanged()
                    } else {
                        Logger.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Topic>>, t: Throwable) {
                    Logger.e(TAG, t.message.toString())
                }

            })
        }
        fun getPhotos(isClear: Boolean) {
            RetrofitHttp.photoService.getPhotos(page++, per_page).enqueue(object: Callback<ArrayList<Image>> {
                override fun onResponse(call: Call<ArrayList<Image>>, response: Response<ArrayList<Image>>) {
                    if (!response.body().isNullOrEmpty()) {
                        Logger.d(TAG, response.body().toString())
                        if (isClear) feeds.clear()
                        feeds.addAll(response.body()!!)
                        rv_home_feed.adapter!!.notifyDataSetChanged()
                    } else {
                        Logger.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Image>>, t: Throwable) {
                    Logger.e(TAG, t.message.toString())
                }

            })
        }
        fun getTopicPhotos(topic: Topic) {
            RetrofitHttp.topicService.getTopicPhotos(topic.id, topic.page, topic.per_page).enqueue(object: Callback<ArrayList<Image>> {
                override fun onResponse(call: Call<ArrayList<Image>>, response: Response<ArrayList<Image>>) {
                    if (!response.body().isNullOrEmpty()) {
                        Logger.d(TAG, response.body().toString())
                        feeds.clear()
                        feeds.addAll(response.body()!!)
                        rv_home_feed.adapter!!.notifyDataSetChanged()
                    } else {
                        Logger.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<ArrayList<Image>>, t: Throwable) {
                    Logger.e(TAG, t.message.toString())
                }

            })
        }

        private fun refreshTopicAdapter(context: Context, recyclerView: RecyclerView, topics: ArrayList<Topic>) {
            recyclerView.adapter = TopicAdapter(context, topics)
        }

        private fun refreshFeedAdapter(context: Context, recyclerView: RecyclerView, feeds: java.util.ArrayList<Image>) {
            recyclerView.adapter = FeedAdapter(context, feeds)
        }

        private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            getPhotos(true)
            refresh_home.isRefreshing = false
        }

    }

}