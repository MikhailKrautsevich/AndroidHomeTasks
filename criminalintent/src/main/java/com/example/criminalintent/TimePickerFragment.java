package com.example.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

public class TimePickerFragment extends DialogFragment {

    static final String EXTRA_FOR_TIME = "EXTRA_FOR_TIME" ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog_time, null);

        final TimePicker timePicker = (TimePicker) v ;

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(getString(R.string.time_picker_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour ;
                        int minute ;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour() ;
                            minute = timePicker.getMinute() ;
                        } else {
                            hour = timePicker.getCurrentHour() ;
                            minute = timePicker.getCurrentMinute() ;
                        }
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        Date date = calendar.getTime() ;
                        sendTimeResult(date);
                    }
                })
                .create() ;
    }

    private void sendTimeResult(Date date) {
        Intent intent = new Intent() ;
        intent.putExtra(EXTRA_FOR_TIME, date) ;
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
    }
}
