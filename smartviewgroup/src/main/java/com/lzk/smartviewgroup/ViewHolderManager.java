package com.lzk.smartviewgroup;

import android.view.View;

public abstract class ViewHolderManager<T> implements ViewTouchInner {
    protected abstract void onBindViewHolder(View view, T t, SmartViewGroup smartViewGroup);

    protected abstract int getItemLayoutId();

}
