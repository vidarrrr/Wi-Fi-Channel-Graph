package com.graph.wifi.signal.util

import com.graph.wifi.signal.model.Point

object PointGenerator {
    /**
     * Generate Start, Peak Start, Peak End, End points
     * Usually the shape is trapezoidal
     * Example: SSID with peak 2417 Mhz is channel 2 with 20MHz channel width
     * This WiFi starts in channel 1, contains peak in 2 and ends in channel 3
     * Example 2 : SSID with peak 2422 Mhz is channel 3 with 40MHz channel width
     * This WiFi starts in channel 1, peak starts in channel 2, peak ends in channel 3 and ends in channel 4
     * The lower the signalToHeight, the higher the height of the graphics will be.
     * @param channelStartIndex Int start of WiFi channel Example 2 -> Channel 1
     * @param channelPeakStartIndex Int start peak of WiFi channel Example 2 -> Channel 2
     * @param channelPeakEndIndex Int end peak of WiFi channel Example 2 -> Channel 3
     * @param channelEndIndex Int end of WiFi Channel Example 3 -> Channel 4
     * @param signalToHeight Float RSSI signal formatted in 10 base -> -30 dBm is 3
     * @param xWidth Float a channel text's width
     * @param yHeight Float a signal text's height
     * @param textSize Float text's size
     */
    fun generatePoint(
        channelStartIndex: Int,
        channelPeakStartIndex: Int,
        channelPeakEndIndex: Int,
        channelEndIndex: Int,
        signalToHeight: Float,
        xWidth: Float,
        yHeight: Float,
        textSize: Float
    ): List<Point> {
        val points = mutableListOf<Point>()
        //if shown full then add
        //else don't add make orthogonal view
        val additionalWidthStart = if (channelStartIndex != channelPeakStartIndex) {
            0.5f
        } else {
            0f
        }

        val additionalWidthEnd = if (channelEndIndex != channelPeakEndIndex) {
            0.5f
        } else {
            0f
        }
        points.add(Point(channelStartIndex * xWidth, yHeight))
        points.add(
            Point(
                (channelPeakStartIndex - additionalWidthStart) * xWidth,
                signalToHeight * (yHeight + textSize) / 10
            )
        )
        points.add(
            Point(
                (channelPeakEndIndex + additionalWidthEnd) * xWidth,
                signalToHeight * (yHeight + textSize) / 10
            )
        )
        points.add(Point(channelEndIndex * xWidth, yHeight))
        return points
    }

}