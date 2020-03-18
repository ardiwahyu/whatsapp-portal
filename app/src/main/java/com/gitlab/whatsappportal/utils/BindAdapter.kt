package com.gitlab.whatsappportal.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.R
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

object BindAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic fun imageUrl(imageView: ImageView, string: String){
        val requestBuilder = GlideApp.with(imageView.context)
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        val uri = Uri.parse(string)
        requestBuilder.load(uri).into(imageView)
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("setCode")
    @JvmStatic fun setCode(textView: TextView, string: String){
        if (string.trim().isNotEmpty()){
            textView.visibility = View.VISIBLE
            textView.text = "+$string"
        }else{
            textView.visibility = View.GONE
        }
    }
}