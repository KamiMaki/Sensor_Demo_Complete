package com.example.sensor_kotlin
//讀取三軸加速度sensor的值
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.abs

private lateinit var sm: SensorManager
private lateinit var mAccelerometer: Sensor
lateinit var X: TextView
lateinit var Y: TextView
lateinit var Z: TextView

class Practice1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice1)
        //連接三個TextView
        X = findViewById(R.id.X)
        Y = findViewById(R.id.Y)
        Z = findViewById(R.id.Z)
        //創立一個Sensormanager來取用感測器資料
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        //透過Sensormanager選取預設的加速度sensor
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //註冊Listener(參數一:sensorEventListener,參數二:指定的sensor,參數三:設定事件發生後傳送數值的頻率)
        sm.registerListener(myAccelerometerListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        /*
          SENSOR_DELAY_NORMAL:200,000 microseconds = 0.2s
          SENSOR_DELAY_UI: 60,000 microsecond = 0.06s
          SENSOR_DELAY_GAME: 20,000 microsecond = 0.02s
          SENSOR_DELAY_FASTEST: 0 microsecond = 0s
         */
    }
    //設定SensorEventListener(sensor值改變時要做的事)
    private val myAccelerometerListener = object: SensorEventListener {
        //sensor準確度發生變化的時候
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        //sensor數值發生變化的時候
        override fun onSensorChanged(event: SensorEvent?) {
            if(event != null){
                val xValue = abs(event.values[0]) // 加速度 - X 軸方向
                val yValue = abs(event.values[1]) // 加速度 - Y 軸方向
                val zValue = abs(event.values[2]) // 加速度 - Z 軸方向
                //更改TextView中的內容
                X.text = xValue.toString()
                Y.text = yValue.toString()
                Z.text = zValue.toString()
            }
        }
    }
    override fun onPause() {
        sm.unregisterListener(myAccelerometerListener)
        super.onPause()
    }
}