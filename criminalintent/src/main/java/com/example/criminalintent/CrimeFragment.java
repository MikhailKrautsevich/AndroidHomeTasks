package com.example.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

import static android.widget.CompoundButton.OnCheckedChangeListener;

public class CrimeFragment extends Fragment {

    private static final String LOG = "CrimeFragment_log" ;
    private static final String ARG_CRIME_ID = "crime_id" ;

    private Crime mCrime ;
    private EditText mTitleField ;
    private Button mDateButton ;
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

        UUID crimeID  = null;
        if (getArguments() != null) {
            crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        }
        mCrime = CrimeLab.get(getActivity())
                .getCrime(crimeID) ;
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

        mDateButton.setText(
                DateFormat.format( "EEEE, dd MMM, yyyy", mCrime.getDate())
                .toString() );
        mDateButton.setEnabled(false);

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
        View.OnClickListener listener = new CrimeFragmentListener() ;
        mToFirstButton.setOnClickListener(listener);
        mToLastButton.setOnClickListener(listener);

        CrimePagerActivity mActivity = (CrimePagerActivity) getActivity() ;
        if (mActivity != null) {
            if (mActivity.isItTheFirstItem()) {
                mToFirstButton.setEnabled(false);
            }
            if (mActivity.isItTheLastItem()) {
                mToLastButton.setEnabled(false);
            }
            mActivity.changeRightAndLeft(mActivity.getAdapterPos());
            Log.d(LOG, "mActivity.getAdapterPos()" + mActivity.getAdapterPos()) ;
        }
        return view ;
    }

    class CrimeFragmentListener implements View.OnClickListener {
        CrimePagerActivity mActivity = (CrimePagerActivity) getActivity() ;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_first :
                    mActivity.goToTheFirstItem();
                    break;
                case R.id.btn_last:
                    mActivity.goToTheLastItem();
                    break;
                default:
                    Log.d(LOG, "CrimeFragmentListener: default branch.");
            }
        }
    }
}
