package com.example.composeApp.mvi.main

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.example.composeApp.enums.Ratio
import com.example.composeApp.mvi.MviViewModel
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch

class MainViewModel(
) : MviViewModel<MainViewState, MainViewAction>(
    reducer = MainViewReducer(),
    initialState = MainViewState()
) {
    fun pickImage(
        byteArray: ByteArray
    ) {
        dispatch(
            MainViewAction.PickImage(byteArray)
        )
    }

    fun startCropImage() {
        dispatch(
            MainViewAction.StartCropImage
        )
    }

    fun cropImage(
        imageBitmap: ImageBitmap?
    ) {
        dispatch(
            MainViewAction.CropImage(imageBitmap)
        )
    }

    fun endCropImage() {
        dispatch(
            MainViewAction.EndCropImage
        )
    }

    fun selectCropRatio(
        ratio: Ratio
    ) {
        dispatch(
            MainViewAction.SelectCropRatio(ratio)
        )
    }
}