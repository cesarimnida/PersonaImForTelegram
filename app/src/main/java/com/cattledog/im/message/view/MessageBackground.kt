package com.cattledog.im.message.view

import android.graphics.Canvas
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
abstract class MessageBackground : ShapeDrawable(RectShape()) {
    protected open val stdWidth = 488f
    protected open val stdHeight = 166f
    protected var width = 0.0f
    protected var height = 0.0f
    protected var scaleX = 0.0f
    protected var scaleY = 0.0f
    protected var dif1 = 0.0f
    protected var dif2 = 0.0f
    override fun onDraw(shape: Shape?, canvas: Canvas?, paint: Paint?) {
        val outlinePaint = outlinePaint()
        val mainPaint = mainPaint()
        calculateValues()
        val outlineSquareLocations = buildOutlineSquareLocations()
        buildOutlineShape(outlineSquareLocations).draw(canvas, outlinePaint)
        val pointerOutlineLocations = buildPointerOutlineLocations()
        buildOutlineShape(pointerOutlineLocations).draw(canvas, outlinePaint)
        buildMainShape(outlineSquareLocations, buildMainSquareDifferences()).draw(canvas, mainPaint)
        buildMainShape(pointerOutlineLocations, buildPointerMainDifferences()).draw(canvas, mainPaint)
    }

    private fun calculateValues() {
        width = shape.width
        height = shape.height
        scaleX = width / stdWidth
        scaleY = height / stdHeight
        calculateDifs()
    }

    private fun buildOutlineShape(locations: ArrayList<Pair<Float, Float>>): PathShape {
        val path = Path()
        val firstLocation = locations[0]
        path.moveTo(firstLocation.first, firstLocation.second)
        for (i in 1 until locations.size) {
            val location = locations[i]
            path.lineTo(location.first, location.second)
        }
        path.close()
        val shape = PathShape(path, stdWidth, stdHeight)
        shape.resize(stdWidth, stdHeight)
        return shape
    }

    private fun buildMainShape(
        outlineLocations: ArrayList<Pair<Float, Float>>,
        mainDifferences: ArrayList<Pair<Float, Float>>
    ): PathShape {
        val path = Path()
        val firstLocation = outlineLocations[0]
        val firstDifference = mainDifferences[0]
        path.moveTo(firstLocation.first + firstDifference.first, firstLocation.second + firstDifference.second)
        for (i in 1 until outlineLocations.size) {
            val location = outlineLocations[i]
            val difference = mainDifferences[i]
            path.lineTo(location.first + difference.first, location.second + difference.second)
        }
        path.close()
        val shape = PathShape(path, stdWidth, stdHeight)
        shape.resize(stdWidth, stdHeight)
        return shape
    }

    protected fun xLineLocation(p1: Pair<Float, Float>, p2: Pair<Float, Float>, y: Float): Float {
        return p1.first + ((y - p1.second) * (p2.first - p1.first)) / (p2.second - p1.second)
    }

    abstract fun calculateDifs()
    abstract fun mainPaint(): Paint
    abstract fun outlinePaint(): Paint
    abstract fun buildOutlineSquareLocations(): ArrayList<Pair<Float, Float>>
    abstract fun buildPointerOutlineLocations(): ArrayList<Pair<Float, Float>>
    abstract fun buildMainSquareDifferences(): ArrayList<Pair<Float, Float>>
    abstract fun buildPointerMainDifferences(): ArrayList<Pair<Float, Float>>
}