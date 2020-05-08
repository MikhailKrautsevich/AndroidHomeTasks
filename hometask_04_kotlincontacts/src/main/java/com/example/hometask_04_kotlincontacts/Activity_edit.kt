package com.example.hometask_04_kotlincontacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class Activity_edit : Activity() {

    private lateinit var backFromEdit : ImageButton
    private lateinit var removeContact : Button
    private lateinit var phoneNumEdit : TextView
    private lateinit var emailEdit : TextView
    private lateinit var nameEdit : TextView
    private lateinit var answerIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gettedIntent : Intent = this.intent
        answerIntent = intent
        setContentView(R.layout.activity_edit)

        backFromEdit = findViewById(R.id.backFromEdit)
        backFromEdit.setOnClickListener {
            setResult(RESULT_CANCELED, answerIntent)
            finish()
        }

        nameEdit = findViewById(R.id.nameEdit)
        emailEdit = findViewById(R.id.emailEdit)
        phoneNumEdit = findViewById(R.id.phoneNumEdit)

        nameEdit.text = (gettedIntent.
                getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME))
        val isItEmail : Boolean = gettedIntent.
                getBooleanExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false)
        if (isItEmail) {emailEdit.text = (gettedIntent.
                getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO))
                phoneNumEdit.visibility = View.INVISIBLE}
        if (!isItEmail) {phoneNumEdit.text = (gettedIntent.
                getStringExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO))
                emailEdit.visibility = View.INVISIBLE}
        val positionToRemove = gettedIntent.getIntExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, 99)

        removeContact = findViewById(R.id.removeButton)
        removeContact.setOnClickListener {
            setResult(RESULT_OK, answerIntent)
            answerIntent.putExtra(EXTRAS.EXTRA_FOR_CON_REMOVE, positionToRemove)
            finish()
        }
    }
}
