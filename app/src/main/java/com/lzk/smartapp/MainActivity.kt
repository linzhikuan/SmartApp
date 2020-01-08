package com.lzk.smartapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.lzk.photolibrary.product.SouceProduct
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity(), View.OnClickListener {
    @BindView(id = R.id.txt_test, click = true)
    private val textView: TextView? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    fun addSubscribe(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    fun onCleared() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AutoWiredProcess.initBindView(this)

        addSubscribe(SouceProduct(this).loadPhotoData().subscribe({
            Log.d("lzk", "成功获取" + "__" + Thread.currentThread().name)
        }, {
            Log.d("lzk", "失败获取__" + it.message)
        }))
    }

    override fun onClick(p0: View?) {
        Log.d("lzk", "onClick__" + p0?.id)
    }
}
