package com.example.hometask_04_kotlincontacts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerContacts : RecyclerView
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    private val gridLayoutManager: GridLayoutManager = GridLayoutManager(this, 2)
    lateinit var adapter1: NameListAdapter
    private lateinit var noContacts : TextView
    val contacts: ArrayList<ContactClass> = ArrayList()

    private val ADD_NEW_CONTACT = 900
    private val EDIT_CONTACT = 901

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        noContacts = findViewById(R.id.noContacts)

        val addNewContact : ImageButton = findViewById(R.id.addNewContact)
        addNewContact.setOnClickListener{
            val intent = Intent(this, Activity_add::class.java)
            startActivityForResult(intent, ADD_NEW_CONTACT)
        }
 //       linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
 //       gridLayoutManager = GridLayoutManager(this, 2)
        recyclerContacts = findViewById(R.id.recyclerContacts)
        if (recyclerContacts.adapter == null) {
        recyclerContacts.adapter = NameListAdapter(this) }
        adapter1 = recyclerContacts.adapter as NameListAdapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerContacts.layoutManager = linearLayoutManager
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerContacts.layoutManager = gridLayoutManager
        }
        recyclerContacts.visibility = View.INVISIBLE

        val searchView : SearchView = findViewById(R.id.search)
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter1.filter.filter(newText)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (adapter1.isListOfContactsEmpty()) {
            recyclerContacts.visibility = View.INVISIBLE
            noContacts.visibility = View.VISIBLE
        } else {
            recyclerContacts.visibility = View.VISIBLE
            noContacts.visibility = View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (adapter1 == null)
        {adapter1 = recyclerContacts.adapter as NameListAdapter}
        if (data != null)
        {when (requestCode) {
        ADD_NEW_CONTACT -> {
        val isMail : Boolean = data.getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS , false)
        if ( data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME) != null
                && data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO) != null) run {
            val conName: String = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME).trim()
            val conInfo: String = data.getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO).trim()

            if (conName.isNotEmpty() && conInfo.isNotEmpty() && isMail) {
                val newContact : ContactClass = ContactClass
                        .setName(conName)
                        .setNumberOrEmail(conInfo)
                        .itIsEmail()
                        .build()
                adapter1.addItem(newContact)
            }
            if (conName.isNotEmpty() && conInfo.isNotEmpty() && !isMail) {
                val newContact : ContactClass = ContactClass
                        .setName(conName)
                        .setNumberOrEmail(conInfo)
                        .build()
                adapter1.addItem(newContact)
            }
        }
        }
            EDIT_CONTACT -> {if (resultCode == Activity.RESULT_OK) {
            val position : Int = data.getIntExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, 9)
            adapter1.removeItem(position)}
            }
        }
        }
    }

    inner class NameListAdapter(context: Context) : RecyclerView.Adapter<NameListAdapter.ItemViewHolder>() , Filterable {

        private var contactsFull : ArrayList<ContactClass> = ArrayList(contacts)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_element, parent, false))


        override fun getItemCount(): Int {
            var result : Int = 0
            if (contacts != null)
            {result =  contacts.size}
            return result
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bindData(contacts[position])
        }

        fun addItem(newContact: ContactClass) {
            contacts.add(newContact)
            val comparator = ContactsComparator()
            contacts.sortWith(comparator)
            notifyDataSetChanged()
        }

        fun removeItem(position: Int) {
            if (contacts.size >= position) {
                contacts.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getFilter(): Filter {
            return ContactFilter()
        }

        inner class ContactFilter : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtered : MutableList<ContactClass> = ArrayList<ContactClass>()
                if (constraint == null || constraint.length == 0) {
                    filtered.addAll(contactsFull)
                } else {
                    var pattern : String = constraint.toString().toLowerCase().trim()
                    for (con in contactsFull) {
                        if ((con.name).toLowerCase().startsWith(pattern))
                        {filtered.add(con)}
                    }
                }
                val results = FilterResults()
                results.values = filtered
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contacts.clear()
                if (results != null) {
                    contacts.addAll(results.values as List<ContactClass>)
                    notifyDataSetChanged()
                }
            }
        }

        fun isListOfContactsEmpty() = contacts.isEmpty()

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val nameText = itemView.findViewById<TextView>(R.id.nameText)
            private val infoText = itemView.findViewById<TextView>(R.id.emailText)
            private val phNumberPic = itemView.findViewById<ImageView>(R.id.phNumberPic)
            private val emailPic = itemView.findViewById<ImageView>(R.id.emailPic)

            fun bindData (contact: ContactClass) {
                itemView.setOnClickListener(View.OnClickListener {
                    val position = this.adapterPosition
                    val contactTo = contacts[position]
                    val remIntent = Intent(this@MainActivity, Activity_edit::class.java)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, contactTo.name)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, contactTo.numberOrEmail)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, contactTo.isEmail)
                    remIntent.putExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, position)
                    startActivityForResult(remIntent , EDIT_CONTACT)
                })

                val position = this.adapterPosition
                nameText?.text = contact.name
                infoText?.text = (contact.numberOrEmail)
                if (contact.isEmail) {
                    phNumberPic?.visibility = View.INVISIBLE
                    emailPic?.visibility = View.VISIBLE
                    nameText?.setTextColor(Color.GREEN)}
                if (!contact.isEmail) {
                    phNumberPic?.visibility = View.VISIBLE
                    emailPic?.visibility = View.INVISIBLE
                    nameText?.setTextColor(Color.CYAN)}
            }
        }
    }
}

