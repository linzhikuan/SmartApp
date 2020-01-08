package cn.lae.mvvm.base

import android.content.Context

interface IBaseView {
    fun finishActivity()
    fun showInitLoadView(show: Boolean)
    fun showNoDataView(show: Boolean)
    fun showTransLoadingView(show: Boolean)
    fun showNetWorkErrView(show: Boolean)
    /**获取上下文*/
    val mContext: Context
}