package com.graph.wifi.signal.util

/**
 * Color generate by randomly and use it easily
 * @param bgColorOfGraphs String you can set like FFFFFF hex digits.
 * This is to check for the absence of a graphic that is the same color as the canvas background color.
 * @param alphaPrefix set alpha of generated color for WiFi graph
 */
class ColorGenerator(
    private val bgColorOfGraphs: String = "FFFFFF",
    private val alphaPrefix: String = "#55"
) {
    private val _colorList = mutableListOf<String>()

    //val colorList: List<String> get() = _colorList
    private val charList = listOf(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
    )

    /**
     * Generate random color
     */
    fun generateColor(): String {
        val stringBuilder = StringBuilder(alphaPrefix)
        var isAdded = false
        while (!isAdded) {
            for (i in 0..5) {
                stringBuilder.append(charList.random())
            }
            if (!_colorList.contains(stringBuilder.toString()) || !stringBuilder.endsWith(
                    bgColorOfGraphs
                )
            ) {
                isAdded = true
            } else {
                stringBuilder.setLength(0)
                stringBuilder.append(alphaPrefix)
            }
        }
        return stringBuilder.toString()
    }
}