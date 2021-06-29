package com.example.criminalintent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.CompoundButton.OnCheckedChangeListener;

public class CrimeFragment extends Fragment {

    private static final String LOG = "CrimeFragment_log" ;
    private static final String ARG_CRIME_ID = "crime_id" ;
    private static final String DIALOG_DATE = "DialogDate" ;
    private static final String DIALOG_TIME = "DialogTime" ;
    private static final int REQUEST_DATE = 211 ;
    private static final int REQUEST_TIME = 222 ;
    private static final int REQUEST_CONTACT = 233 ;

    private Crime mCrime ;
    private EditText mTitleField ;
    private Button mDateButton ;
    private Button mTimeButton ;
    private Button mReportButton ;
    private Button mSuspectButton ;
    private Button mToFirstButton ;
    private Button mToLastButton ;
    private CheckBox mSolvedCheckBox ;

    static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle() ;
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment fragment = new CrimeFragment() ;
        fragment.setArguments(args);
        return fragment ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeID  = null;
        if (getArguments() != null) {
            crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
            mCrime = CrimeLab.get(getActivity())
                    .getCrime(crimeID) ;
        } else {
            mCrime = new Crime() ;
            mCrime.setTitle(getString(R.string.smth_wrong));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false) ;

        mTitleField = view.findViewById(R.id.crime_title) ;
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date) ;
        updateDate(mCrime.getDate());

        mTimeButton = view.findViewById(R.id.crime_time) ;
        updateTime(new Date());

        mReportButton = view.findViewById(R.id.crime_report) ;
        mSuspectButton = view.findViewById(R.id.crime_suspect) ;

        mSolvedCheckBox = view.findViewById(R.id.crime_solved) ;
        mSolvedCheckBox.setChecked(mCrime.getSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                Log.d(LOG, "mSolvedCheckBox: " + isChecked) ;
            }
        });

        mToFirstButton = view.findViewById(R.id.btn_first) ;
        mToLastButton = view.findViewById(R.id.btn_last) ;
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI) ;
        View.OnClickListener listener = new CrimeFragmentListener(pickContact) ;
        mToFirstButton.setOnClickListener(listener);
        mToLastButton.setOnClickListener(listener);
        mDateButton.setOnClickListener(listener);
        mTimeButton.setOnClickListener(listener);
        mReportButton.setOnClickListener(listener);
        mSuspectButton.setOnClickListener(listener);

        CrimePagerActivity mActivity = (CrimePagerActivity) getActivity() ;
        if (mActivity != null) {
            if (mActivity.isItTheFirstItem(mCrime)) {
                Log.d(LOG, "CrimeFragment: mActivity.isItTheFirstItem()) = " + mActivity.isItTheFirstItem(mCrime)) ;
                mActivity.put0ToLeft();
                mToFirstButton.setEnabled(false);
            }
            if (mActivity.isItTheLastItem(mCrime)) {
                Log.d(LOG, "CrimeFragment: mActivity.isItTheLastItem()) = " + mActivity.isItTheLastItem(mCrime)) ;
                mActivity.put0ToRight();
                mToLastButton.setEnabled(false);
            }
            mActivity.changeRightAndLeft(mActivity.getAdapterPos());
            Log.d(LOG, "mActivity.getAdapterPos() = " + mActivity.getAdapterPos()) ;
        }

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

//        pickContact.addCategory(Intent.CATEGORY_HOME) ;                                           //    makes mSuspect enable
        PackageManager packageManager = getActivity().getPackageManager() ;
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        return view ;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime :
                CrimePagerActivity activity = (CrimePagerActivity) getActivity() ;
                CrimeLab.get(activity).deleteCrime(mCrime.getID());
                activity.deleteCrime();
                activity.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE) ;
            mCrime.setDate(date) ;
            updateDate(date);
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_FOR_TIME) ;
            updateTime(date);
        }
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri conUri = data.getData() ;
            String[] queryString = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getActivity().getContentResolver()
                    .query(conUri, queryString, null, null, null) ;
            try {
                if (c.getCount() == 0) {return;}
                c.moveToFirst();
                String suspect = c.getString(0) ;
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }
    }

    private void updateDate(Date date) {
        mDateButton.setText(getFormattedDate(date));
    }

    private String getFormattedDate(Date date) {
        return DateFormat
                .format( "EEEE, dd MMM, yyyy", date)
                .toString();
    }

    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.getSolved()) {
            solvedString = getString(R.string.crime_report_solved) ;
        } else {
            solvedString = getString(R.string.crime_report_unsolved) ;
        }

        String dateFormat = "EEE, MMM dd" ;
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString() ;            // источник русского в коде

        String suspect = mCrime.getSuspect() ;
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect) ;
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect) ;
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspect) ;

        return report ;
    }

    private void updateTime (Date date) {
        mTimeButton.setText(getFormattedTime(date)) ;
    }

    private String getFormattedTime(Date date) {
        return DateFormat
                .format("HH:mm", date)
                .toString() ;
    }

    class CrimeFragmentListener implements View.OnClickListener {

        CrimePagerActivity mActivity ;
        FragmentManager fragmentManager ;
        Intent pickIntent;

         CrimeFragmentListener(Intent intent) {
             mActivity = (CrimePagerActivity) getActivity() ;
             fragmentManager = getFragmentManager() ;
             pickIntent = intent;
         }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_first :
                    mActivity.goToTheFirstItem();
                    break;
                case R.id.btn_last:
                    mActivity.goToTheLastItem();
                    break;
                case R.id.crime_date:
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate()) ;
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    dialog.show(fragmentManager, DIALOG_DATE);
                    break;
                case R.id.crime_time:
                    TimePickerFragment timePickerFragment = new TimePickerFragment() ;
                    timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    timePickerFragment.show(fragmentManager, DIALOG_TIME) ;
                    break;
                case R.id.crime_report:
                    Intent i = new Intent(Intent.ACTION_SEND) ;
                    i.setType("text/plain") ;
                    i.putExtra(Intent.EXTRA_TEXT, getCrimeReport()) ;
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject)) ;
                    i = Intent.createChooser(i, getString(R.string.send_report)) ;
                    startActivity(i);
                    break;
                case R.id.crime_suspect:
                    startActivityForResult(pickIntent, REQUEST_CONTACT);

                default:
                    Log.d(LOG, "CrimeFragmentListener: default branch.");
            }
        }
    }
}
