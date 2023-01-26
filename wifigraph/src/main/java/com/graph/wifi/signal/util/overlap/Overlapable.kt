package com.graph.wifi.signal.util.overlap

interface Overlapable {
    suspend fun run(frequency:Int, channelWidth:Int):Int
}