package com.lzk.smartviewgroup

import android.view.View
import androidx.annotation.Nullable

interface ViewTouchInner {
    fun onClick(@Nullable smartViewGroup: SmartViewGroup, x: Float, y: Float, @Nullable touchView: View)
    fun onMove(@Nullable smartViewGroup: SmartViewGroup, x: Float, y: Float, @Nullable touchView: View)
    fun onActionUp(@Nullable smartViewGroup: SmartViewGroup, x: Float, y: Float, @Nullable touchView: View)
    fun onActionCancle(@Nullable smartViewGroup: SmartViewGroup, x: Float, y: Float, @Nullable touchView: View)
    fun isIntercept(@Nullable smartViewGroup: SmartViewGroup, x: Float, y: Float, @Nullable touchView: View): Boolean
}