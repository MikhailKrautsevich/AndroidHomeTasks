package com.example.hometask_04_kotlincontacts

class ContactClass (private val config: Builder) {

    lateinit var name : String
    lateinit var numberOrEmail : String
    var isEmail : Boolean = false

    init {
        this.name = Builder.name
        this.numberOrEmail = Builder.numberOrEmail
        this.isEmail = Builder.isEmail
    }

    companion object Builder {
        lateinit var name : String
        lateinit var numberOrEmail : String
        var isEmail : Boolean = false

        fun setName(name : String) : Builder {
            this.name = name
            return this
        }

        fun setNumberOrEmail (info : String) : Builder {
            this.numberOrEmail = info
            return this
        }

        fun itIsEmail() : Builder {
            this.isEmail = true
            return this
        }

        fun build() : ContactClass {
            val newContact = ContactClass(this)
            return newContact
        }
    }
}