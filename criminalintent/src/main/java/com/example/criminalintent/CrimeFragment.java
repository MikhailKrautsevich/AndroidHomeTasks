package com.example.criminalintent;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

;

public class CrimeFragment extends Fragment {

    private static final String LOG = "CrimeFragment_log";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_PHOTO_BIG = "DialogBigPhoto";
    private static final int REQUEST_DATE = 211;
    private static final int REQUEST_TIME = 222;
    private static final int REQUEST_CONTACT = 233;
    private static final int REQUEST_PHOTO = 244;

    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspect;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private Button mToFirstButton;
    private Button mToLastButton;
    private CheckBox mSolvedCheckBox;
    private Callbacks mCallbacks;
    private final Intent mCaptureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeID;
        if (getArguments() != null) {
            crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
            mCrime = CrimeLab.get(getActivity())
                    .getCrime(crimeID);
            Log.d(LOG, "CrimeFragment: OnCreate: mCrime = " + mCrime);
            mPhotoFile = CrimeLab.get(getActivity())
                    .getPhotoFile(mCrime);
        } else {
            mCrime = new Crime();
            mCrime.setTitle(getString(R.string.smth_wrong));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPhotoView = view.findViewById(R.id.crime_photo);
        mPhotoButton = view.findViewById(R.id.crime_camera);

        mDateButton = view.findViewById(R.id.crime_date);
        updateDate(mCrime.getDate());

        mTimeButton = view.findViewById(R.id.crime_time);
        updateTime(new Date());

        mReportButton = view.findViewById(R.id.crime_report);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mCallSuspect = view.findViewById(R.id.call_suspect);

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.getSolved());
        setSolvedCheckBoxDescription();
        mSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mCrime.setSolved(isChecked);
            Log.d(LOG, "mSolvedCheckBox: " + isChecked);
            updateCrime();
            setSolvedCheckBoxDescription();
        });

        mToFirstButton = view.findViewById(R.id.btn_first);
        mToLastButton = view.findViewById(R.id.btn_last);
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        View.OnClickListener listener = new CrimeFragmentListener(pickContact);
        mToFirstButton.setOnClickListener(listener);
        mToLastButton.setOnClickListener(listener);
        mDateButton.setOnClickListener(listener);
        mTimeButton.setOnClickListener(listener);
        mReportButton.setOnClickListener(listener);
        mSuspectButton.setOnClickListener(listener);
        mCallSuspect.setOnClickListener(listener);
        mPhotoButton.setOnClickListener(listener);
        mPhotoView.setOnClickListener(listener);

        CrimePagerActivity mActivity = (CrimePagerActivity) getActivity();
        if (mActivity != null) {
            if (mActivity.isItTheFirstItem(mCrime)) {
                Log.d(LOG, "CrimeFragment: mActivity.isItTheFirstItem()) = " + mActivity.isItTheFirstItem(mCrime));
                mActivity.put0ToLeft();
                mToFirstButton.setEnabled(false);
            }
            if (mActivity.isItTheLastItem(mCrime)) {
                Log.d(LOG, "CrimeFragment: mActivity.isItTheLastItem()) = " + mActivity.isItTheLastItem(mCrime));
                mActivity.put0ToRight();
                mToLastButton.setEnabled(false);
            }
            mActivity.changeRightAndLeft(mActivity.getAdapterPos());
            Log.d(LOG, "mActivity.getAdapterPos() = " + mActivity.getAdapterPos());
        }

        setSuspect(mCrime.getSuspect());

//        pickContact.addCategory(Intent.CATEGORY_HOME) ;                                           //    makes mSuspect enable
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        boolean canTakePhoto = mPhotoFile != null &&
                mCaptureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isAdded() && mPhotoFile != null && mPhotoFile.exists()) {
                    int width = mPhotoView.getWidth();
                    int height = mPhotoButton.getHeight();
                    Log.d(LOG, "onGlobalLayout: width = " + width + ", height = " + height);
                    Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), width, height);
                    mPhotoView.setImageBitmap(bitmap);
                    if (!mPhotoView.isEnabled()) {
                        mPhotoView.setEnabled(true);
                    }
                    setCrimePhotoDescription();
                }
            }
        });

