package com.example.sensor_kotlin
//練習在同個activity中接多個sensor
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

private lateinit var sm: SensorManager
private lateinit var mLight: Sensor
private lateinit var mTemperature: Sensor
private lateinit var mHumid: Sensor
lateinit var L: TextView
lateinit var T: TextView
lateinit var H: TextView

class Practice2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice2)
        //連接三個TextView
        L = findViewById(R.id.L)//顯示光照度數值
        T = findViewById(R.id.T)//顯示溫度數值
        H = findViewById(R.id.H)//顯示濕度數值
        //創立一個Sensormanager來取用感測器資料
        sm = getSystemService(SENSOR_SERVICE) as SensorManager

        //判斷裝置是否有該sensor，避免傳入null到registerListener造成閃退
        if(sm.getDefaultSensor(Sensor.TYPE_LIGHT)!=null){
            mLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT)
            sm.registerListener(mySensorListener, mLight, SensorManager.SENSOR_DELAY_FASTEST)
        }
        if(sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!=null){
            mTemperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            sm.registerListener(mySensorListener, mTemperature, SensorManager.SENSOR_DELAY_FASTEST)
        }
        if(sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)!=null){
            mHumid = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
            sm.registerListener(mySensorListener, mHumid, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }
    //設定SensorEventListener(sensor值改變時要做的事)
    private val mySensorListener = object: SensorEventListener {
        //sensor準確度發生變化的時候
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        //sensor數值發生變化的時候
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                if(event.sensor.type == Sensor.TYPE_LIGHT) {
                    val Light_Value = event.values[0]
                    L.text = Light_Value.toString()
                }
                if(event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    val Temp_Value = event.values[0]
                    T.text = Temp_Value.toString()
                }
                if(event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    val Humid_Value = event.values[0]
                    H.text = Humid_Value.toString()
                }
            }
        }
    }
    override fun onPause() {
        sm.unregisterListener(mySensorListener)
        super.onPause()
    }
}