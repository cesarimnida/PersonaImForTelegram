package com.cattledog.im.message.view

import android.graphics.Color
import android.graphics.Paint

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/04/2019
 * ************************************************************
 */
class UserBackground : MessageBackground() {
    override val stdWidth = 479f
    override val stdHeight = 102f

    override fun calculateDifs() {
        val firstPoint = Pair(width - (stdWidth - 411.5f), 6f)
        val lastPoint = Pair(width - (stdWidth - 460f), height - (stdHeight - 90f))
        dif1 = 43f - xLineLocation(firstPoint, lastPoint, 68.2f)
        dif2 = 33.8f - xLineLocation(firstPoint, lastPoint, 103.7f)
    }

    override fun mainPaint(): Paint {
        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.color = Color.WHITE
        whitePaint.style = Paint.Style.FILL
        return whitePaint
    }

    override fun outlinePaint(): Paint {
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blackPaint.color = Color.BLACK
        blackPaint.style = Paint.Style.FILL
        return blackPaint
    }

    override fun buildOutlineSquareLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(width - (stdWidth - 411.5f), 6f))
        locations.add(Pair(width - (stdWidth - 460f), height - (stdHeight - 90f)))
        locations.add(Pair(71f, height - (stdHeight - 101.6f)))
        locations.add(Pair(0.5f, 0.6f))
        return locations
    }

    override fun buildMainSquareDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(407.0f - 411.5f, 17.1f - 6.1f))
        differences.add(Pair(448.0f - 460.0f, 79.6f - 90.1f))
        differences.add(Pair(76.0f - 71.0f, 97.1f - 101.6f))
        differences.add(Pair(25.0f - 0.5f, 9.6f - 0.6f))
        return differences
    }

    override fun buildPointerOutlineLocations(): ArrayList<Pair<Float, Float>> {
        val locations = ArrayList<Pair<Float, Float>>()
        locations.add(Pair(width - (stdWidth - 390.0f), 7.1f))
        locations.add(Pair(width - (stdWidth - 441.0f), 46.1f))
        locations.add(Pair(width - (stdWidth - 441.5f), 29.6f))
        locations.add(Pair(width - (stdWidth - 478.0f), 63.6f))
        locations.add(Pair(width - (stdWidth - 455.5f), 56.1f))
        locations.add(Pair(width - (stdWidth - 454.7f), 73.8f))
        locations.add(Pair(width - (stdWidth - 322.8f), 28.8f))
        return locations
    }

    override fun buildPointerMainDifferences(): ArrayList<Pair<Float, Float>> {
        val differences = ArrayList<Pair<Float, Float>>()
        differences.add(Pair(355.5f - 390.0f, 17.6f - 7.1f))
        differences.add(Pair(443.0f - 441.0f, 50.1f - 46.1f))
        differences.add(Pair(446.5f - 441.5f, 38.1f - 29.6f))
        differences.add(Pair(472.0f - 478.0f, 59.6f - 63.6f))
        differences.add(Pair(453.0f - 455.5f, 52.1f - 56.1f))
        differences.add(Pair(452.5f - 454.7f, 69.1f - 73.8f))
        differences.add(Pair(330.0f - 322.8f, 32.1f - 28.8f))
        return differences
    }
}