//        updatePhotoView();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                CrimePagerActivity activity = (CrimePagerActivity) getActivity();
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
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate(date);
            updateCrime();
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_FOR_TIME);
            updateTime(date);
        }
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri conUri = data.getData();
            String[] queryString = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c = getActivity().getContentResolver()
                    .query(conUri, queryString, null, null, null);
            try {
                if (c == null || c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                updateCrime();
                setSuspect(suspect);
            } finally {
                c.close();
            }
        }
        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.example.criminalintent.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
            updateCrime();
        }
    }

    private void updateCrime() {
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    private void updateDate(Date date) {
        String formattedDate = MyDateUtil.getFormattedDate(date, getContext());
        mDateButton.setText(formattedDate);
        String newDescription = getString(R.string.date_btn_description)
                + ' '
                + getString(R.string.date_btn_new_description)
                + formattedDate;
        mDateButton.setContentDescription(newDescription);
        Log.d(LOG, newDescription);
    }

    private String getCrimeReport() {
        String solvedString;
        if (mCrime.getSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateString = DateFormatter.getFormattedDate(mCrime.getDate(), getContext());

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspect);
    }

    private void setSuspect(String suspect) {
        if (suspect != null) {
            mSuspectButton.setText(suspect);
            mSuspectButton.setContentDescription(getString(R.string.suspect_btn_new_description, suspect));
            mCallSuspect.setVisibility(View.VISIBLE);
            setSuspectNameToCallBtn(suspect);
        } else {
            mSuspectButton.setText(getString(R.string.crime_suspect_text));
            mSuspectButton.setContentDescription(getString(R.string.suspect_btn_description));
            mCallSuspect.setVisibility(View.GONE);
        }
    }

    private void setSuspectNameToCallBtn(String suspect) {
        mCallSuspect.setText(getString(R.string.call_suspect, suspect));
        mCallSuspect.setContentDescription(getString(R.string.call_suspect_btn_description, suspect));
    }

    private void updateTime(Date date) {
        mTimeButton.setText(DateFormatter.getFormattedTime(date, getContext()));
    }

    private void setCrimePhotoDescription() {
        mPhotoView.setContentDescription(getString(R.string.crime_photo_image_description));
    }

    private void setSolvedCheckBoxDescription() {
        boolean solved = mSolvedCheckBox.isChecked();
        if (solved) {
            mSolvedCheckBox.setContentDescription(getString(R.string.solved_checkbox_description));
            Log.d(LOG, "CrimeFragment: I set SOLVED checkbox description");
        } else {
            mSolvedCheckBox.setContentDescription(getString(R.string.unsolved_checkbox_description));
            Log.d(LOG, "CrimeFragment: I set UNSOLVED checkbox description");
        }
    }

    private void updatePhotoView() {
        Log.d(LOG, "CrimeFragmentListener: updatePhotoView() called");
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            Log.d(LOG, "CrimeFragmentListener: updatePhotoView() : null branch ");
            Log.d(LOG, "CrimeFragmentListener: updatePhotoView() : mPhotoFile == null " + (mPhotoFile == null));
            Log.d(LOG, "CrimeFragmentListener: updatePhotoView() : mPhotoFile.exists() " + (mPhotoFile.exists()));
            mPhotoView.setEnabled(false);
            mPhotoView.setContentDescription(getString(R.string.crime_photo_no_image_description));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            Log.d(LOG, "CrimeFragmentListener: updatePhotoView() : try to setImageBitmap ");
            mPhotoView.setEnabled(true);
            setCrimePhotoDescription();
        }
    }

    class CrimeFragmentListener implements View.OnClickListener {

        CrimePagerActivity mActivity;
        FragmentManager fragmentManager;
        Intent pickIntent;

        CrimeFragmentListener(Intent intent) {
            mActivity = (CrimePagerActivity) getActivity();
            fragmentManager = getFragmentManager();
            pickIntent = intent;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_first:
                    mActivity.goToTheFirstItem();
                    break;
                case R.id.btn_last:
                    mActivity.goToTheLastItem();
                    break;
                case R.id.crime_date:
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    dialog.show(fragmentManager, DIALOG_DATE);
                    break;
                case R.id.crime_time:
                    TimePickerFragment timePickerFragment = new TimePickerFragment();
                    timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    timePickerFragment.show(fragmentManager, DIALOG_TIME);
                    break;
                case R.id.crime_report:
//                    ShareCompat.IntentBuilder
//                            .from(getActivity())
//                            .setType("text/plain")
//                            .setText(getCrimeReport())
//                            .setChooserTitle(getString(R.string.send_report))
//                            .startChooser();

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                    i = Intent.createChooser(i, getString(R.string.send_report));
                    startActivity(i);
                    break;
                case R.id.crime_suspect:
                    startActivityForResult(pickIntent, REQUEST_CONTACT);
                    break;
                case R.id.call_suspect:
                    Log.d(LOG, "CrimeFragmentListener: R.id.call_suspect:start");
                    if (mCrime.getSuspect() != null) {
                        String[] name = {mCrime.getSuspect()};
                        String[] queryID = new String[]{ContactsContract.Contacts._ID};
                        ContentResolver cr = getActivity().getContentResolver();
                        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
                                queryID,
                                ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                                name,
                                null);
                        String[] id;
                        String phNum;
                        if (c != null) {
                            try {
                                if (c.getCount() == 0) {
                                    Log.d(LOG, "CrimeFragmentListener: R.id.call_suspect: (c.getCount() == 0 ");
                                    return;
                                }
                                c.moveToFirst();
                                id = new String[]{c.getString(0)};
                                Log.d(LOG, "CrimeFragmentListener: R.id.call_suspect: id[0] = " + id[0]);
                            } finally {
                                c.close();
                            }
                            if (id[0] != null) {
                                String[] queryPhNum = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                                Cursor c1 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        queryPhNum,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        id,
                                        null);
                                if (c1 != null) {
                                    try {
                                        if (c1.getCount() == 0) {
                                            Log.d(LOG, "CrimeFragmentListener: R.id.call_suspect: (c1.getCount() == 0 ");
                                            return;
                                        }
                                        c1.moveToFirst();
                                        phNum = c1.getString(0);
                                    } finally {
                                        c1.close();
                                    }
                                    if (phNum != null) {
                                        Log.d(LOG, "CrimeFragmentListener: R.id.call_suspect: phNum == " + phNum);
                                        Uri phNumUri = Uri.parse("tel:" + phNum);
                                        Intent phIntent = new Intent(Intent.ACTION_DIAL);
                                        phIntent.setData(phNumUri);
                                        startActivity(phIntent);
                                    }
                                }
                            }
                        }
                        break;
                    }
                case R.id.crime_camera:
                    Uri uri = FileProvider.getUriForFile(getActivity(),
                            "com.example.criminalintent.fileprovider",
                            mPhotoFile);
                    mCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    List<ResolveInfo> cameraActivities = getActivity()
                            .getPackageManager()
                            .queryIntentActivities(mCaptureImage, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo activity : cameraActivities) {
                        getActivity()
                                .grantUriPermission(activity.activityInfo.packageName,
                                        uri,
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    startActivityForResult(mCaptureImage, REQUEST_PHOTO);
                    break;
                case R.id.crime_photo:
                    if (mPhotoFile != null && mPhotoFile.exists()) {
                        Log.d(LOG, "CrimeFragmentListener: R.id.crime_photo");
                        PhotoDialog photoDialog = PhotoDialog.newInstance(mPhotoFile);
                        photoDialog.show(fragmentManager, DIALOG_PHOTO_BIG);
                    }
                    break;
                default:
                    Log.d(LOG, "CrimeFragmentListener: default branch.");
                    break;
            }
        }
    }
}
