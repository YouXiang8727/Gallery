package com.example.composeApp.mvi.main

import com.example.composeApp.mvi.Reducer
import com.preat.peekaboo.image.picker.toImageBitmap

class MainViewReducer: Reducer<MainViewState, MainViewAction> {
    override fun reduce(state: MainViewState, action: MainViewAction): MainViewState {
        return when (action) {
            is MainViewAction.PickImage -> {
                val image = action.byteArray.toImageBitmap()
                MainViewState(image, image)
            }

            is MainViewAction.CropImage -> {
                state.copy(
                    cropImage = action.imageBitmap
                )
            }

            MainViewAction.StartCropImage -> {
                state.copy(
                    isCropMode = true
                )
            }

            MainViewAction.EndCropImage -> {
                state.copy(
                    isCropMode = false
                )
            }

            is MainViewAction.SelectCropRatio -> {
                state.copy(
                    ratio = action.ratio
                )
            }
        }
    }
}