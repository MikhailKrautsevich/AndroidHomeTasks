package com.example.hometask_04_kotlincontacts

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var recyclerContacts : RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter1: NameListAdapter
    lateinit var noContacts : TextView
    var contacts: ArrayList<ContactClass> = ArrayList<ContactClass>()

    val ADD_NEW_CONTACT = 900
    val EDIT_CONTACT = 901

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

