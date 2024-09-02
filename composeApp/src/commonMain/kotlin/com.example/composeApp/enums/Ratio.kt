package com.example.composeApp.enums

enum class Ratio(val aspectRatio: Float, val showText: String) {
    RATIO_1_1(1f, "1:1"),
    RATIO_3_2(3f / 2f, "3:2"),
    RATIO_2_3(2f / 3f, "2:3"),
    RATIO_3_4(3f / 4f, "3:4"),
    RATIO_4_3(4f / 3f, "4:3"),
    RATIO_4_5(4f / 5f, "4:5"),
    RATIO_16_9(16f / 9f, "16:9")
}