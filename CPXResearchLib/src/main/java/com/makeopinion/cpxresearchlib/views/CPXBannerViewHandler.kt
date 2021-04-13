package com.makeopinion.cpxresearchlib.views

import android.R.attr.bitmap
import android.app.Activity
import android.graphics.Color
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.makeopinion.cpxresearchlib.NetworkService
import com.makeopinion.cpxresearchlib.R
import com.makeopinion.cpxresearchlib.models.CPXConfiguration
import com.squareup.picasso.Picasso


class CPXBannerViewHandler(
    private val activity: Activity,
    private val configuration: CPXConfiguration,
    private val listener: CPXBannerViewHandlerListener
) {
    private var view: View? = null
    private var imageView: ImageView? = null

    fun install() {
        if (view == null) {
            inflateView()
        }

        view?.let {
            if (it.parent == null) {
                val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                params.gravity = configuration.style.getGravity()
                rootView.addView(it, params)
                rootView.invalidate()
            }
        }

        imageView?.let {
            val imageUri = NetworkService.imageUrlFor(configuration)
            Picasso.get().load(imageUri).into(it)
            /*it.setOnClickListener {
                listener.onImageTapped()
            }*/
            it.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    event?.let { localEvent ->
                        val imageWidth = it.width
                        val imageHeight = it.height
                        val bmp = it.drawable.toBitmap()

                        val scaleX = bmp.width / imageWidth
                        val scaleY = bmp.height / imageHeight

                        val x = localEvent.x.toInt() * scaleX
                        val y = localEvent.y.toInt() * scaleY

                        if (x < bmp.width && y < bmp.height) {
                            val pixel: Int = bmp.getPixel(x, y)

                            val redValue: Int = Color.red(pixel)
                            val blueValue: Int = Color.blue(pixel)
                            val greenValue: Int = Color.green(pixel)

                            val result = redValue != 0 || blueValue != 0 || greenValue != 0

                            if (result) {
                                when (localEvent.action) {
                                    ACTION_DOWN -> listener.onImageTapped()
                                }
                            }

                            return result
                        }
                    }

                    return false
                }
            })
        }
    }

    fun remove() {
        view?.let {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            rootView.removeView(it)
        }
    }

    private fun inflateView() {
        activity.layoutInflater.inflate(R.layout.bannerview, null).let {
            view = it
            imageView = it.findViewById(R.id.banner_image)
        }
    }
}

interface CPXBannerViewHandlerListener {
    fun onImageTapped()
}