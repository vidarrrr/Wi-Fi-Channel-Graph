package com.graph.wifisignalgraph

import android.net.wifi.ScanResult
import com.graph.wifi.signal.util.ChannelController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateRange() = runTest(UnconfinedTestDispatcher()) {
        val fifthChannels =
            ChannelController.getActiveChannels(ScanResult.CHANNEL_WIDTH_20MHZ, 2417)
        val sixthChannels =
            ChannelController.getActiveChannels(ScanResult.CHANNEL_WIDTH_80MHZ, 2447)
        val seventhChannels =
            ChannelController.getActiveChannels(ScanResult.CHANNEL_WIDTH_40MHZ, 2447)
        assertEquals(4,fifthChannels.size)
        assertEquals(14,sixthChannels.size)
        assertEquals(9,seventhChannels.size)
    }
}