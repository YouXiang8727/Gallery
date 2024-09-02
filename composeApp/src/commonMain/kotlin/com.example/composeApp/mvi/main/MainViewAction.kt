package com.example.composeApp.mvi.main

import androidx.compose.ui.graphics.ImageBitmap
import com.example.composeApp.enums.Ratio
import com.example.composeApp.mvi.Action

sealed interface MainViewAction: Action {
    data class PickImage(val byteArray: ByteArray): MainViewAction

    data class CropImage(val imageBitmap: ImageBitmap?): MainViewAction

    data object StartCropImage: MainViewAction

    data object EndCropImage: MainViewAction

    data class SelectCropRatio(val ratio: Ratio): MainViewAction
}
