package com.example.criminalintent.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class CrimeTypeConverter {

    @TypeConverter
    public Long fromDate(Date date) {
        return date.getTime() ;
    }

    @TypeConverter
    public Date toDate(long l) {
        return new Date(l) ;
    }

    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString() ;
    }

    @TypeConverter
    public UUID toUUID(String s) {
        return UUID.fromString(s) ;
    }

    @TypeConverter
    public boolean toBoolean(String s) {
        return (s.equals(true)) ;
    }

    @TypeConverter
    public String fromBoolean(boolean b) {
        return String.valueOf(b) ;
    }
}
