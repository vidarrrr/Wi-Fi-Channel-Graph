package com.graph.wifi.signal.util

//https://en.wikipedia.org/wiki/List_of_WLAN_channels
object ChannelConstants {
    val _2ghzChannels = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    val _5ghzChannels1 = listOf(32, 34, 36, 38, 40, 42, 44, 46, 48)
    val _5ghzChannels2 = listOf(50, 52, 54, 56, 58, 60, 62, 64, 68)
    val _5ghzChannels3 = listOf(96, 100, 102, 104, 106, 108, 110)
    val _5ghzChannels4 = listOf(112, 114, 116, 118, 120, 122, 124)
    val _5ghzChannels5 = listOf(126, 128, 132, 134, 136, 138)
    val _5ghzChannels6 = listOf(140, 142, 144, 149, 151, 153, 155, 157, 159)
    val _5ghzChannels7 = listOf(161, 163, 165, 167, 169, 171, 173, 175, 177)

    const val _2ghz = "2.5 GHz"
    const val _5ghz = "5 GHz"
}