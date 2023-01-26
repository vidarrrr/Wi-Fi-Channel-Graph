package com.graph.wifi.signal.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.graph.wifi.signal.util.PointGenerator
import com.graph.wifi.signal.model.Point
import com.graph.wifi.signal.model.SSIDWithColor
import com.graph.wifi.signal.model.WiFiGraph
import java.util.concurrent.atomic.AtomicBoolean

class ChannelRouterGraph @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyle: Int = 0
) :
    View(context, attributeSet, defaultStyle) {

    private lateinit var paint: Paint

    private lateinit var path: Path

    //private val graphColor = Color.parseColor("#f9aa43")
    private var xAxisChannelColor = Color.parseColor("#010101")

    private var lineColor = Color.LTGRAY//Color.parseColor("#FFFB7D")
    private var signalLevelColor = Color.parseColor("#f9aa43")//Color.parseColor("#97D9E1")
    private var bgColor: Int = Color.WHITE


    private var wifiList2 = emptyList<WiFiGraph>()
    private var channels = emptyList<Int>()
    private var _ssidWithColors = arrayListOf<SSIDWithColor>()
    private var _colorsToPositions = mutableMapOf<Int,Int>()

    val ssidWithColors: List<SSIDWithColor> get() = _ssidWithColors
    val colorsTopPositions: Map<Int,Int> get() = _colorsToPositions

    private var channelName: String = ""
    private var strokeWidth: Float
    private var textSize: Float

    private var onSSIDWithColor: (() -> Unit)? = null
    private var showOnlySelectedIndexes = arrayListOf<Int>()

    private val isLoading = AtomicBoolean(false)

    /**
     *  Where it all begins in here
     *  @param channelName String Channel Hz Name like 2.5 GHz
     *  @param wifiList2 List<WiFiGraph>
     *  @param channels List<Int> X Axis Channel List
     *  @param backGroundColor Int Canvas Background Color
     *  @param lineColor Int Lines Color
     *  @param signalLevelColor Int RSSI and channelName color
     *  @param strokeWidth Float Size of paint
     *  @param textSize Float Size of texts
     */
    fun start(
        channelName: String,
        wifiList2: List<WiFiGraph>,
        channels: List<Int>,
        backGroundColor: Int = Color.WHITE,
        xAxisChannelColor: Int = Color.parseColor("#010101"),
        lineColor: Int = Color.LTGRAY,
        signalLevelColor: Int = Color.parseColor("#f9aa43"),
        strokeWidth: Float = 2f,
        textSize: Float = 18f
    ) {
        this.channelName = channelName
        this.wifiList2 = wifiList2
        this.channels = channels
        this.bgColor = backGroundColor
        this.xAxisChannelColor = xAxisChannelColor
        this.lineColor = lineColor
        this.signalLevelColor = signalLevelColor
        this.strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            strokeWidth,
            context.resources.displayMetrics
        )

        this.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            textSize,
            context.resources.displayMetrics
        )
        invalidate()
    }

    /**
     * Display only selected Wifi Graphs
     * If you set an empty list, all List<WiFiGraph> will be shown, otherwise a list with positions is required
     * @param positions List<Int> emptyList for all, otherwise list of selected positions
     * @param onLoading () -> Unit prevent redrawing while is drawing
     */
    fun setOnlyShowPositions(positions : List<Int>,onLoading : () -> Unit){
        if(isLoading.get()){
            onLoading()
            return
        }
        showOnlySelectedIndexes.clear()
        if(positions.isEmpty()) {
            invalidate()
            return
        }
        for(i in positions){
            showOnlySelectedIndexes.add(i)
        }
        invalidate()
    }

    /**
     * Set channels
     * @param channels List<Int>
     */
    fun setChannels(channels: List<Int>){
        this.channels = channels
        invalidate()
    }


    /**
     * Set channel Name
     * @param channelName String
     */
    fun setChannelName(channelName: String){
        this.channelName = channelName
        invalidate()
    }
    /**
     * Paint set
     */
    private fun startPaint() {
        paint = Paint()

        //paint.color = graphColor
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth


    }

    /**
     * Path set
     */
    private fun startPath() {
        path = Path()
    }

    init {
        //https://stackoverflow.com/questions/13600802/android-convert-dp-to-float
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f,
            context.resources.displayMetrics
        )

        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            18f,
            context.resources.displayMetrics
        )
        startPaint()
        startPath()
    }

    /**
     * Set callback function
     * @param onSSIDWithColor () -> Unit
     */
    fun setOnSSIDWithColor(onSSIDWithColor: () -> Unit) {
        this.onSSIDWithColor = onSSIDWithColor
    }

    /**
     * Draw WiFi Graphics
     */
    private fun drawGraphs(
        canvas: Canvas,
        height: Float,
        width: Float,
        wifiList2: List<WiFiGraph>,
        ssidWithColors: ArrayList<SSIDWithColor>,
        channels: List<Int>,
        showOnlySelectedIndexes: List<Int>
    ) {

//        paint.strokeWidth = strokeWidth
//        path.reset()
//        drawSignals(path, paint, height / 10, canvas)
//        drawChannelName(canvas, paint, channelName)
//        drawChannels(path, paint, xAxisChannelColor,width / (channels.size + 1), canvas, channels)

//        //https://stackoverflow.com/questions/32691499/android-fill-path-with-color-partially
//        //https://stackoverflow.com/questions/13545792/drawing-a-filled-rectangle-with-a-border-in-android
//        //https://stackoverflow.com/questions/29100772/android-canvas-cliprect-removing-existing-clip
        val paintNew = Paint()
        paintNew.color = xAxisChannelColor
        paintNew.style = Paint.Style.STROKE
        paintNew.strokeCap = Paint.Cap.ROUND
        paintNew.strokeJoin = Paint.Join.ROUND
        paintNew.isAntiAlias = true
        paintNew.strokeWidth = strokeWidth
        val path2 = Path()


        ssidWithColors.clear()
        var currentPosition = 0
        for (i in wifiList2) {
            _colorsToPositions[i.color] = currentPosition
            if(showOnlySelectedIndexes.isNotEmpty() && currentPosition !in showOnlySelectedIndexes){
                currentPosition++
                continue
            }
            val startPeakPosition = if (i.channels.size == 3) {
                i.channelPeak
            } else {
                //channels 36 38 ...
                //i.channels 32 34 36 .. 42 44 46 48
                var position = 1
                while(channels.indexOf(i.channels[position])==-1 && i.channels.size - 1> position){//i.channels[position]<=i.channelPeak){
                    position++
                }
                i.channels[position]
                //i.channels[1]
            }
            val lastPeakPosition = if (i.channels.size == 3) {
                i.channelPeak
            } else {
                var position = i.channels.size - 2
                while(channels.indexOf(i.channels[position])==-1 && i.channels[position] >= startPeakPosition){//i.channels[position]>=i.channelPeak){
                    position--
                }
                i.channels[position]
                //i.channels[i.channels.size - 2]
            }
            //if (channels.indexOf(i.channelPeak) == -1) continue
            var channelStartIndex = channels.indexOf(i.channels.first()) + 1
            val startPeakPosition1 = channels.indexOf(startPeakPosition) + 1
            //not in range
            if (channelStartIndex == 0) {
                //this wifi in not in list
                if (startPeakPosition1 == 0){
                    currentPosition++
                    continue
                }
                else
                    channelStartIndex = startPeakPosition1
            }
            val lastPeakPosition1 = channels.indexOf(lastPeakPosition) + 1
            var channelEndIndex = channels.indexOf(i.channels.last()) + 1

            //not in range
            if (channelEndIndex == 0) {
                //this wifi in not in list
                if (lastPeakPosition1 == 0){
                    currentPosition++
                    continue
                }
                else
                    channelEndIndex = lastPeakPosition1
            }

            val points1 = PointGenerator.generatePoint(
                channelStartIndex,//i.channels.first(),
                startPeakPosition1,//startPeakPosition,
                lastPeakPosition1,//lastPeakPosition,
                channelEndIndex,//i.channels.last(),
                i.signalStrength,
                width / (channels.size + 1),
                height - textSize,
                textSize
            )
            graph(
                path2,
                paintNew,
                i.color,
                canvas,
                points1[0],
                points1[1],
                points1[2],
                points1[3]
                //Point(0f, height - textSize),
                //Point((width / channels.size), 4 * ((height-textSize) / 10)),
                //Point(4 * (width / channels.size), height - textSize)
            )

            ssidWithColors.add(SSIDWithColor(i.ssid, i.color))
            currentPosition++
            //drawChannels(path2, paintNew, width / (channels.size + 1), canvas, channels)
        }


        onSSIDWithColor?.let { it() }


        paintNew.color = xAxisChannelColor
        //canvas.drawText(channelInt.toString(), previousX * 2, textSize, paint)
        //https://stackoverflow.com/questions/20900412/center-text-in-canvas-android
    }

    /**
     * Draw Channel Name -> 2.5 Ghz
     */
    private fun drawChannelName(canvas: Canvas, paint: Paint, channelName: String) {
        val bounds = Rect()
        paint.style = Paint.Style.FILL
        paint.color = signalLevelColor
        paint.getTextBounds(channelName, 0, channelName.length, bounds)
        val x11 = (canvas.width / 2f) - (bounds.width() / 2f)
        canvas.drawText(channelName, x11, textSize, paint)
    }

    /**
     * Draw RSSI Signals -90 -80 ...
     */
    private fun drawSignals(path: Path, paint: Paint, oneOfChannelHeight: Float, canvas: Canvas) {
        val signalValue = -90
        for (i in 0..10) {
            paint.style = Paint.Style.FILL
            paint.color = signalLevelColor
            paint.textSize = textSize
            canvas.drawText(
                (-1 * (-signalValue - (9 - i) * 10)).toString(),
                0f,
                (i) * oneOfChannelHeight + textSize,
                paint
            )
            //canvas.drawText((10 - i).toString(), 0f, (i) * oneOfChannelHeight + textSize, paint)
            paint.color = lineColor
            paint.style = Paint.Style.STROKE
            path.moveTo(0f, (i) * oneOfChannelHeight)
            path.lineTo(0f, (i) * oneOfChannelHeight)
            path.lineTo(width.toFloat(), (i) * oneOfChannelHeight)
            canvas.drawPath(path, paint)
            path.reset()
        }
    }

    /**
     * Draw Channels in X Axis 1 2 3 ...
     */
    private fun drawChannels(
        path: Path,
        paint: Paint,
        xAxisChannelColor: Int,
        oneOfChannelWidth: Float,
        canvas: Canvas,
        channels: List<Int>
    ) {
        val color = paint.color
        paint.color = xAxisChannelColor
        paint.style = Paint.Style.FILL
        for (i in channels.indices) {

            paint.textSize = textSize
            val rect = Rect()
            val text = channels[i].toString()
            //center channel text
            paint.getTextBounds(text, 0, text.length, rect)
            canvas.drawText(
                channels[i].toString(),
                ((i + 1) * oneOfChannelWidth) - (rect.width() / 2),// + textSize,
                height.toFloat(),
                paint
            )

            canvas.drawPath(path, paint)
            path.reset()
        }
        paint.style = Paint.Style.STROKE
        paint.color = color
    }

    /**
     * Draw Trapezoid WiFi Graph
     */
    private fun graph(
        path: Path,
        paint: Paint,
        color: Int,
        canvas: Canvas,
        startPoint: Point,
        centerStartPoint: Point,
        centerEndPoint: Point,
        endPoint: Point
    ) {


        path.reset()
        paint.style = Paint.Style.FILL
        paint.color = color
        path.moveTo(startPoint.x, startPoint.y)




        path.lineTo(centerStartPoint.x, centerStartPoint.y)
        path.lineTo(centerEndPoint.x, centerEndPoint.y)
        path.lineTo(endPoint.x, endPoint.y)

        canvas.drawPath(path, paint)
        //only selected area can draw
        //canvas.clipPath(path)

        //canvas.drawRect(rect,paint)
        path.reset()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawColor(bgColor)
            if (wifiList2.isNotEmpty()) {
                isLoading.compareAndSet(false,true)
                paint.strokeWidth = strokeWidth
                path.reset()

                drawSignals(path, paint, height.toFloat() / 10, canvas)
                drawChannelName(canvas, paint, channelName)
                drawChannels(
                    path,
                    paint,
                    xAxisChannelColor,
                    width.toFloat() / (channels.size + 1),
                    canvas,
                    channels
                )

                drawGraphs(
                    it,
                    height.toFloat(),
                    width.toFloat(),
                    wifiList2,
                    _ssidWithColors,
                    channels,
                    showOnlySelectedIndexes.toList()
                )
                isLoading.compareAndSet(true,false)
            }
        }
    }
}