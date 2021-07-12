package com.example.simplealarmclock

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    private var ringtone : Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        var notificationUri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notificationUri)
        if (ringtone == null) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, notificationUri)
        }
        ringtone?.play()
    }

    override fun onDestroy() {
        if (ringtone != null && ringtone?.isPlaying == true) {
            ringtone!!.stop()
        }
        super.onDestroy()
    }
}
