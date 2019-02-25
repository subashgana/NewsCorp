package com.newscorp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.newscorp.databinding.ImageLayoutBinding;

public class ImagesViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;
    private int width;

    ImagesViewHolder(View itemView, int spans) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        ViewGroup.LayoutParams layoutParams = getBinding().image.getLayoutParams();
        width = getScreenWidth() / spans;
        layoutParams.width = width;
        layoutParams.height = width;
        getBinding().image.setLayoutParams(layoutParams);
    }

    public ImageLayoutBinding getBinding() {
        return ((ImageLayoutBinding) binding);
    }

    private int getScreenWidth() {
        Display display = ((WindowManager) getBinding().image.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getWidth() {
        return width;
    }
}