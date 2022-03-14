package dev.ogabek.pinterest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.model.ImageOffline

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val data = intent.getSerializableExtra("picture") as ImageOffline
        val image: ImageView = findViewById(R.id.image)
        Glide.with(this).load((data.image)).into(image)

    }
}