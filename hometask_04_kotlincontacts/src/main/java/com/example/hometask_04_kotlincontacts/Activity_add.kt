package com.example.hometask_04_kotlincontacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast

class Activity_add : Activity() {
    private lateinit var nameAddText : EditText
    private lateinit var phoneNumberAdd : EditText
    private lateinit var emailAdd : EditText
    private lateinit var phoneNumberRadButton : RadioButton
    private lateinit var emailRadButton : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val answerIntent = Intent()

        phoneNumberRadButton = findViewById(R.id.phoneNumberRadButton)
        emailRadButton = findViewById(R.id.emailRadButton)

        nameAddText = findViewById(R.id.nameAddText)
        phoneNumberAdd = findViewById(R.id.phoneNumberAdd)
        emailAdd = findViewById(R.id.emailAdd)
        emailAdd.visibility = View.INVISIBLE

        val backFromAdd : ImageView = findViewById(R.id.backFromAdd)
        backFromAdd.setOnClickListener{
            setResult(RESULT_CANCELED, answerIntent)
            finish()
        }

        val addCorfirm : ImageView = findViewById(R.id.addConfirm)
        addCorfirm.setOnClickListener {
            val name = nameAddText.text.toString().trim()
            val email = emailAdd.text.toString().trim()
            val phoneNumber = phoneNumberAdd.text.toString().trim()
            if (!phoneNumberRadButton.isChecked && !emailRadButton.isChecked) {
                Toast.makeText(applicationContext, R.string.choosecontype, Toast.LENGTH_SHORT)
                        .show()
            }
            if (phoneNumberRadButton.isChecked && !emailRadButton.isChecked){
                Toast.makeText(applicationContext, R.string.wilsavephnumber, Toast.LENGTH_SHORT)
                        .show()
                if (name.isNotEmpty() && phoneNumber.isNotEmpty() && ("\\+?[0-9]*".toRegex()).matches(phoneNumber) ) {
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, nameAddText.text
                            .toString())
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, phoneNumberAdd.text
                            .toString())
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, false)
                    setResult(RESULT_OK, answerIntent)
                    finish()}
                else {Toast.makeText(applicationContext, R.string.somethingwrongwithc, Toast.LENGTH_SHORT)
                        .show()}
            }
            if (!phoneNumberRadButton.isChecked &&  emailRadButton.isChecked) {
                Toast.makeText(applicationContext, R.string.willsaveemail, Toast.LENGTH_SHORT)
                        .show()
                if (name.isNotEmpty() && email.isNotEmpty() && !("[а-яёА-ЯЁ]*".toRegex()).matches(email)) {
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_NAME, nameAddText.text
                            .toString())
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_INFO, emailAdd.text
                            .toString())
                    answerIntent.putExtra(EXTRAS.EXTRA_FOR_CONTACT_IS, true)
                    setResult(RESULT_OK, answerIntent)
                    finish()}
                else {Toast.makeText(applicationContext, R.string.somethingwrongwithc, Toast.LENGTH_SHORT)
                        .show()}
            }
            if ( phoneNumberRadButton.isChecked && emailRadButton.isChecked) {
                Toast.makeText(applicationContext, R.string.youhavetochoose, Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    fun onRadioClick(view : View) {
        val rb : RadioButton = view as RadioButton
        when (rb.id) {
            R.id.phoneNumberRadButton -> {
                phoneNumberAdd.visibility = View.VISIBLE
                emailAdd.visibility = View.INVISIBLE
            }
            R.id.emailRadButton -> {
                phoneNumberAdd.visibility = View.INVISIBLE
                emailAdd.visibility = View.VISIBLE
            }
        }
    }
}
