package dev.ogabek.pinterest.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.activity.DetailsActivity
import dev.ogabek.pinterest.activity.ImageActivity
import dev.ogabek.pinterest.fragment.ProfileFragment
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.ImageOffline
import java.io.ByteArrayOutputStream


class OfflineFeedAdapter(val context: ProfileFragment, val feeds: ArrayList<ImageOffline>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_feed, parent, false)

        return HomeFeedViewHolder(view)
    }

    class HomeFeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item: LinearLayout = view.findViewById(R.id.item_home_feed)
        val image: ImageView = view.findViewById(R.id.iv_home_feed)
        val more: ImageView = view.findViewById(R.id.iv_home_feed_more)
        val likes: TextView = view.findViewById(R.id.tv_home_feed_likes)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feed = feeds[position]

        Logger.d("onBindViewHolder", "galdi $position")

        if (holder is HomeFeedViewHolder) {
            holder.apply {
                likes.text = feed.likes.toString()
                Glide.with(context).load(feed.image).placeholder(ColorDrawable(Color.parseColor(feed.color))).into(image)
                item.setOnClickListener {
                    val intent = Intent(context.requireContext(), ImageActivity::class.java)
                    intent.putExtra("picture", feed)
                    context.startActivity(intent)
                }
                more.setOnClickListener {
                    context.deleteFromDatabase(feed)
                    context.notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount() = feeds.size
}