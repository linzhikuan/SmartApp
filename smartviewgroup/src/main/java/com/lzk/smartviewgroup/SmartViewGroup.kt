package com.lzk.smartviewgroup

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull


class SmartViewGroup : ViewGroup {
    val TAG = "SmartViewGroup"
    private val itemTypeManager: ItemTypeManager = ItemTypeManager()
    private val viewMap = HashMap<View, Any>()
    private var touchView: View? = null
    private var viewTouchInner: ViewTouchInner? = null
    private var touchViewManager: ViewHolderManager<Any>? = null
    private var enbleAreaInnerBase: BaseViewEnbleAreaInner? = null
    private val thisView = this

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun myLog(msg: String) {
        Log.i(TAG, msg)
    }

    fun <T> register(@NonNull cls: Class<T>, @NonNull manager: ViewHolderManager<T>) {
        itemTypeManager.register(cls, manager)
    }


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view.visibility != View.GONE) {
                val left = view.left
                val top = view.top
                val right = left + view.measuredWidth
                val bottom = top + view.measuredHeight
                myLog("onLayout__" + left + "__" + top + "__" + right + "__" + bottom + "__" + view.left + "__" + view.top)
                view.layout(left, top, right, bottom)
                if (view.measuredWidth == 0 && view.measuredHeight == 0) {
                    val viewHolderManager = itemTypeManager.getViewHolderManager(viewMap[view])
                    viewHolderManager.onBindViewHolder(view, viewMap[view], thisView)
                } else if (view.tag == null) {
                    view.tag = "created"
                    val viewHolderManager = itemTypeManager.getViewHolderManager(viewMap[view])
                    viewHolderManager.onBindViewHolder(view, viewMap[view], thisView)
                }


            }
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            val groupWidth = getMaxWidth()
            val groupHeight = getTotalHeight()

            setMeasuredDimension(groupWidth, groupHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getMaxWidth(), height)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, getTotalHeight())
        }
    }

    private fun getTotalHeight(): Int {
        return 0
    }

    private fun getMaxWidth(): Int {
        return 100
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val x: Float = ev?.x ?: 0f
        val y: Float = ev?.y ?: 0f
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchView = null
                touchView = getTouchView(x, y)
                touchViewManager = itemTypeManager.getViewHolderManager(viewMap[touchView])
                viewTouchInner =
                    if (touchViewManager != null && touchViewManager is ViewTouchInner) {
                        touchViewManager as ViewTouchInner
                    } else null
                viewTouchInner?.onClick(this, x, y, touchView!!)
            }
            MotionEvent.ACTION_MOVE -> {
//                myLog("onMove__" + x + "__" + y)
                viewTouchInner?.onMove(this, x, y, touchView!!)
            }
            MotionEvent.ACTION_UP -> {
                viewTouchInner?.onActionUp(this, x, y, touchView!!)
            }
            MotionEvent.ACTION_CANCEL -> {
                viewTouchInner?.onActionCancle(this, x, y, touchView!!)
            }
        }
        return viewTouchInner?.run {
            isIntercept(
                thisView,
                x,
                y,
                touchView!!
            ) || enbleAreaInnerBase?.inarea(x, y) ?: true
        }
            ?: super.dispatchTouchEvent(ev)
    }

    fun getTouchManager(view: View): ViewHolderManager<Any>? {
        return itemTypeManager.getViewHolderManager(viewMap[view])
    }

    fun getTouchManager(x: Float, y: Float): ViewHolderManager<Any>? {
        val view: View? = getTouchView(x, y)
        return itemTypeManager.getViewHolderManager(viewMap[view])
    }

    fun getTouchView(x: Float, y: Float): View? {
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            myLog("getTouchView__" + child.left + "_" + child.top + "_" + child.right + "_" + child.bottom)
            if (touchView != null && touchView == child)
                continue

            if (child.visibility != View.GONE) {
                val rect = Rect(child.left, child.top, child.right, child.bottom)
                if (rect.contains(x.toInt(), y.toInt())) {
//                    myLog("getTouchView__" + child.id + "__" + (if (touchView == null) "" else touchView!!.id.toString()))
                    return child
                }
            }
        }
        return null
    }


    var viewId = 0

    fun addItem(t: Any) {
        val viewHolderManager = itemTypeManager.getViewHolderManager(t)
        viewHolderManager?.let {
            val view = LayoutInflater.from(context).inflate(it.itemLayoutId, null)
            view.id = viewId
            viewId++
            viewMap[view] = t
            addView(view)
        }
    }

    fun setAreaEnbleInner(areaInnerBase: BaseViewEnbleAreaInner) {
        this.enbleAreaInnerBase = areaInnerBase
    }

    fun getAreaEnbleInner(): BaseViewEnbleAreaInner? {
        return enbleAreaInnerBase
    }

}