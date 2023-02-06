package com.graph.wifi.signal.util

import android.net.wifi.ScanResult
import com.graph.wifi.signal.util.overlap.ChannelOverlap
import com.graph.wifi.signal.util.overlap.Channels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChannelController {
    /**
     * Calculate WiFi Channel Range
     * Attention : If WiFi contains two channelFrequency then create two instance of WiFi Graph and set same color
     * @param channelWidth Int pass WiFi channelWidth value like ScanResult.CHANNEL_WIDTH_20MHZ
     * @param channelFrequency1 Int WiFi peak frequency
     */
    suspend fun getActiveChannels(channelWidth: Int, channelFrequency1: Int): List<Int> =
        withContext(
            Dispatchers.Default
        ) {
            val overlapList = arrayListOf<Int>()
            var channelOverlap: ChannelOverlap
            val channelWidthAsInt = getRange(channelWidth)
            val startIndex = if (channelFrequency1 < 2500) {
                0
            } else {
                13
            }
            val endIndex = if (channelFrequency1 < 2500) {
                13
            } else {
                Channels.list.size - 1
            }
            for (i in startIndex..endIndex) {
                val channelsItem = Channels.list[i]
                channelOverlap = ChannelOverlap(channelsItem.channelFrequency, channelsItem.channel)
                val value = channelOverlap.run(channelFrequency1, channelWidthAsInt)
                if (value != -1) {
                    overlapList.add(value)
                }
            }

            return@withContext overlapList.toList()
        }

    /**
     * Get range of WiFi channel width
     * @param channelWidth Int pass value like ScanResult.CHANNEL_WIDTH_20MHZ
     */
    private fun getRange(channelWidth: Int): Int {
        return when (channelWidth) {
            ScanResult.CHANNEL_WIDTH_20MHZ -> {
                20
            }
            ScanResult.CHANNEL_WIDTH_80MHZ, ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ -> {
                80
            }
            ScanResult.CHANNEL_WIDTH_40MHZ -> {
                40
            }
            ScanResult.CHANNEL_WIDTH_160MHZ -> {
                160
            }
            /*ScanResult.CHANNEL_WIDTH_320MHZ -> {
                320
            }*/
            else -> {
                -1
            }
        }
    }
}