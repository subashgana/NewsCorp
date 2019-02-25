package com.newscorp;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomBindings {

    @BindingAdapter("bind:imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).fit().into(imageView);
    }
}
