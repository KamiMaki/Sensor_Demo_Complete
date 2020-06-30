package com.example.sensor_kotlin

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.sqrt


private lateinit var sm: SensorManager
private lateinit var mAccelerometer: Sensor

private val SPEED_THRESHOLD = 200//搖動速度閥值
private val UPDATE_TIME_INTERVAL = 150//達成觸發條件之搖動持續時間
private var mLastX = 0F//一開始的X方向加速度
private var mLastY = 0F//一開始的Y方向加速度
private var mLastZ = 0F//一開始的Z方向加速度
private var mSpeed = 0F//三軸加速度加權之搖動速度
private var mLastUpdateTime:Long = 0//一開始的時間
private lateinit var Speed:TextView
private lateinit var State:TextView
class shake : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        //透過Sensormanager選取預設的加速度sensor
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //註冊Listener(參數一:sensorEventListener,參數二:指定的sensor,參數三:設定事件發生後傳送數值的頻率)
        sm.registerListener(myAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        Speed = findViewById(R.id.textView9)
        State = findViewById(R.id.textView10)
    }
    private val myAccelerometerListener = object: SensorEventListener {
        //sensor準確度發生變化的時候
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        //sensor數值發生變化的時候
        override fun onSensorChanged(event: SensorEvent?) {
            if(event != null){
                var mCurrentUpdateTime = System.currentTimeMillis()//取得現在時間
                var mTimeInterval = mCurrentUpdateTime- mLastUpdateTime//計算時間差
                if (mTimeInterval<UPDATE_TIME_INTERVAL)//如果小於指定的時間區間則跳出函式
                    return
                //計算目前加速度
                val xValue = event.values[0] // 加速度 - X 軸方向
                val yValue = event.values[1] // 加速度 - Y 軸方向
                val zValue = event.values[2] // 加速度 - Z 軸方向

                //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
                var DeltaX = xValue - mLastX
                var DeltaY = yValue - mLastY
                var DeltaZ = zValue - mLastZ
                //保存現在加速度及時間
                mLastX = xValue
                mLastY = yValue
                mLastZ = zValue
                mLastUpdateTime = mCurrentUpdateTime
                //速度加權計算公式
                mSpeed = sqrt(DeltaX.pow(2)+DeltaY.pow(2)+DeltaZ.pow(2))* 10000/mTimeInterval

                //更改TextView中的內容
                if(mSpeed >SPEED_THRESHOLD){
                    Speed.text = mSpeed.toString()
                    State.text = "搖動中!!!"
                    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)//參數為震動持續時間
                }
                else {
                    State.text = "停止搖動..."
                }
            }
        }
    }
    override fun onPause() {
        sm.unregisterListener(myAccelerometerListener)
        super.onPause()
    }
}