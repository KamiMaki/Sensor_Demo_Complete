package com.example.sensor_kotlin

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.sqrt


private lateinit var start : Button
private lateinit var restart : Button
private lateinit var img : ImageView
private lateinit var point : TextView

private lateinit var sm: SensorManager
private lateinit var mAccelerometer: Sensor
private lateinit var Remain_Time_Text :TextView
private lateinit var Remain_Time :TextView
private val SPEED_THRESHOLD = 2000
private val UPDATE_TIME_INTERVAL = 150
private var mLastX = 0F
private var mLastY = 0F
private var mLastZ = 0F
private var mSpeed = 0.0
private var mLastUpdateTime:Long = 0
private var flag = true//控制CountDownTimer運行(跳出activity時會被中斷)

private var PT  = 0
class Shaking_contest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shaking_contest)
        flag = true
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        //透過Sensormanager選取預設的加速度sensor
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //註冊Listener(參數一:sensorEventListener,參數二:指定的sensor,參數三:設定事件發生後傳送數值的頻率)
        sm.registerListener(myAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        start = findViewById(R.id.button7)
        restart = findViewById(R.id.button8)
        img = findViewById(R.id.imageView)
        point = findViewById(R.id.textView12)
        Remain_Time_Text = findViewById(R.id.textView13)
        Remain_Time = findViewById(R.id.textView14)
        start.setOnClickListener {
            PT = 0//搖動次數
            point.text = "0"//分數初始化為0
            start.visibility = View.INVISIBLE
            img.visibility = View.INVISIBLE
            point.visibility = View.VISIBLE
            Remain_Time.visibility = View.VISIBLE
            Remain_Time_Text.visibility = View.VISIBLE
            sm.registerListener(myAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
            //建立倒數計時器，每秒更新一次時間，時間結束時取消註冊Listener避免繼續更新分數
            object : CountDownTimer(30000, 1000) {
                private var time = 30
                override fun onFinish() {
                    sm.unregisterListener(myAccelerometerListener)
                    restart.visibility = View.VISIBLE
                }

                override fun onTick(millisUntilFinished: Long) {
                    if(!flag) cancel()
                    time-=1
                    Remain_Time.text = time.toString()
                }
            }.start()
        }
        //重新開始時整理layout至初始狀態
        restart.setOnClickListener {
            start.visibility = View.VISIBLE
            restart.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
            point.visibility = View.INVISIBLE
            Remain_Time.visibility = View.INVISIBLE
            Remain_Time_Text.visibility = View.INVISIBLE
        }
    }
    private val myAccelerometerListener = object: SensorEventListener {
        //sensor準確度發生變化的時候
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        //sensor數值發生變化的時候
        override fun onSensorChanged(event: SensorEvent?) {
            if(event != null){
                var mCurrentUpdateTime = System.currentTimeMillis()
                var mTimeInterval = mCurrentUpdateTime- mLastUpdateTime
                if (mTimeInterval<UPDATE_TIME_INTERVAL)
                    return
                mLastUpdateTime = mCurrentUpdateTime
                val xValue = event.values[0] // 新加速度 - X 軸方向
                val yValue = event.values[1] // 新加速度 - Y 軸方向
                val zValue = event.values[2] // 新加速度 - Z 軸方向
                //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
                var DeltaX = xValue - mLastX
                var DeltaY = yValue - mLastY
                var DeltaZ = zValue - mLastZ
                mLastX = xValue
                mLastY = yValue
                mLastZ = zValue
                mSpeed = (sqrt(DeltaX.pow(2)+DeltaY.pow(2)+DeltaZ.pow(2)) * 10000/mTimeInterval).toDouble()
                //更改TextView中的內容
                if(mSpeed >SPEED_THRESHOLD){
                    PT+=1
                    point.text = PT.toString()//更新分數
                }
            }
        }
    }
    override fun onPause() {
        sm.unregisterListener(myAccelerometerListener)
        flag = false
        super.onPause()
    }
}