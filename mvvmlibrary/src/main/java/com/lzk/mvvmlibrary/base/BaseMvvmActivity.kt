package cn.lae.mvvm.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

abstract class BaseMvvmActivity<V : ViewDataBinding, VM : BaseViewModel<*>> : BaseActivity() {
    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    private var viewModelId: Int? = null

    override fun initContentView() {
        initViewDataBinding()
        initViewObservable()
    }

    open fun initViewObservable() {
        mViewModel.uc().getLoadingEvent().observe(this,
            Observer<Boolean> { show -> showInitLoadView(show) })
    }

    private fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, onBindLayout)
        viewModelId = onBindVariableId
        mViewModel = createViewModel()
        mBinding.setVariable(viewModelId!!, mViewModel)
        lifecycle.addObserver(mViewModel)
    }

    private fun createViewModel(): VM {
        return ViewModelProviders.of(this, onBindViewModelFactory).get(onBindViewModel)
    }


    abstract val onBindViewModel: Class<VM>
    abstract val onBindViewModelFactory: ViewModelProvider.Factory?
    abstract val onBindVariableId: Int
}