package com.example.simplealarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
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
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MINUTE, materialTimePicker.minute)
                calendar.set(Calendar.HOUR, materialTimePicker.hour)

                val alarmManger: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val info : AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis,
                        getAlarmInfoPendingIntent())
                alarmManger.setAlarmClock(info, getAlarmActionPendingIntent())
                val sdt = SimpleDateFormat("HH : mm", Locale.getDefault())
                val text = getString(R.string.alarm_clock_is_set) + sdt.format(calendar.time)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }

            materialTimePicker.show(supportFragmentManager, "materialTimePicker")
        }
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getAlarmActionPendingIntent() : PendingIntent {
        intent = Intent(this, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}