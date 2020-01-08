package cn.lae.mvvm.base

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cn.lae.mvvm.event.SingleLiveEvent
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

open class BaseViewModel<M : BaseModel> constructor(var mModel: M, var context: Context) :
    ViewModel(), IBaseViewModel, Consumer<Disposable> {

    private var uiControl: UIChangeLiveData? = null

    fun uc(): UIChangeLiveData {
        if (uiControl == null)
            uiControl = UIChangeLiveData()
        return uiControl!!
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun accept(t: Disposable?) {
        t?.let {
            mModel.addSubscribe(it)
        }

    }


    override fun onCleared() {
        super.onCleared()
        mModel.onCleared()
    }


    inner class UIChangeLiveData {
        private var showInitLoadViewEvent: SingleLiveEvent<Boolean>? = null
        fun getLoadingEvent(): SingleLiveEvent<Boolean> {
            if (showInitLoadViewEvent == null)
                showInitLoadViewEvent = SingleLiveEvent()
            return showInitLoadViewEvent!!
        }
    }

    fun Disposable.addSub() {
        mModel.addSubscribe(this)
    }

}
