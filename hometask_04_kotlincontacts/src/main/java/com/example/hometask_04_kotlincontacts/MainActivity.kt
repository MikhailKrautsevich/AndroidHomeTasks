package com.example.hometask_04_kotlincontacts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    inner class NameListAdapter(context: Context) : RecyclerView.Adapter<NameListAdapter.ItemViewHolder>() {

        private var contactsFull : ArrayList<ContactClass> = ArrayList(contacts)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_element, parent, false))


        override fun getItemCount() = contacts.size

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun isListOfContactsEmpty() = contacts.isEmpty()

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var nameText : TextView = findViewById(R.id.nameText)
            private var infoText : TextView = findViewById(R.id.emailText)
            private val phNumberPic : ImageView = findViewById(R.id.phNumberPic)
            private val emailPic : ImageView = findViewById(R.id.emailPic)

            fun bindData (contact: ContactClass) {
                itemView.setOnClickListener(View.OnClickListener {
                    val position = this.adapterPosition
                    val contactTo = contacts.get(position)
                    val remIntent = Intent(this@MainActivity, Activity_edit::class.java)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, contactTo.name)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, contactTo.numberOrEmail)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, contactTo.isEmail)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, position)
                    startActivityForResult(remIntent , EDIT_CONTACT)
                })

                nameText.text = (contact.name)
                infoText.text = (contact.numberOrEmail)
                if (contact.isEmail) {
                    phNumberPic.visibility = View.INVISIBLE
                    emailPic.visibility = View.VISIBLE
                    nameText.setTextColor(Color.GREEN)}
                if (!contact.isEmail) {
                    phNumberPic.visibility = View.VISIBLE
                    emailPic.visibility = View.INVISIBLE
                    nameText.setTextColor(Color.CYAN)}
            }
        }
    }
}

