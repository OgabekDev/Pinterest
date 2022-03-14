package dev.ogabek.pinterest.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.adapter.FeedAdapter
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.SearchImages
import dev.ogabek.pinterest.model.Topic
import dev.ogabek.pinterest.network.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        et_search = view.findViewById(R.id.et_search)
        rv_search = view.findViewById(R.id.rv_search)
        iv_clear = view.findViewById(R.id.clear_text)
        tv_search = view.findViewById(R.id.tv_search)

        tv_search.setOnClickListener {
            if (et_search.text.toString().isNotEmpty()) getSearchFeeds(true, et_search.text.toString())
            hideKeyboard(et_search)
        }

        rv_search.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = StaggeredGridLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPositions = (Objects.requireNonNull(recyclerView.layoutManager) as StaggeredGridLayoutManager).findLastVisibleItemPositions(null).maxOrNull()
                val endHasBeenReached = lastVisibleItemPositions!! + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    getSearchFeeds(false, et_search.text.toString())
                }
            }
        })

        et_search.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                Logger.d(TAG, "VISIBLE")
                iv_clear.visibility = View.VISIBLE
            } else {
                Logger.d(TAG, "GONE")
                iv_clear.visibility = View.GONE
            }
            Logger.d(TAG, "Text has been changed")
            page = 1
        }

        rv_search.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        refreshSearchAdapterAdapter(requireContext(), rv_search, searchFeed)

        iv_clear.setOnClickListener {
            et_search.setText("")
            searchFeed.clear()
            page = 1
            rv_search.adapter!!.notifyDataSetChanged()
        }

        return view

    }



    private fun refreshSearchAdapterAdapter(context: Context, recyclerView: RecyclerView, searchFeed: ArrayList<Image>) {
        val adapter = FeedAdapter(context, searchFeed)
        recyclerView.adapter = adapter
    }

    companion object {
        private lateinit var et_search: TextInputEditText
        private lateinit var rv_search: RecyclerView
        private lateinit var iv_clear: ImageView
        private lateinit var tv_search: TextView

        private val TAG: String = SearchFragment::class.java.simpleName

        val searchFeed = ArrayList<Image>()

        private var page: Int = 1
        private val per_page: Int = 20

        fun getSearchFeeds(isClear: Boolean, query: String) {
            RetrofitHttp.photoService.searchPhotos(query, page++, per_page).enqueue(object : Callback<SearchImages>{
                override fun onResponse(call: Call<SearchImages>, response: Response<SearchImages>) {
                    if (response.body() != null) {
                        Logger.d(TAG, response.body().toString())
                        if (isClear) searchFeed.clear()
                        searchFeed.addAll(response.body()!!.results)
                        rv_search.adapter!!.notifyDataSetChanged()
                    } else{
                        Logger.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<SearchImages>, t: Throwable) {
                    Logger.e(TAG, "Response is null")
                }

            })
        }

    }

    fun hideKeyboard(view: View) =
        (requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager)!!
            .hideSoftInputFromWindow(view.windowToken, 0)

}