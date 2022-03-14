package dev.ogabek.pinterest.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import java.io.ByteArrayOutputStream


class FeedAdapter(val context: Context, val feeds: ArrayList<Image>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                Glide.with(context).load(feed.urls.thumb).placeholder(ColorDrawable(Color.parseColor(feed.color))).into(image)
                likes.text = feed.likes.toString()
                item.setOnClickListener {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("image", feed)
                    intent.putExtra("picture", getBitmapImage(image))
                    context.startActivity(intent)
                }
            }
        }

    }

    companion object {
        fun getBitmapImage(image: ImageView): ByteArray {
            val draw: BitmapDrawable = image.drawable as BitmapDrawable
            val bitmap: Bitmap = draw.bitmap
            val steam = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam)
            return steam.toByteArray()
        }
    }

    private fun setSize(imageView: ImageView, heightPixels: Long, widthPixels: Long) {
        //Change pixel to dp
        val heightInDp = (heightPixels / context.resources.displayMetrics.density).toInt()

        val width = imageView.measuredWidth
        Logger.d("Image View size", width.toString())

        // Algorithm
        val ratio: Long = widthPixels / heightPixels


        //Set layout with programmatically
        val params = imageView.layoutParams
        params.height = (width * ratio).toInt()
        imageView.layoutParams = params
    }

    override fun getItemCount() = feeds.size
}