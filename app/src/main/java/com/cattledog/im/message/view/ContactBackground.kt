package com.cattledog.im.message.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 09/04/2019
 * ************************************************************
 */
class ContactBackground : ShapeDrawable(RectShape()) {
    private val mStdWidth = 200f
    private val mStdHeight = 68f
    override fun onDraw(shape: Shape?, canvas: Canvas?, paint: Paint?) {
        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.color = Color.WHITE
        whitePaint.style = Paint.Style.FILL
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = Color.BLACK
        whitePaint.style = Paint.Style.FILL
        buildWhiteShape().draw(canvas, whitePaint)
        buildBlackShape().draw(canvas, blackPaint)
    }

    private fun buildWhiteShape(): PathShape {
        val path = Path()
        val width = shape.width
        val height = shape.height
        val mScaleX = width / mStdWidth
        val mScaleY = height / mStdHeight
        path.moveTo(23.7f, 4.2f)
        path.lineTo(mScaleX * 9.8f, height - mScaleY * 10.2f)
        path.lineTo(width - mScaleX * 20f, height)
        path.lineTo(width - mScaleX * 0.4f, mScaleY * 0.1f)
        path.close()
        val shape = PathShape(path, mStdWidth, mStdHeight)
        shape.resize(mStdWidth, mStdHeight)
        return shape
    }

    private fun buildBlackShape(): PathShape {
        val path = Path()
        val width = shape.width
        val height = shape.height
        val mScaleX = width / mStdWidth
        val mScaleY = height / mStdHeight
        path.moveTo(25.5f, 8.3f)
        path.lineTo(mScaleX * 13.9f, height - mScaleY * 13.5f)
        path.lineTo(width - mScaleX * 21.3f, height - mScaleY * 2.9f)
        path.lineTo(width - mScaleX * 10.2f, mScaleY * 2.2f)
        path.close()
        val shape = PathShape(path, mStdWidth, mStdHeight)
        shape.resize(mStdWidth, mStdHeight)
        return shape
    }

}