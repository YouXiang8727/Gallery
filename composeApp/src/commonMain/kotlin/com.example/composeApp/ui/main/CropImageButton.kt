package com.example.composeApp.ui.main

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.mvi.main.MainViewModel

@Composable
fun CropImageButton(
    mainViewModel: MainViewModel = viewModel { MainViewModel() }
) {
    Button(
        onClick = {
            mainViewModel.startCropImage()
        },
        enabled = mainViewModel.state.image != null
    ) {
        Text("Crop Image")
    }
}