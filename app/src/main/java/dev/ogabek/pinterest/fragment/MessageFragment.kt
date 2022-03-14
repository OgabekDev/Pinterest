package dev.ogabek.pinterest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.adapter.MessageAdapter
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.network.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageFragment : Fragment(R.layout.fragment_message) {

    private lateinit var rv_message: RecyclerView
    private lateinit var refresh_message: SwipeRefreshLayout

    private val news: ArrayList<Image> = ArrayList()

    private lateinit var adapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        getPhotos(true)

    }

    private fun initViews(view: View) {
        adapter = MessageAdapter(requireContext(), news)
        rv_message = view.findViewById(R.id.rv_message)
        rv_message.layoutManager = GridLayoutManager(requireContext(), 1)
        rv_message.adapter = adapter

        rv_message.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = GridLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    getPhotos(false)
                }
            }
        })

        refresh_message = view.findViewById(R.id.refresh_message)
        refresh_message.setOnRefreshListener {
            page = 0
            getPhotos(true)
            refresh_message.isRefreshing = false
        }
    }

    var page: Int = 0
    val per_page: Int = 20

    private val TAG: String = MessageFragment::class.java.simpleName

    fun getPhotos(isClear: Boolean) {
        RetrofitHttp.photoService.getPhotos(page++, per_page).enqueue(object:
            Callback<ArrayList<Image>> {
            override fun onResponse(call: Call<ArrayList<Image>>, response: Response<ArrayList<Image>>) {
                if (!response.body().isNullOrEmpty()) {
                    Logger.d(TAG, response.body().toString())
                    if (isClear) news.clear()
                    news.addAll(response.body()!!)
                    rv_message.adapter!!.notifyDataSetChanged()
                } else {
                    Logger.e(TAG, "Response is null")
                }
            }

            override fun onFailure(call: Call<ArrayList<Image>>, t: Throwable) {
                Logger.e(TAG, t.message.toString())
            }

        })
    }

}