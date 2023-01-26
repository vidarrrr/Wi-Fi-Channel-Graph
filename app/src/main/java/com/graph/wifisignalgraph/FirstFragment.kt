package com.graph.wifisignalgraph

import android.graphics.Color
import android.net.wifi.ScanResult
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.graph.wifisignalgraph.databinding.FragmentFirstBinding
import com.graph.wifi.signal.util.ChannelConstants
import com.graph.wifi.signal.util.ColorGenerator
import com.graph.wifi.signal.util.SignalLevel
import com.graph.wifi.signal.model.WiFiGraph
import com.graph.wifi.signal.util.ChannelController.getActiveChannels
import com.graph.wifi.signal.util.FrequencyToChannel
import com.graph.wifi.signal.util.overlap.ChannelOverlap
import com.graph.wifi.signal.util.overlap.Channels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/

        //https://medium.com/@anujguptawork/how-to-create-your-own-android-library-and-publish-it-750e0f7481bf
        MainScope().launch {
            val colorGenerator = ColorGenerator()
            val firstChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_40MHZ,5160)
            val secondChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ,5250)
            val secondChannels2 = getActiveChannels(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ,5200)
            val thirdChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_20MHZ,5180)
            val fourthChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_80MHZ,5260)
            val fifthChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_20MHZ,2417)
            val sixthChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_80MHZ,2447)
            val seventhChannels = getActiveChannels(ScanResult.CHANNEL_WIDTH_40MHZ,2447)

            binding.cg.start(
                ChannelConstants._2ghz,
                listOf(
                    WiFiGraph(
                        "FREEBOX",
                        32,
                        Color.parseColor(colorGenerator.generateColor()), //"#33FFAA00"
                        firstChannels,//listOf(30, 32, 34),
                        SignalLevel.getSignalLevel(-59)
                    ),
                    WiFiGraph(
                        "FREEBOX-A",
                        50,
                        Color.parseColor("#33FFFF00"),
                        secondChannels,//listOf(42, 44, 46, 48, 50, 52, 54,56),
                        SignalLevel.getSignalLevel(-49)
                    ),
                    /*WiFiGraph(
                        "FREEBOX-A2",
                        50,
                        Color.parseColor("#33FFFF00"),
                        secondChannels2,//listOf(42, 44, 46, 48, 50, 52, 54,56),
                        SignalLevel.getSignalLevel(-49)
                    ),*/
                    WiFiGraph(
                        "FREEBOX-B",
                        36,
                        Color.parseColor(colorGenerator.generateColor()),
                        thirdChannels,//listOf(34, 36, 38),
                        SignalLevel.getSignalLevel(-39)
                    ),
                    WiFiGraph(
                        "FREEBOX-C",
                        52,
                        Color.parseColor(colorGenerator.generateColor()),
                        fourthChannels,//listOf(46, 48, 50, 52, 54,56),
                        SignalLevel.getSignalLevel(-49)
                    ),
                    WiFiGraph(
                        "FREEBOX",
                        FrequencyToChannel.intChannel(2417),
                        Color.parseColor(colorGenerator.generateColor()), //"#33FFAA00"
                        fifthChannels,
                        SignalLevel.getSignalLevel(-59)
                    ),
                    WiFiGraph(
                        "FREEBOX",
                        6,
                        Color.parseColor("#33FFFF00"),
                        sixthChannels,
                        SignalLevel.getSignalLevel(-49)
                    ),
                    WiFiGraph(
                        "FREEBOX",
                        6,
                        Color.parseColor(colorGenerator.generateColor()),
                        seventhChannels,
                        SignalLevel.getSignalLevel(-39)
                    )
                ),
                ChannelConstants._2ghzChannels
            )

            binding.cg.setOnSSIDWithColor {
                //https://stackoverflow.com/questions/6094315/single-textview-with-multiple-colored-text
                binding.text1.text = ""
                for (i in binding.cg.ssidWithColors) {
                    val spannableString = SpannableString(i.ssid + "\t")
                    spannableString.setSpan(
                        ForegroundColorSpan(i.color),
                        0,
                        spannableString.length,
                        0
                    )
                    binding.text1.append(spannableString)
                }
            }
            var clicked = false
            binding.buttonChanger.setOnClickListener {
                clicked = if(clicked){
                    binding.cg.setOnlyShowPositions(listOf(0,2)){

                    }
                    false
                }else{
                    binding.cg.setOnlyShowPositions(listOf()){

                    }
                    true
                }
            }
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.two_ghz -> {
                        binding.cg.setChannels(ChannelConstants._2ghzChannels)
                        binding.cg.setChannelName(ChannelConstants._2ghz)
                    }
                    R.id.five_ghz -> {
                        binding.cg.setChannels(ChannelConstants._5ghzChannels1)
                        binding.cg.setChannelName(ChannelConstants._5ghz)
                    }
                    else -> {
                        binding.cg.setChannels(ChannelConstants._5ghzChannels2)
                        binding.cg.setChannelName(ChannelConstants._5ghz)
                    }
                }

            }
        }


        /* //Test Cases
        binding.cg.start(
            ChannelPosition._2ghz,
            listOf(
                WiFiGraph(
                    "FREEBOX",
                    52,
                    Color.parseColor(ColorGenerator().generateColor()), //"#33FFAA00"
                    listOf(50, 52, 54),
                    SignalLevel.getSignalLevel(-59)
                ),
                WiFiGraph(
                    "FREEBOX-A",
                    46,
                    Color.parseColor("#33FFFF00"),
                    listOf(42, 44, 46, 48, 50),
                    SignalLevel.getSignalLevel(-49)
                ),
                WiFiGraph(
                    "FREEBOX-B",
                    36,
                    Color.parseColor(ColorGenerator().generateColor()),
                    listOf(34, 36, 38),
                    SignalLevel.getSignalLevel(-39)
                )
            ),
            ChannelPosition._5ghzChannels2
        )



binding.cg.start(
            ChannelPosition._2ghz,
            listOf(
                WiFiGraph(
                    "FREEBOX",
                    52,
                    Color.parseColor(ColorGenerator().generateColor()), //"#33FFAA00"
                    listOf(50, 52, 54),
                    SignalLevel.getSignalLevel(-59)
                ),
                WiFiGraph(
                    "FREEBOX-A",
                    46,
                    Color.parseColor("#33FFFF00"),
                    listOf(42, 44, 46, 48, 50),
                    SignalLevel.getSignalLevel(-49)
                ),
                WiFiGraph(
                    "FREEBOX-B",
                    36,
                    Color.parseColor(ColorGenerator().generateColor()),
                    listOf(34, 36, 38),
                    SignalLevel.getSignalLevel(-39)
                )
            ),
            ChannelPosition._5ghzChannels1
        )



binding.cg.start(
            ChannelPosition._2ghz,
            listOf(
                WiFiGraph(
                    "FREEBOX",
                    2,
                    Color.parseColor(ColorGenerator().generateColor()), //"#33FFAA00"
                    listOf(1, 2, 3),
                    SignalLevel.getSignalLevel(-59)
                ),
                WiFiGraph(
                    "FREEBOX",
                    6,
                    Color.parseColor("#33FFFF00"),
                    listOf(4, 5, 6, 7, 8),
                    SignalLevel.getSignalLevel(-49)
                ),
                WiFiGraph(
                    "FREEBOX",
                    6,
                    Color.parseColor(ColorGenerator().generateColor()),
                    listOf(5, 6, 7),
                    SignalLevel.getSignalLevel(-39)
                )
            ),
            ChannelPosition._2ghzChannels
        )
        * */
        //val spannableStrBuilder = SpannableStringBuilder()




    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}