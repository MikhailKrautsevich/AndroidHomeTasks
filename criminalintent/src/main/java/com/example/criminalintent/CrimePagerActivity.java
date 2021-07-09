package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity
    implements CrimeFragment.Callbacks {

    private static final String EXTRA_CRIME_ID = "com.example.criminalIntent.crime_id" ;
    private static final String EXTRA_LEFT_EVENT = "EXTRA_LEFT_EVENT" ;
    private static final String EXTRA_RIGHT_EVENT = "EXTRA_RIGHT_EVENT" ;
    private static final String EXTRA_DELETE = "EXTRA_DELETE" ;
    private static final String LOG = "CrimePagerActivity_log" ;

    private static int sLeftEvent ;
    private static int sRightEvent ;

    private ViewPager mViewPager ;
    private List<Crime> mCrimes ;
    private Intent mIntent ;

    public static Intent newIntent(Context packageContext, UUID crimeID) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class) ;
        intent.putExtra(EXTRA_CRIME_ID, crimeID) ;
        return intent ;
    }

    static int getLeftEvent(Intent data) {
        return data.getIntExtra(EXTRA_LEFT_EVENT, 0) ;
    }

    static int getRightEvent(Intent data) {
        return data.getIntExtra(EXTRA_RIGHT_EVENT, 0) ;
    }

    static boolean getDeleted(Intent data) {
        return data.getBooleanExtra(EXTRA_DELETE, false) ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = findViewById(R.id.crime_view_pager) ;

        mCrimes = CrimeLab.get(this).getCrimes() ;
        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID) ;
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position) ;
                Log.d(LOG, "getItem(int position): crime.getDate = " + crime.getDate().toString() );
                return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0 ; i < mCrimes.size(); i++ ) {
            if (mCrimes.get(i).getID().equals(crimeID)) {
                mViewPager.setCurrentItem(i);
                sRightEvent = i ;
                sLeftEvent = i ;
                break;
            }
        }

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float opacity = Math.abs(Math.abs(position) - 1) ;
                page.setAlpha(opacity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void goToTheFirstItem() {
        sLeftEvent = 0 ;
        mViewPager.setCurrentItem(0);
    }

    void  goToTheLastItem() {
        int lastIndex = mCrimes.size() - 1 ;
        sRightEvent = lastIndex ;
        mViewPager.setCurrentItem(lastIndex);
    }

    int getAdapterPos() {
        return mViewPager.getCurrentItem() ;
    }

    void changeRightAndLeft(int pos) {
        Log.d(LOG, "changeRightAndLeft: pos = " + pos) ;
        if (mIntent == null) {
            mIntent = new Intent() ;
            setResult(RESULT_OK, mIntent);
            Log.d(LOG, "changeRightAndLeft: create new Intent") ;
        }
        if (pos < sLeftEvent) {
            sLeftEvent = pos ;
            Log.d(LOG, "changeRightAndLeft: sLeftEvent to " + pos) ;
        } else if (pos > sRightEvent) {
            sRightEvent = pos ;
            Log.d(LOG, "changeRightAndLeft: sRightEvent to " + pos) ;
        }
        mIntent.putExtra(EXTRA_LEFT_EVENT, sLeftEvent) ;
        mIntent.putExtra(EXTRA_RIGHT_EVENT, sRightEvent) ;
    }

    void deleteCrime() {
        if (mIntent == null) {
            mIntent = new Intent() ;
        }
        setResult(RESULT_OK, mIntent);
        mIntent.putExtra(EXTRA_DELETE, true) ;
    }

    boolean isItTheFirstItem(Crime crime) {
        return (mCrimes.get(0).getID().equals(crime.getID())) ;
    }

    boolean isItTheLastItem(Crime crime) {
        return ((mCrimes.get(mCrimes.size()-1).getID().equals(crime.getID())));
    }

    void put0ToLeft() {sLeftEvent = 0 ;}

    void put0ToRight() {sRightEvent = 0 ;}

    @Override
    public void onCrimeUpdated(Crime crime) {
        // пустая реализация
    }
}
