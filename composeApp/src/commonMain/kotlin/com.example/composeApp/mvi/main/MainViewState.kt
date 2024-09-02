package com.example.composeApp.mvi.main


import androidx.compose.ui.graphics.ImageBitmap
import com.example.composeApp.enums.Ratio
import com.example.composeApp.mvi.State

data class MainViewState(
    val image: ImageBitmap? = null,
    val cropImage: ImageBitmap? = null,
    val isCropMode: Boolean = false,
    val ratio: Ratio = Ratio.RATIO_16_9
): State {
    fun getImageAspectRatio() = if (image == null) {
        throw IllegalStateException("Image is null")
    }else {
        image.width.toFloat() / image.height.toFloat()
    }
}
