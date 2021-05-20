package com.example.criminalintent;

import java.util.Comparator;

class CrimeComparator implements Comparator<Crime> {

    @Override
    public int compare(Crime crime_1, Crime crime_2) {
        return (crime_1.getID().compareTo(crime_2.getID()));
    }
}
