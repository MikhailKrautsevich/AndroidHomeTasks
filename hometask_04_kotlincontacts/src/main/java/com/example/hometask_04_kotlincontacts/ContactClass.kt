package com.example.hometask_04_kotlincontacts

class ContactClass private constructor() {

    lateinit var name : String
    lateinit var numberOrEmail : String
    var isEmail : Boolean = false

    inner class Builder {
        private lateinit var contact :  ContactClass

        fun setName(name : String) : Builder {
            contact.name = name
            return this
        }

        fun setNumberOrEmail (info : String) : Builder {
            contact.numberOrEmail = info
            return this
        }

        fun itIsEmail() : Builder {
            contact.isEmail = true
            return this
        }

        fun build() : ContactClass {
            return contact
        }
    }
}