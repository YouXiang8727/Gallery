package com.example.composeApp.ui.main

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.mvi.main.MainViewModel
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

@Composable
fun PickImageButton(
    mainViewModel: MainViewModel = viewModel { MainViewModel() }
) {
    val scope = rememberCoroutineScope()
    val multipleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                mainViewModel.pickImage(it)
            }
        }
    )

    Button(
        onClick = {
            multipleImagePicker.launch()
        }
    ) {
        Text("Pick from gallery")
    }
}