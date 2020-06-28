package com.example.hometask_06;

import com.example.hometask_06.database.ContactEntity;

public class ContactClass {
    private String name;
    private String numberOrEmail;
    boolean isEmail ;

    public ContactClass(String name, String numberOrEmail, boolean isEmail) {
        this.name = name;
        this.numberOrEmail = numberOrEmail;
        this.isEmail = isEmail;
    }

    public String getName() {
        return name;
    }

    public String getNumberOrEmail() {
        return numberOrEmail;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOrEmail(String numberOrEmail) {
        this.numberOrEmail = numberOrEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }

    static ContactEntity createEntity(ContactClass contact) {
        ContactEntity entity = new ContactEntity(contact.getName(), contact.getNumberOrEmail() , contact.isEmail()) ;
        return entity ;
    }
}
