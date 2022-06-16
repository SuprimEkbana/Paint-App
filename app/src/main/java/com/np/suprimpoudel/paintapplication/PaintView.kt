package com.np.suprimpoudel.paintapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.np.suprimpoudel.paintapplication.MainActivity.Companion.paintBrush
import com.np.suprimpoudel.paintapplication.MainActivity.Companion.path

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    companion object {
        val pathList = arrayListOf<Path>()
        val colorList = arrayListOf<Int>()
        var currentBrush = Color.BLACK
        var mBitmap: Bitmap? = null
    }

    private var mCanvas: Canvas? = null

    var params: ViewGroup.LayoutParams? = null

    init {
        setup()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
            }
            else -> return false
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas) {
        mCanvas = canvas
        super.onDraw(mCanvas)
        mCanvas?.drawColor(Color.WHITE)
        if (mBitmap != null) {
            canvas?.drawBitmap(mBitmap!!, 0f, 0f, Paint())
            invalidate()
        }
        for (i in pathList.indices) {
            paintBrush.color = colorList[i]
            mCanvas?.drawPath(pathList[i], paintBrush)
            invalidate()
        }
    }

    private fun setup() {
        paintBrush.isAntiAlias = true
        paintBrush.color = currentBrush
        paintBrush.style = Paint.Style.STROKE
        paintBrush.strokeJoin = Paint.Join.ROUND
        paintBrush.strokeWidth = 8f

        params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun drawSomething() {
        draw(mCanvas)
    }
}