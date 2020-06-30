package com.example.sensor_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn1: Button = findViewById(R.id.button)
        var btn2: Button = findViewById(R.id.button2)
        var btn3: Button = findViewById(R.id.button3)
        var btn4: Button = findViewById(R.id.button4)
        var btn5: Button = findViewById(R.id.button5)
        var btn6: Button = findViewById(R.id.button6)
        btn1.setOnClickListener {
            startActivity(Intent(this, Sensor_list::class.java))//Sensor list
        }
        btn2.setOnClickListener {
            startActivity(Intent(this, Multiple_language::class.java))//多國語言
        }
        btn3.setOnClickListener {
            startActivity(Intent(this, Practice1::class.java))//加速計
        }
        btn4.setOnClickListener {
            startActivity(Intent(this, Practice2::class.java))//多重Sensor
        }
        btn5.setOnClickListener {
            startActivity(Intent(this, shake::class.java))//搖一搖
        }
        btn6.setOnClickListener {
            startActivity(Intent(this, Shaking_contest::class.java)) //手速大賽
        }
    }
}