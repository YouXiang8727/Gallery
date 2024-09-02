package com.example.composeApp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.mvi.main.MainViewModel
import com.github.panpf.zoomimage.compose.rememberZoomState

@Composable
fun MainView() {
    val mainViewModel: MainViewModel = viewModel { MainViewModel() }
    val zoomState = rememberZoomState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn (
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        PickImageButton()
                        CropImageButton()
                    }
                }

                mainViewModel.state.cropImage?.let { imageBitmap ->
                    item {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            bitmap = imageBitmap,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }

            if (mainViewModel.state.isCropMode) {
                CropImageDialog(
                    zoomState = zoomState
                )
            }
        }
    }
}