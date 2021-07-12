package com.example.simplealarmclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.simplealarmclock.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setAlarmBtn = findViewById<Button>(R.id.setAlarmBtn)
        setAlarmBtn.setOnClickListener {
            val materialTimePicker = MaterialTimePicker
                    .Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(getString(R.string.choose_time))
                    .build()

            materialTimePicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.MINUTE, materialTimePicker.minute)
                calendar.set(Calendar.HOUR, materialTimePicker.hour)
            }

            materialTimePicker.show(supportFragmentManager, "materialTimePicker")
        }
    }
}