package com.graph.wifi.signal.util

object SignalLevel {
    /**
     * Calculate Signal Level for height in graph
     * -30 -> 3
     * -90 -> 9
     * The lower the SignalToHeight, the higher the height of the charts.
     * @param dbm Int RSSI Signal
     */
    fun getSignalLevel(dbm:Int):Float{
        return if(dbm>=90){
            9f
        }else if(dbm>=0){
            0.5f
        }else{
            -1*dbm/10f
            //10-((100+dbm)/10f)
        }
    }
}