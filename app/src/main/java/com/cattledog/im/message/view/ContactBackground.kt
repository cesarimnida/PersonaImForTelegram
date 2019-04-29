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
    private val stdWidth = 488f
    private val stdHeight = 166f
    private var width = 0.0f
    private var height = 0.0f
    private var scaleX = 0.0f
    private var scaleY = 0.0f
    private var dif1 = 0.0f
    private var dif2 = 0.0f
    override fun onDraw(shape: Shape?, canvas: Canvas?, paint: Paint?) {
        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.color = Color.WHITE
        whitePaint.style = Paint.Style.FILL
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = Color.BLACK
        whitePaint.style = Paint.Style.FILL
        calculateValues()
        val whiteSquareLocations = buildWhiteSquareLocations()
        buildWhiteShape(whiteSquareLocations).draw(canvas, whitePaint)
        val whitePointerLocations = buildWhitePointerLocations()
        buildWhiteShape(whitePointerLocations).draw(canvas, whitePaint)
        buildBlackShape(whiteSquareLocations, buildBlackSquareDifferences()).draw(canvas, blackPaint)
        buildBlackShape(whitePointerLocations, buildBlackPointerDifferences()).draw(canvas, blackPaint)
    }

    private fun calculateValues() {
        width = shape.width
        height = shape.height
        scaleX = width / stdWidth
        scaleY = height / stdHeight
        val lastPoint = Pair(24f, height - (stdHeight - 141f))
        val firstPoint = Pair(58f, 10f)
        dif1 = 43f - xLineLocation(firstPoint, lastPoint, 68.2f)
        dif2 = 33.8f - xLineLocation(firstPoint, lastPoint, 103.7f)
    }

    private fun buildWhiteShape(locations: ArrayList<Pair<Float, Float>>): PathShape {
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

    private fun buildBlackShape(
        whiteLocations: ArrayList<Pair<Float, Float>>,
        blackDifferences: ArrayList<Pair<Float, Float>>
    ): PathShape {
        val path = Path()
        val firstLocation = whiteLocations[0]
        val firstDifference = blackDifferences[0]
        path.moveTo(firstLocation.first + firstDifference.first, firstLocation.second + firstDifference.second)
        for (i in 1 until whiteLocations.size) {
            val location = whiteLocations[i]
            val difference = blackDifferences[i]
            path.lineTo(location.first + difference.first, location.second + difference.second)
        }
        path.close()
        val shape = PathShape(path, stdWidth, stdHeight)
        shape.resize(stdWidth, stdHeight)
        return shape
    }

    private fun buildWhiteSquareLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(58f + dif1, 10f))
        locations.add(Pair(24f + dif2, height - (stdHeight - 141f)))
        locations.add(Pair(width - scaleX * (stdWidth - 440f), height - (stdHeight - 166f)))
        locations.add(Pair(width - scaleX * (stdWidth - 488f), scaleY * 0f))
        return locations
    }

    private fun buildWhitePointerLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(43f - dif1, 68.2f))
        locations.add(Pair(36f, 72.3f))
        locations.add(Pair(34f, 62.3f))
        locations.add(Pair(0f, 94.3f))
        locations.add(Pair(19f, 92.3f))
        locations.add(Pair(21f, 108.3f))
        locations.add(Pair(33.8f - dif2, 103.7f))
        return locations
    }

    private fun xLineLocation(p1: Pair<Float, Float>, p2: Pair<Float, Float>, y: Float): Float {
        return p1.first + ((y - p1.second) * (p2.first - p1.first)) / (p2.second - p1.second)
    }

    private fun buildBlackSquareDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(60f - 58f, 20.1f - 10.1f))
        differences.add(Pair(34f - 24f, 133.1f - 141.1f))
        differences.add(Pair(437 - 440f, 159.1f - 166.1f))
        differences.add(Pair(464 - 488f, 5.1f - 0.1f))
        return differences
    }

    private fun buildBlackPointerDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(47.8f - 43f, 73.2f - 68.2f))
        differences.add(Pair(34f - 36f, 78.1f - 72.3f))
        differences.add(Pair(31f - 34f, 69.1f - 62.3f))
        differences.add(Pair(9f - 0f, 89.1f - 94.3f))
        differences.add(Pair(23f - 19f, 84.1f - 92.3f))
        differences.add(Pair(25f - 21f, 97.1f - 108.3f))
        differences.add(Pair(43.9f - 33.8f, 90.2f - 103.7f))
        return differences
    }

}