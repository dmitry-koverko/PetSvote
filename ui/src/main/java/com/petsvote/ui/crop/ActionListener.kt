package com.petsvote.ui.crop

internal interface ActionListener {

    fun onScaled(scale: Float)
    fun onScaleEnded()
    fun onMoved(dx: Float, dy: Float)
    fun onFlinged(velocityX: Float, velocityY: Float)
    fun onMoveEnded()
}