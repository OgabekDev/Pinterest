package dev.ogabek.pinterest.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.activity.DetailsActivity
import dev.ogabek.pinterest.model.Image

class MessageAdapter(val context: Context, val newsList: ArrayList<Image>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_message)
        val name: TextView = view.findViewById(R.id.tv_name)
        val description: TextView = view.findViewById(R.id.tv_news)
        val time: TextView = view.findViewById(R.id.tv_time)
        val item: LinearLayout = view.findViewById(R.id.item_message)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news = newsList[position]

        if (holder is MessageViewHolder) {
            holder.apply {
                Glide.with(context).load(news.urls.small).placeholder(ColorDrawable(Color.parseColor(news.color))).error(ColorDrawable(Color.parseColor(news.color))).into(image)
                name.text = Html.fromHtml("<b>${news.user.name}</b> has uploaded new photo")
                when {
                    news.description != null -> {
                        description.text = news.description
                    }
                    news.alt_description != null -> {
                        description.text = news.alt_description
                    }
                    else -> {
                        description.text = "Tap to view"
                    }
                }
                val date = news.updated_at // 2022-03-10T12:01:18-05:00
                time.text = "${date.substring(0, 10)} at ${date.subSequence(11, 19)}"

                item.setOnClickListener {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("image", news)
                    intent.putExtra("picture", FeedAdapter.getBitmapImage(image))
                    context.startActivity(intent)
                }

            }
        }

    }

    override fun getItemCount() = newsList.size
}