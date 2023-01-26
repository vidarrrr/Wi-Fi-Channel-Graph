package com.graph.wifi.signal.util.overlap
/**
 * Check if the frequency is within the WiFi frequency range
 * Calculates range of WiFi and checks frequencyChannel if it is within then returns channel otherwise -1
 * @param frequencyChannel Int  frequency of will be controlled
 * @param channel Int channel of frequencyChannel
 */
class ChannelOverlap(private val frequencyChannel: Int,private val channel:Int): Overlapable {
    /**
     * Check if the frequency is within the WiFi frequency range
     * Calculates range of WiFi and checks frequencyChannel if it is within then returns channel otherwise -1
     * @param frequency Int WiFi frequency
     * @param channelWidth Int WiFi Channel Width
     */
    override suspend fun run(frequency: Int, channelWidth: Int): Int {
        val range = (frequency-(channelWidth/2))..(frequency+(channelWidth/2))
        return if(frequencyChannel in range){
            channel
        }else{
            -1
        }
    }

}