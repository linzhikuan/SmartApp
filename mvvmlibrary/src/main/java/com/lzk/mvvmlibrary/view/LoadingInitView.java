package com.lzk.mvvmlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzk.mvvmlibrary.R;


public class LoadingInitView extends RelativeLayout {
    //    private final AnimationDrawable mAnimationDrawable;
    private CutomProgressDrawable cutomProgressDrawable;

    public LoadingInitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_init_loading, this);
        ImageView imgLoading = findViewById(R.id.img_init_loading);
//        mAnimationDrawable = (AnimationDrawable) imgLoading.getDrawable();
        cutomProgressDrawable = new CutomProgressDrawable();
        imgLoading.setImageDrawable(cutomProgressDrawable);
    }

    public void startLoading() {
        cutomProgressDrawable.start();
    }

    public void stopLoading() {
        cutomProgressDrawable.stop();
    }

    public void loading(boolean b) {
        if (b) {
            startLoading();
        } else {
            stopLoading();
        }
    }
}
