package com.example.hometask_06;

import java.util.Comparator;

public class ContactsComparator implements Comparator <ContactClass> {
    @Override
    public int compare(ContactClass o1, ContactClass o2) {
        if (!o1.getName().equals(o2.getName())) {
        return o1.getName().compareTo(o2.getName());}
        else {if (o1.isEmail && !o2.isEmail) {return 1;}
        else return 0 ;}
    }
}
