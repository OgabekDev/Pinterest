package dev.ogabek.pinterest.activity

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.WindowInsetsController
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.database.PictureRepository
import dev.ogabek.pinterest.helper.Logger
import dev.ogabek.pinterest.model.Image
import dev.ogabek.pinterest.model.ImageOffline
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.Executors

class DetailsActivity : AppCompatActivity() {

    private val TAG: String = DetailsActivity::class.java.simpleName

    private lateinit var iv_back: ImageView
    private lateinit var iv_more: ImageView
    private lateinit var iv_main: ImageView

    private lateinit var iv_profile: ShapeableImageView
    private lateinit var tv_profile_name: TextView
    private lateinit var tv_profile_bio: TextView

    private lateinit var tv_img_title: TextView
    private lateinit var tv_img_description: TextView

    private lateinit var btn_open: Button

    private lateinit var btn_download: LottieAnimationView
    private lateinit var btn_view: Button
    private lateinit var btn_save: Button
    private lateinit var btn_share: ImageView

    private lateinit var instagram: LinearLayout
    private lateinit var tv_instagram: TextView

    private lateinit var twitter: LinearLayout
    private lateinit var tv_twitter: TextView

    private lateinit var portfolio: LinearLayout
    private lateinit var tv_portfolio: TextView

    private lateinit var image: Image

    private lateinit var imageView: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)

        initViews()
        onClicks()
        getInfo()
        setData()

    }

    private fun getInfo() {
        image = intent.getSerializableExtra("image") as Image
        imageView = intent.getByteArrayExtra("picture") as ByteArray
        Logger.d(TAG, image.toString())
    }

    private fun setData() {
        Glide.with(this).load(image.urls.regular).placeholder(ColorDrawable(Color.parseColor(image.color))).into(iv_main)
        iv_main.setImageBitmap(BitmapFactory.decodeByteArray(imageView, 0, imageView.size))
        Glide.with(this).load(image.user.profile_image.large).into(iv_profile)
        tv_profile_name.text = "${image.user.name} (${image.user.username})"
        tv_profile_bio.text = image.user.bio
        tv_img_title.text = image.description
        tv_img_description.text = image.alt_description

        if (image.user.social.instagram_username == null) {
            tv_portfolio.text = "none"
        } else {
            tv_instagram.text = image.user.social.instagram_username
        }

        if (image.user.social.twitter_username == null) {
            tv_twitter.text = "none"
        } else {
            tv_twitter.text = image.user.social.twitter_username
        }

        if (image.user.social.portfolio_url == null) {
            tv_portfolio.text = "none"
        } else {
            tv_portfolio.text = image.user.social.portfolio_url
        }

    }

    private fun initViews() {
        iv_back = findViewById(R.id.iv_back_details)
        iv_more = findViewById(R.id.iv_more_details)
        iv_main = findViewById(R.id.iv_img_details)

        iv_profile = findViewById(R.id.iv_profile_image)
        tv_profile_name = findViewById(R.id.tv_profile_name)
        tv_profile_bio = findViewById(R.id.tv_profile_bio)

        tv_img_title = findViewById(R.id.tv_image_title)
        tv_img_description = findViewById(R.id.tv_image_description)

        btn_open = findViewById(R.id.btn_open)

        btn_download = findViewById(R.id.anim_download)
        btn_view = findViewById(R.id.btn_view)
        btn_save = findViewById(R.id.btn_save)
        btn_share = findViewById(R.id.iv_image_share)

        instagram = findViewById(R.id.instagram)
        tv_instagram = findViewById(R.id.tv_instagram)

        twitter = findViewById(R.id.twitter)
        tv_twitter = findViewById(R.id.tv_twitter)

        portfolio = findViewById(R.id.portfolio)
        tv_portfolio = findViewById(R.id.tv_portfolio)

    }

    private fun onClicks() {

        iv_back.setOnClickListener {
            super.onBackPressed()
        }

        btn_download.setOnClickListener {
            btn_download.setAnimation("download.json")
            btn_download.playAnimation()
            // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save in External Storage (Micro SD)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                askPermission()
            } else {
                saveImageToGallery(image.links.download)
            }
        }

        btn_view.setOnClickListener {
            openUrl(image.links.html)
        }

        btn_open.setOnClickListener {
            openUrl(image.user.links.html)
        }

        instagram.setOnClickListener {
            if (image.user.social.instagram_username != null) {
                openUrl("https://instagram.com/${image.user.social.instagram_username!!}")
            } else {
                Toast.makeText(this, "Couldn't find any Instagram account :(", Toast.LENGTH_SHORT).show()
            }
        }

        twitter.setOnClickListener {
            if (image.user.social.twitter_username != null) {
                openUrl("https://twitter.com/${image.user.social.twitter_username!!}")
            } else {
                Toast.makeText(this, "Couldn't find any Twitter account :(", Toast.LENGTH_SHORT).show()
            }
        }

        portfolio.setOnClickListener {
            if (image.user.social.portfolio_url != null) {
                openUrl(image.user.social.portfolio_url!!)
            } else {
                Toast.makeText(this, "Couldn't find any Portfolio :(", Toast.LENGTH_SHORT).show()
            }
        }

        btn_save.setOnClickListener {
            val imageOffline = ImageOffline(image.id, image.urls.regular, image.likes, image.color)
            saveImageToDatabase(imageOffline)
        }

        btn_share.setOnClickListener {
            shareUrl(image.links.html)
        }

    }

    private fun saveImageToDatabase(imageOffline: ImageOffline) {
        val repository = PictureRepository(application)
        val executor = Executors.newSingleThreadExecutor()  // in Background
        Toast.makeText(this, "Image has been saved your account. You can view it even offline", Toast.LENGTH_LONG).show()
        executor.execute {
            repository.saveImage(imageOffline)
        }
    }

    fun getBitmapImage(image: ImageView): ByteArray {
        val draw: BitmapDrawable = image.drawable as BitmapDrawable
        val bitmap: Bitmap = draw.bitmap
        val steam = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam)
        return steam.toByteArray()
    }

    companion object {

        val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    }

    var msg: String? = ""
    var lastMsg = ""

    @SuppressLint("Range")
    private fun saveImageToGallery(url: String) {
        Logger.d(TAG, "Saving image started")

        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) directory.mkdirs()

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("IMG_${image.id}.jpg")
                .setDescription("")
                .setDestinationInExternalPublicDir(directory.toString(), "IMG_${image.id}.jpg")
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) { runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()

    }

    private fun statusMessage(url: String, directory: File, status: Int): String {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + "IMG_${image.id}.jpg"
            else -> "There's nothing to download"
        }
        return msg
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1) {
            saveImageToGallery(image.links.download)
        }
    }

    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun shareUrl(url: String) {
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle("Share URL")
            .setText(url)
            .startChooser()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                //this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos")
                    .setPositiveButton("Accept") { dialog,  _ ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Deny") { dialog, _ -> dialog.cancel()}
                    .show()
            } else {
                // NNo explanation needed, we can request the permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request
            }
        } else {
            // Permission has already been granted
            saveImageToGallery(image.links.download)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // if request is canceled, the result arrays are empty
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                    // Download the Image
                    saveImageToGallery(image.links.download)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request
            else -> {
                // Ignore all other requests
            }
        }
    }

}