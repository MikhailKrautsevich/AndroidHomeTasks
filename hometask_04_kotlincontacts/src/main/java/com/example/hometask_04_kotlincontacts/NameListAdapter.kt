package com.example.hometask_04_kotlincontacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NameListAdapter(context: Context, contacts: ArrayList<ContactClass>) : RecyclerView.Adapter<NameListAdapter.ItemViewHolder>() {

    var contactsFull : ArrayList<ContactClass>
    var contacts : ArrayList<ContactClass>

    init {
        contactsFull = ArrayList(contacts)
        this.contacts = ArrayList(contacts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_element, parent, false))


    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isListOfContactsEmpty() = contacts.isEmpty()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}