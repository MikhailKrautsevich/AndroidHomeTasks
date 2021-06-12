package com.example.criminalintent.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

class CrimeTypeConverter {

    @SuppressWarnings(value = "all")
    @TypeConverter
    public Long fromDate(Date date) {
        return date.getTime() ;
    }

    @SuppressWarnings(value = "all")
    @TypeConverter
    public Date toDate(long l) {
        return new Date(l) ;
    }

    @SuppressWarnings(value = "all")
    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString() ;
    }

    @SuppressWarnings(value = "all")
    @TypeConverter
    public UUID toUUID(String s) {
        return UUID.fromString(s) ;
    }
}
