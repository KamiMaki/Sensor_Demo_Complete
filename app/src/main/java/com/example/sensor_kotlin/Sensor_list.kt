package com.example.sensor_kotlin

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.TextView

class Sensor_list : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_list)
        //宣告SensorManager
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        //連接TextView
        var TV: TextView = findViewById(R.id.textView)
        //取得所有的sensor
        val allSensors = sm.getSensorList(Sensor.TYPE_ALL)
        //依序append到textView中
        for(sensor in allSensors){
            TV.append("${sensor.name}\n")
        }
    }
}