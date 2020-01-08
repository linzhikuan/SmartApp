package cn.lae.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.lae.mvvm.base.BaseViewModel
import com.safframework.ext.clickWithTrigger
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun RxAppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun RxAppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

inline fun <reified F : Fragment> Context.newFragment(vararg args: Pair<String, String>): F {
    val bundle = Bundle()
    args.let {
        for (arg in args) {
            bundle.putString(arg.first, arg.second)
        }
    }
    return Fragment.instantiate(this, F::class.java.name, bundle) as F
}


@Suppress("UNCHECKED_CAST")
class CreateVPF(var clazz: BaseViewModel<*>) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return clazz as T
    }
}


fun View.setData(value: Int, click: (view: View) -> Unit = {}): View {
    when (this) {
        is TextView -> if (value != 0) setText(value)
        is ImageView -> setImageResource(value)
    }

    clickWithTrigger { view -> click(view) }
    return this
}

infix fun ViewGroup.inflate(layoutResId: Int): View =
    LayoutInflater.from(context).inflate(layoutResId, this, false)





