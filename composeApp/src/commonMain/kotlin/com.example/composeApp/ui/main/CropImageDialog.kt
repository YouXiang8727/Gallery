package com.example.composeApp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.enums.Ratio
import com.example.composeApp.mvi.main.MainViewModel
import com.github.panpf.zoomimage.ZoomImage
import com.github.panpf.zoomimage.compose.ZoomState
import kotlin.math.abs

@Composable
fun CropImageDialog(
    mainViewModel: MainViewModel = viewModel { MainViewModel() },
    zoomState: ZoomState
) {
    val cropAspectRatio = mainViewModel.state.ratio

    val contentScale = remember(cropAspectRatio) {
        if (mainViewModel.state.getImageAspectRatio() < cropAspectRatio.aspectRatio) {
            ContentScale.FillWidth
        }else {
            ContentScale.FillHeight
        }
    }

    Dialog(
        onDismissRequest = {
            mainViewModel.endCropImage()
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            mainViewModel.state.image?.let { imageBitmap ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    ZoomImage(
                        painter = BitmapPainter(image = imageBitmap),
                        contentDescription = null,
                        contentScale = contentScale,
                        zoomState = zoomState,
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(cropAspectRatio.aspectRatio)
                            .background(Color.Black)
                            .cropBox(cropAspectRatio)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                mainViewModel.selectCropRatio(
                                    Ratio.entries.getOrElse(
                                        Ratio.entries.indexOf(cropAspectRatio) + 1
                                    ) {
                                        Ratio.entries.first()
                                    }
                                )
                            },
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Text(
                                text = cropAspectRatio.showText,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.clip(RoundedCornerShape(4.dp))
                            )
                        }

                        TextButton(
                            modifier = Modifier
                                .padding(8.dp),
                            onClick = {
                                val scaledImage = imageBitmap.cropImage(zoomState)
                                mainViewModel.cropImage(scaledImage)
                                mainViewModel.endCropImage()
                            }
                        ) {
                            Text(
                                text = "Crop",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.clip(RoundedCornerShape(4.dp))
                            )
                        }
                    }
                    Text(
                        text = "contentDisplayRect ${zoomState.zoomable.contentDisplayRect.left}, " +
                                "${zoomState.zoomable.contentDisplayRect.top}, " +
                                "${zoomState.zoomable.contentDisplayRect.right}, " +
                                "${zoomState.zoomable.contentDisplayRect.bottom}",
                        maxLines = 1,
                        color = Color.White
                    )
                    Text(
                        text = "contentSize ${zoomState.zoomable.contentSize}",
                        color = Color.White
                    )
                    Text(
                        text = "containerSize ${zoomState.zoomable.containerSize}",
                        color = Color.White
                    )
                    Text(
                        text = "${zoomState.zoomable.transform.scale}",
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun Modifier.cropBox(
    ratio: Ratio,
    boxColor: Color = Color.White, // 裁剪框的颜色
    boxWidth: Dp = 2.dp // 裁剪框的宽度
) = drawWithContent {
    drawContent()
    drawCropBox(ratio, boxColor, boxWidth)
}

private fun DrawScope.drawCropBox(
    ratio: Ratio,
    boxColor: Color,
    boxWidth: Dp
) {
    val height = size.width / ratio.aspectRatio
    val offset = Offset(
        0f,
        (size.height - height) / 2
    )

    drawRect(
        color = boxColor,
        topLeft = offset,
        size = Size(size.width, height),
        style = Stroke(boxWidth.toPx())
    )

    for (i in 1 until 3) {
        drawLine(
            color = Color.White,
            start = Offset(size.width * i / 3, 0f),
            end = Offset(size.width * i / 3, size.height),
            strokeWidth = 1f
        )

        drawLine(
            color = Color.White,
            start = Offset(0f, size.height * i / 3),
            end = Offset(size.width, size.height * i / 3),
            strokeWidth = 1f
        )
    }
}

private fun ImageBitmap.cropImage(
    offset: IntOffset,
    width: Int,
    height: Int
): ImageBitmap {
    println("cropImage(offset = $offset, width = $width, height = $height)")
    // Create a new ImageBitmap with the cropped dimensions
    val croppedBitmap = ImageBitmap(width, height)

    // Draw the cropped area onto the new ImageBitmap
    val canvas = Canvas(croppedBitmap)
    canvas.drawImageRect(
        image = this,
        srcOffset = offset,
        srcSize = IntSize(width, height),
        dstSize = IntSize(width, height),
        paint = Paint()
    )

    return croppedBitmap
}

private fun ImageBitmap.cropImage(
    zoomState: ZoomState
): ImageBitmap {
    val displayRect = zoomState.zoomable.contentDisplayRect
    val containerSize = zoomState.zoomable.containerSize
    val scaleX = displayRect.width.toFloat() / this.width.toFloat()
    val scaleY = displayRect.height.toFloat() / this.height.toFloat()
    val offset = IntOffset(
        abs(displayRect.left / scaleX).toInt(),
        abs(displayRect.top / scaleY).toInt()
    )
    val width = (containerSize.width / scaleX).toInt()
    val height = (containerSize.height / scaleY).toInt()
    println("cropImage(offset = $offset, width = $width, height = $height)")
    // Create a new ImageBitmap with the cropped dimensions
    val croppedBitmap = ImageBitmap(width, height)

    // Draw the cropped area onto the new ImageBitmap
    val canvas = Canvas(croppedBitmap)
    canvas.drawImageRect(
        image = this,
        srcOffset = offset,
        srcSize = IntSize(width, height),
        dstSize = IntSize(width, height),
        paint = Paint()
    )

    return croppedBitmap
}