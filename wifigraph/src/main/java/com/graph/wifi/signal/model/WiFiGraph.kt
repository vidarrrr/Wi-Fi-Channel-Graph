package com.graph.wifi.signal.model

data class WiFiGraph(
    val ssid: String,
    val channelPeak: Int,
    val color: Int,
    val channels: List<Int>,
    val signalStrength: Float,
    val channelName: String
)
