package dev.ogabek.pinterest.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.fragment.HomeFragment
import dev.ogabek.pinterest.model.Topic

class TopicAdapter(val context: Context, val topics: ArrayList<Topic>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var selected_index = 0;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_topic, parent, false)
        return TopicViewHolder(view)
    }

    class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val topic = topics[position]
        if (holder is TopicViewHolder) {
            holder.apply {
                title.text = topic.title
                title.isSelected = topic.isSelected

                if (topic.isSelected) {
                    title.setTextColor(Color.WHITE)
                    selected_index=position
                } else {
                    title.setTextColor(Color.BLACK)
                }

                title.setOnClickListener {
                    if (title.text == "For you") {
                        HomeFragment.getPhotos(true)
                    } else {
                        HomeFragment.getTopicPhotos(topic)
                        topic.page++
                        notifyItemChanged(position)
                    }
                    if (!title.isSelected) {
                        selected(position)
                    }
                }
            }
        }
    }

    override fun getItemCount() = topics.size

    private fun selected(position: Int) {
        topics[position].isSelected=true
        topics[selected_index].isSelected=false
        notifyItemChanged(position)
        notifyItemChanged(selected_index)
        selected_index=position

    }
}