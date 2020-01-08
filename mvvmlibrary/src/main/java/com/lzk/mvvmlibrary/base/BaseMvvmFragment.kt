package cn.lae.mvvm.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

abstract class BaseMvvmFragment<V : ViewDataBinding, VM : BaseViewModel<*>> : BaseFragment() {


    override fun showNoDataView(show: Boolean) {
    }

    override fun showTransLoadingView(show: Boolean) {
    }

    override fun showNetWorkErrView(show: Boolean) {
    }


    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    private var viewModelId: Int = 0

    override fun initConentView(root: ViewGroup) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), onBindLayout, root, true)
        initViewModel()
        initViewObservable()
    }

    private fun initViewModel() {
        mViewModel = createViewModel()
        viewModelId = onBindVariableId
        mBinding.setVariable(viewModelId, mViewModel)
        lifecycle.addObserver(mViewModel)
    }

    private fun createViewModel(): VM {
        return ViewModelProviders.of(this, onBindViewModelFactory).get(onBindViewModel)
    }

    open fun initViewObservable() {
        mViewModel.uc().getLoadingEvent().observe(this,
            Observer<Boolean> { show -> showInitLoadView(show) })
    }

    abstract val onBindViewModel: Class<VM>
    abstract val onBindViewModelFactory: ViewModelProvider.Factory
    abstract val onBindVariableId: Int

}