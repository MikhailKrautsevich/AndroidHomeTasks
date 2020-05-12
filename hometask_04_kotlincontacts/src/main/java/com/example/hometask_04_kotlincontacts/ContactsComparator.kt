package com.example.hometask_04_kotlincontacts

class ContactsComparator : Comparator<ContactClass> {

    override fun compare(con1: ContactClass?, con2: ContactClass?): Int {
        var result = 0
        if (con1 != null && con2 != null) {
            if ((con1.name) != con2.name) {
                result =  con1.name.compareTo(con2.name)
            } else if (con1.isEmail && !con2.isEmail) {
                result = 1}
        }
        return result
    }
}