package cn.lae.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.RelativeLayout
import com.lzk.mvvmlibrary.R
import com.lzk.mvvmlibrary.view.LoadingInitView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BaseFragment : RxFragment(), IBaseView {

    protected  val TAG = BaseFragment::class.java.simpleName
    protected lateinit var mActivity: RxAppCompatActivity
    protected lateinit var mView: View
    protected lateinit var mViewStubContent: RelativeLayout
    private var isViewCreated = false
    private var isViewVisable = false
    final override lateinit var mContext: Context

    private lateinit var mViewStubInitLoading: ViewStub

    private var mLoadingInitView: LoadingInitView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as RxAppCompatActivity
        mContext = this.context!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_root, container, false)
        initCommonView(mView)
        return mView
    }

    fun initCommonView(view: View) {
        mViewStubContent = view.findViewById(R.id.view_stub_content)
        mViewStubInitLoading = view.findViewById(R.id.view_stub_init_loading)

        initConentView(mViewStubContent)
    }

    open fun initConentView(root: ViewGroup) {
        LayoutInflater.from(mActivity).inflate(onBindLayout, root, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        //如果启用了懒加载就进行懒加载，否则就进行预加载
        if (enableLazyData()) {
            lazyLoad()
        } else {
            initView(mView)
            initListener()
            initData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewVisable = isVisibleToUser
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad()
        }
    }

    private fun lazyLoad() {
        //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isViewVisable) {
            initView(mView)
            initListener()
            initData()
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isViewVisable = false
        }
    }

    override fun finishActivity() {
        mActivity.finish()
    }

    //默认不启用懒加载
    fun enableLazyData(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
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

    abstract val onBindLayout: Int
    abstract fun initView(view: View)
    abstract fun initData()
    fun initListener() {}
}

