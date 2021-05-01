package com.example.neco_kot_practice_21_arrays

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val bad = 0..3
    private val normal = 4..6
    private val nice = 7..9
    private val excellent = 10

    private val gradeArray = arrayOf(4, 7, 3, 6, 10, 2)

    private val badArray = ArrayList<String>()
    private val normalArray = ArrayList<String>()
    private val niceArray = ArrayList<String>()
    private val excellentArray = ArrayList<String>()

    private val tag = "My_LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameArray: Array<String> = resources.getStringArray(R.array.names)
        var message:String?

        for ((index, mark) in gradeArray.withIndex()) {
            when (mark) {
                in bad -> {
                    message = "Плохие оценки: Ученик ${nameArray[index]} - $mark"
                    Log.d(tag, message)
                    badArray.add(message)}
                in normal -> {
                    message = "Нормальные оценки: Ученик ${nameArray[index]} - $mark"
                    Log.d(tag, message)
                    normalArray.add(message)}
                in nice -> {
                    message = "Хорошие оценки: Ученик ${nameArray[index]} - $mark"
                    Log.d(tag, message)
                    niceArray.add(message)}
                excellent -> {
                    message = "Отличные оценки: Ученик ${nameArray[index]} - $mark"
                    Log.d(tag, message)
                    excellentArray.add(message)}
            }
        }

        Log.d(tag, "\n")

        for (string in badArray) {
            Log.d(tag, string)
        }
        for (string in normalArray) {
            Log.d(tag, string)
        }
        for (string in niceArray) {
            Log.d(tag, string)
        }
        for (string in excellentArray) {
            Log.d(tag, string)
        }
    }
}
