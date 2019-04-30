package com.cattledog.im.message.view

import android.graphics.Color
import android.graphics.Paint

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 09/04/2019
 * ************************************************************
 */
class ContactBackground : MessageBackground() {

    override fun calculateDifs() {
        val firstPoint = Pair(58f, 10f)
        val lastPoint = Pair(24f, height - (stdHeight - 141f))
        dif1 = xLineLocation(firstPoint, lastPoint, 68.2f) - xLineLocation(firstPoint, lastPoint, 68.2f)
        dif2 = 33.8f - xLineLocation(firstPoint, lastPoint, 103.7f)
    }

    override fun mainPaint(): Paint {
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = Color.BLACK
        blackPaint.style = Paint.Style.FILL
        return blackPaint
    }

    override fun outlinePaint(): Paint {
        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.color = Color.WHITE
        whitePaint.style = Paint.Style.FILL
        return whitePaint
    }

    override fun buildOutlineSquareLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(58f + dif1, 10f))
        locations.add(Pair(24f + dif2, height - (stdHeight - 141f)))
        locations.add(Pair(width - scaleX * (stdWidth - 440f), height - (stdHeight - 166f)))
        locations.add(Pair(width - scaleX * (stdWidth - 488f), scaleY * 0f))
        return locations
    }

    override fun buildMainSquareDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(60f - 58f, 20.1f - 10.1f))
        differences.add(Pair(34f - 24f, 133.1f - 141.1f))
        differences.add(Pair(437 - 440f, 159.1f - 166.1f))
        differences.add(Pair(464 - 488f, 5.1f - 0.1f))
        return differences
    }

    override fun buildPointerOutlineLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(63f - dif1, 48.2f))
        locations.add(Pair(36f, 72.3f))
        locations.add(Pair(34f, 62.3f))
        locations.add(Pair(0f, 94.3f))
        locations.add(Pair(19f, 92.3f))
        locations.add(Pair(21f, 108.3f))
        locations.add(Pair(53.8f - dif2, 93.7f))
        return locations
    }

    override fun buildPointerMainDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(67.8f - 63f, 53.2f - 48.2f))
        differences.add(Pair(34f - 36f, 78.1f - 72.3f))
        differences.add(Pair(31f - 34f, 69.1f - 62.3f))
        differences.add(Pair(9f - 0f, 89.1f - 94.3f))
        differences.add(Pair(23f - 19f, 84.1f - 92.3f))
        differences.add(Pair(25f - 21f, 97.1f - 108.3f))
        differences.add(Pair(63.9f - 53.8f, 90.2f - 93.7f))
        return differences
    }

}