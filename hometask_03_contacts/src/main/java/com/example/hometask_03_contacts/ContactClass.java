package com.example.hometask_03_contacts;


class ContactClass {
    private String name;
    private String numberOrEmail;
    boolean isEmail = false ;

    ContactClass(String name, boolean is, String info)
    {
        setName(name);
        itIsEmail(is);
        setNumberOrEmail(info);
    }

    String getName() {
        return this.name;
    }

    String getNumberOrEmail() {
        return numberOrEmail;
    }

    boolean isItEmail() {return isEmail; }

    private void setNumberOrEmail(String numberOrEmail) {
        this.numberOrEmail = numberOrEmail;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void itIsEmail(boolean is) {this.isEmail = is;}
}
