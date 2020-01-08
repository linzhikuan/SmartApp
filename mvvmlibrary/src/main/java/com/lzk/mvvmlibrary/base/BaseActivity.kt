package cn.lae.mvvm.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import cn.lae.mvvm.setData
import com.lzk.mvvmlibrary.R
import com.lzk.mvvmlibrary.StatusBarUtil
import com.lzk.mvvmlibrary.view.LoadingInitView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.common_toolbar.*

abstract class BaseActivity : RxAppCompatActivity(), IBaseView {

    override fun finishActivity() {
        finish()
    }

    override fun showInitLoadView(show: Boolean) {

        if (mLoadingInitView == null) {
            val view = mViewStubInitLoading.inflate()
            mLoadingInitView = view.findViewById<View>(R.id.view_init_loading) as LoadingInitView
        }
        mLoadingInitView!!.setOnClickListener(if (show) View.OnClickListener { } else null)
        mLoadingInitView!!.visibility = if (show) View.VISIBLE else View.GONE
        mLoadingInitView!!.loading(show)
    }

    override fun showNoDataView(show: Boolean) {
    }

    override fun showTransLoadingView(show: Boolean) {
    }

    override fun showNetWorkErrView(show: Boolean) {
    }

    final override lateinit var mContext: Context

    private lateinit var mContentView: ViewGroup

    private var mViewStubContent: RelativeLayout? = null

    private lateinit var mViewStubInitLoading: ViewStub

    private var mLoadingInitView: LoadingInitView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
        mContext = this
        // 全部竖屏显示
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.setContentView(R.layout.activity_root)
        mContentView = findViewById<View>(android.R.id.content) as ViewGroup
        initCommonView()
        initContentView()
        initView()
        initListener()
        initData()
    }

    private fun initCommonView() {
        mViewStubContent = findViewById<View>(R.id.view_stub_content) as RelativeLayout
        mViewStubInitLoading = findViewById(R.id.view_stub_init_loading)


        if (enableToolbar()) {
            view_stub_toolbar.layoutResource = onBindToolbarLayout()
            view_stub_toolbar.inflate()
            toolbar_root?.run {
                setSupportActionBar(this)
                supportActionBar!!.setDisplayShowTitleEnabled(false)
                initToolbarListener(this)
            }
        }

    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        if (mViewStubContent != null)
            initContentView(layoutResID)
    }

    open fun initContentView() {
        initContentView(onBindLayout)
    }


    open fun initToolbarListener(toolbar: Toolbar) {
        val btn1 = toolbar.findViewById<View>(R.id.toolbar_btn_1)
        val btn4 = toolbar.findViewById<View>(R.id.toolbar_btn_4)
        val btnTitle = toolbar.findViewById<View>(R.id.toolbar_title)
        btn1?.setData(toolbarBtn1Value) { toolbarBtn1ClickListener(it) }
        btn4?.setData(toolbarBtn4Value)
        btnTitle?.setData(toolbarTitle)
    }

    open fun updateToolbar() {
        initToolbarListener(toolbar_root!!)
    }

    private fun initContentView(@LayoutRes layoutResID: Int?) {
        val view = LayoutInflater.from(this).inflate(layoutResID!!, mViewStubContent, false)
        mViewStubContent!!.id = android.R.id.content
        mContentView.id = View.NO_ID
        mViewStubContent!!.removeAllViews()
        mViewStubContent!!.addView(view)
    }

    open var toolbarBtn1Value: Int = 0
    open var toolbarBtn2Value: Int = 0
    open var toolbarBtn3Value: Int = 0
    open var toolbarBtn4Value: Int = 0
    open var toolbarTitle: Int = 0
    open var toolbarBtn1ClickListener: (view: View) -> Unit = { finishActivity() }

    abstract val onBindLayout: Int
    abstract fun initView()
    abstract fun initData()
    abstract fun initListener()

    open fun enableToolbar(): Boolean {
        return true
    }

    open fun onBindToolbarLayout(): Int {
        return R.layout.common_toolbar
    }


}