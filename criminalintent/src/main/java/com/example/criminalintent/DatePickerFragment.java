package com.example.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date" ;

    static DatePickerFragment newInstance (Date date) {
        Bundle bundle = new Bundle() ;
        bundle.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment() ;
        fragment.setArguments(bundle);
        return fragment ;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null) ;

        Date date = (Date) savedInstanceState.getSerializable(ARG_DATE) ;
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR) ;
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(calendar.DAY_OF_MONTH) ;

        DatePicker datePicker = (DatePicker) v ;
        datePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.date_picker_title))
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
