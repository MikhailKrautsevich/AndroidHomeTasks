package com.example.hometask_06.database;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices ={@Index(value = {"name"})} )
public class ContactEntity {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String name ;
    private String numberOrEmail;
    private boolean isEmail ;

    public ContactEntity(String name, String numberOrEmail, boolean isEmail) {
        this.name = name;
        this.numberOrEmail = numberOrEmail;
        this.isEmail = isEmail;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setNumberOrEmail(String numberOrEmail) {
        this.numberOrEmail = numberOrEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }
}
