package com.example.hometask_06.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hometask_06.ContactClass;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * from ContactEntity")
    List<ContactClass> getAllContacts() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addContact(ContactEntity contactEntity) ;

    @Delete
    void deleteContact(ContactEntity contactEntity);

    @Update
    void updateContact(ContactEntity contactEntity);
}
