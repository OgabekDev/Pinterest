package dev.ogabek.pinterest.fragment

import android.app.Application
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.adapter.OfflineFeedAdapter
import dev.ogabek.pinterest.database.PictureRepository
import dev.ogabek.pinterest.model.ImageOffline

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var rv_offline: RecyclerView

    private lateinit var offlineList: ArrayList<ImageOffline>
    private lateinit var adapter: OfflineFeedAdapter

    private lateinit var repository: PictureRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offlineList = ArrayList()
        adapter = OfflineFeedAdapter(requireContext(), offlineList)
        initViews(view)

    }

    private fun initViews(view: View) {
        rv_offline = view.findViewById(R.id.rv_offline)
        rv_offline.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_offline.adapter = adapter

        getImageFromDatabase()
    }

    fun getImageFromDatabase() {
        repository = PictureRepository(requireActivity().application)
        Log.d(ContentValues.TAG, "getAllNotes: ${repository.getImages()}")
        offlineList.clear()
        offlineList.addAll(repository.getImages())
        rv_offline.adapter!!.notifyDataSetChanged()
    }

}